***Parts of  A Language Frontend*** 
*From Crafting Interpreters (mainly)*

$ Example stream from backtesting project: PortfolioValue = cash + share * closePrice

# Lexing / Scanning: Take a stream of characters and turn them into usable tokens, disregarding things like comments and whitespace. 
$ PortFolioValue | = | cash | + | share | * | closePrice        Lexing may turn our stream into 7 unique tokens for example 

# Parsing: Take your scanned tokens and form a tree that is something akin to a sentence with correct structure and ordering (Abstract Syntax Tree) which will allow our many tokens to form larger more complex equations
  *Parsing is also responsible for reporting Syntax errors for your language by being unable to form a valid AST from the given tokens according to the grammar of the parser.
   An AST can be made this way and when read from the bottom up represents our full equation as it should be performed (multiply share x close first) then add that resulting value to cash.
$        (+)     
        /   \
    cash    (×)
           /   \
      shares   close

# Static Analysis: Individualism of a language starts to appear.. Static analysis takes the finished, well-formed AST and studies it for meaning, catching errors the parser couldn't without running any of the code (static) and makes sure that the AST actually makes sense
 $ if our AST produced: (×)  
                       /   \
                  shares   close
   According to the parser this is valid we are just multiplying two variables, but the SA will actually dive into those variables and determine if they even exist, if they are 64 Bit ints, or 32 Bit ints (IN STATICALLY TYPED LANGUAGES), according to where they are stored.

  *Static Analysis has several parts and multiple things to check before our code can be runnable.*
  1. Binding / Name Resolution: For any identifiers find where it is defined and wire those two pieces together this develops the program's scope determining where in the source code an identifier can be used.
     Think of where global variables are initialized in Java (above a method signature, beneath the class name)

  2. Type Checking: In statically typed languages (Java, C, C++, etc) within Static Analysis is where variables will be checked for type correctness.

# The various findings of static analysis must be stored somewhere: right back as attributes on the syntax tree itself, in a lookup table off to the side (symbol tables), or a whole new data structure that is made specifically to handle these findings (intermediate representation) next..
