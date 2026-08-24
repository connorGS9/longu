## Question 1 
Earlier, I said that the |, *, and + forms we added to our grammar metasyntax were just syntactic sugar. Take this grammar:

expr → expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+
     | IDENTIFIER
     | NUMBER
Produce a grammar that matches the same language but does not use any of that notational sugar.

Bonus: What kind of expression does this bit of grammar encode?

### Answer 1
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

## Question 2
The Visitor pattern lets you emulate the functional style in an object-oriented language. Devise a complementary pattern for a functional language. It should let you bundle all of the operations on one type together and let you define new types easily.

(SML or Haskell would be ideal for this exercise, but Scheme or another Lisp works as well.)

### Answer 2
Regarding the expression problem we see that in our figurative table where rows are types (binary, unary, etc..) and columns are operations done on those types.
In OOP adding new types (rows) is easy since we just create a new class for that type and override the methods that come from the type interface, however adding a new operation to be performed is a hassle since for every single type we need to go back and add the new operation method(s). 

In functional languages it is the opposite adding new operations is easy since we just need to write a single function that pattern matches to each individual type, however adding a new type requires editing all of the operation methods to add a new case for the new type.

We saw how the visitor pattern solved adding new operations for OOP, but to attempt to solve adding new types to a functional language is a different challenge. In Haskell this is solved with a type class; in Scheme it is a record (or dictionary) of functions. The idea is that we create an interface listingthe operations, so if we need to add a new type, we just add an instance defining the behavior for each operation for that type.

In Haskell-ish code: 
'''
class Expr a where
  interpret :: a -> Value
  printExpr :: a -> String
  toRpn     :: a -> String

-- adding a new type = one instance bundling all its operations
instance Expr Binary where
  interpret b = ...
  printExpr b = ...
  toRpn     b = ...
'''

## Question 3
In reverse Polish notation (RPN), the operands to an arithmetic operator are both placed before the operator, so 1 + 2 becomes 1 2 +. Evaluation proceeds from left to right. Numbers are pushed onto an implicit stack. An arithmetic operator pops the top two numbers, performs the operation, and pushes the result. Thus, this:

(1 + 2) * (4 - 3)
in RPN becomes:

1 2 + 4 3 - *
Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN, and returns the resulting string.

### Answer 3 
com/cratinginterpreters/longu/RpnPrinter.java

Change parenthesize() to output types seperated by spaces followed by the name (operator) at the end and change grouping() to just return the base expression: expr.accept(this) 
