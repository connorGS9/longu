##Question 1 
Earlier, I said that the |, *, and + forms we added to our grammar metasyntax were just syntactic sugar. Take this grammar:

expr → expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+
     | IDENTIFIER
     | NUMBER
Produce a grammar that matches the same language but does not use any of that notational sugar.

Bonus: What kind of expression does this bit of grammar encode?

###Answer 1
Change the sugary notation into expressed rules (ε epsilon means null or nothing (stop recursion))

1. ( "," expr )*
exprList → "," expr exprList (comma followed by an expression followed by more exprLists or null)
exprList → ε 

2. (expr ( "," expr )* )?
arguments → expr exprList
arguments → ε

3. "(" arguments ")" | "." IDENTIFIER
callOrAccess → "(" arguments ")"
callOrAccess → "." IDENTIFIER

4. ( callOrAccess )+
chain → callOrAccess chain
chain → callOrAccess

5. expr → expr chain | IDENTIFIER | NUMBER
expr → expr chain
expr → IDENTIFIER
expr → NUMBER

_BONUS_ The grammar encodes chained function calls and property accesses: expressions where you start from a base (an identifier or number) and chain any number of calls (...) and dot-accesses .name onto it
EX: foo.bar(a,b)
expr → expr chain, where the inner expr is foo and chain is .bar(a, b)
inner expr → IDENTIFIER matches foo
chain → callOrAccess chain splits .bar(a, b) into .bar and (a, b)
callOrAccess → "." IDENTIFIER matches .bar
chain → callOrAccess then callOrAccess → "(" arguments ")" matches (a, b)
arguments → expr exprList matches a, then , b via exprList → "," expr exprList

##Question 2
The Visitor pattern lets you emulate the functional style in an object-oriented language. Devise a complementary pattern for a functional language. It should let you bundle all of the operations on one type together and let you define new types easily.

(SML or Haskell would be ideal for this exercise, but Scheme or another Lisp works as well.)

###Answer 2


##Question 3
In reverse Polish notation (RPN), the operands to an arithmetic operator are both placed before the operator, so 1 + 2 becomes 1 2 +. Evaluation proceeds from left to right. Numbers are pushed onto an implicit stack. An arithmetic operator pops the top two numbers, performs the operation, and pushes the result. Thus, this:

(1 + 2) * (4 - 3)
in RPN becomes:

1 2 + 4 3 - *
Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN, and returns the resulting string.

###Answer 3 
com/cratinginterpreters/longu/RpnPrinter.java

Change parenthesize() to output types seperated by spaces followed by the name (operator) at the end and change grouping() to just return the base expression: expr.accept(this) 
