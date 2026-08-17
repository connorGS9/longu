#include <stdio.h>    // printf, for printing the list and test output
#include <stdlib.h>   // malloc, free, exit
#include <string.h>   // strcmp (find), strlen + memcpy (copying the string)
#include <assert.h>   // assert, for the tests

typedef struct Node { //Individual nodes hold the heap allocated string, and two pointers to previous and next nodes
    char *value;         // pointer to the characters, not the characters themselves
    struct Node *next;   // forward
    struct Node *prev;   // backward
} Node;

typedef struct { //The passable list object with direct access to head and tail nodes
    Node *head;
    Node *tail;
} List;

//Prototype method signatures so that main can use them while living above them in scope. Note they come after structs because they reference them
void list_init(List* list);
void insert(List* ll, const char* strValue);
Node* find(List* ll, const char* strValue);
void delete(List* ll, const char* strToDelete);
void freeList(List* ll);

int main(int argc, char *argv[]) {
    List ll;
    list_init(&ll);

    //for (int i = 1; i < argc; i++) {
        //insert(&doublyLinkedList, argv[i]);
    //}

    insert(&ll, "apple");
    insert(&ll, "banana");
    insert(&ll, "cherry");
 
    /* ---- 1. find: hit and miss ---- */
    assert(find(&ll, "apple")  != NULL);
    assert(find(&ll, "banana") != NULL);
    assert(find(&ll, "cherry") != NULL);
    assert(find(&ll, "ghost")  == NULL);          /* absent -> NULL */
    printf("test 1 (find hit/miss) passed\n");
 
    /* ---- 2. delete the MIDDLE (banana): both neighbors relink ---- */
    delete(&ll, "banana");
    assert(find(&ll, "banana") == NULL);          /* gone */
    assert(strcmp(ll.head->value, "apple")  == 0);/* head unchanged */
    assert(strcmp(ll.tail->value, "cherry") == 0);/* tail unchanged */
    /* prove the chain is intact BOTH directions across the gap */
    assert(strcmp(ll.head->next->value, "cherry") == 0); /* apple -> cherry */
    assert(strcmp(ll.tail->prev->value, "apple")  == 0); /* cherry -> apple */
    printf("test 2 (delete middle) passed\n");
 
    /* ---- 3. delete the HEAD (apple): ll->head must move ---- */
    delete(&ll, "apple");
    assert(find(&ll, "apple") == NULL);
    assert(strcmp(ll.head->value, "cherry") == 0);/* cherry is the new head */
    assert(ll.head->prev == NULL);                /* new head has no prev */
    printf("test 3 (delete head) passed\n");
 
    /* ---- 4. delete the TAIL (cherry): now the only node -> also case 5 ---- */
    delete(&ll, "cherry");
    assert(find(&ll, "cherry") == NULL);
    assert(ll.head == NULL && ll.tail == NULL);   /* single-node delete empties list */
    printf("test 4+5 (delete tail / last node empties list) passed\n");
 
    /* ---- 6. delete something ABSENT from an empty list: no crash, no change ---- */
    delete(&ll, "ghost");
    assert(ll.head == NULL && ll.tail == NULL);
    printf("test 6 (delete absent) passed\n");
 
    /* ---- 7. list is reusable after being emptied ---- */
    insert(&ll, "reused");
    assert(ll.head == ll.tail);                   /* one node is both ends */
    assert(strcmp(ll.head->value, "reused") == 0);
    printf("test 7 (reuse after empty) passed\n");
 
    list_free(&ll);
    assert(ll.head == NULL && ll.tail == NULL);   /* free leaves it clean */
 
    printf("\nall tests passed\n");

    list_free(&ll);
    return 0;
}

void list_init(List* list) {
    list->head = NULL;
    list->tail = NULL;
}

void insert(List* ll, const char* strValue) {

    Node* currNode = malloc(sizeof(Node));
    size_t len = strlen(strValue) + 1; //length of value will be sie of word + 1 for null terminator "A,p,p,l,e,\0" size = 6
    currNode->value = malloc(len); //Allocate len bytes to the value
    memcpy(currNode->value, strValue, len); //Copy strValue into the value field and denote its length as len
    currNode->next = NULL; //Set next to be null as this new node will be the tail no matter what it contains

    if (ll->head == NULL) { //This node is the first node it is both head and tail
        ll->head = currNode;
        ll->tail = currNode;
        currNode->prev = NULL;
    } else {
        ll->tail->next = currNode; //Add the new node after the previous tail and make it the tail
        currNode->prev = ll->tail; //Point back to previous tail
        ll->tail = currNode; //Make currNode the new tail
    }
}

Node* find(List* ll, const char* strValue) {
    //Return the node containing strvalue if it exists or NULL
    for (Node* curr = ll->head; curr != NULL; curr = curr->next) { //Starting from head iterate to next looking for strValue
        if (strcmp(curr->value, strValue) == 0) return curr; //strcmp == 0 when the two strings match so we can return the currentNode
    }
    return NULL;
}

void delete(List* ll, const char* strToDelete) {
    for (Node* curr = ll->head; curr != NULL; curr = curr->next) { //Starting from head iterate to next looking for strValue
        if (strcmp(curr->value, strToDelete) == 0) {
            // Fix the left side 
            if (curr->prev == NULL) { //Curr is head
                ll->head = curr->next;
            } else { // Curr is not head and has something before it 
                curr->prev->next = curr->next;
            }
            // Fix the right side
            if (curr->next == NULL) {        // Curr was the tail
                ll->tail = curr->prev;
            } else {                         // Curr had a node after it
                curr->next->prev = curr->prev;
            }
            free(curr->value);   //free the string first
            free(curr);          //free the node itself
            return;     // End search
        }
    }
}

void list_free(List* ll) {
    Node* curr = ll->head;
    while (curr != NULL) {
        Node* next = curr->next;   // save the link BEFORE destroying curr
        free(curr->value);         // string first
        free(curr);                // then the node
        curr = next;               // advance to the saved pointer
    }
    ll->head = NULL;
    ll->tail = NULL;
}