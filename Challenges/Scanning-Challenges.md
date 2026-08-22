## Question 1
__The lexical grammars of Python and Haskell are not regular. What does that mean, and why aren’t they?__

###Answer 1
Python and Haskell use indentation sensitive syntax meaning the interpreter needs to know the amount of white space before tokens and the amount of white space on the previous line to determine scope of functions/variables and understand which pieces belong where. This is not regular. A regular language is one that can be recognized by a finite automaton, a machine with a fixed, finite number of states and no memory. So regular grammar like in longu(lox) switch between states (in-number, in-string, in-expression,..) without the ability to count/remember. Something must store indentation level like a stack, if indentation increases you need to push a level and emit an INDENT token and likewise pop a level and emit an EDENT when indentation decreases until you match the indentation level of a previous level on the stack. Since a finite state machine doesn't have memory there can't be a structure like a stack, therefore regular grammar cannot support indentation-sensitive grammar.

##Question 2
__side from separating tokens—distinguishing print foo from printfoo—spaces aren’t used for much in most languages. However, in a couple of dark corners, a space does affect how code is parsed in CoffeeScript, Ruby, and the C preprocessor. Where and what effect does it have in each of those languages?__

###Answer 2
In Ruby and CoffeScript white spaces play a part in function calling. In Ruby spacing after a method call determines if you are calling a method with arguments or using a binary operator such as foo + bar vs. foo +bar
The former is addition between foo and bar while the later is calling the method foo with parameter +bar: foo(+bar). 
CoffeScript is similar, if there are spaces between a function identifier and its arguments it will pass everything within the parentheses as a single (first) argument. Ex: foo (bar, baz) vs. foo(bar,baz) if your function expects two arguments the first example will not compile because it will pass "bar,baz" as a singular argument and will tell you there was an unexpected comma, while the second is a regular two arg function call. 
The C-preprocessor case is interesting and relates to how macros work. In C you can define both function and object like macros. EX: #define FOO(x) ((x) + 1) vs. #define FOO (x) ((x) + 1) The former macro is function-like its a macro called FOO which takes a single parameter 'x' and performs the body ((x) + 1), but the second macro is object-like with the body: (x) ((x) + 1), so a space between macro name and parentheses is the difference between a function macro with arguments and an object macro with the parentheses (x) as part of the body.

##Question 3
__Our scanner here, like most, discards comments and whitespace since those aren’t needed by the parser. Why might you want to write a scanner that does not discard those? What would it be useful for?__

###Answer 3
A normal interpreter discards comments and whitespace because the parser only needs the executable structure. But any tool that works with the source as text needs them preserved. Code formatters (prettier, gofmt) must keep comments and whitespace because their job is to reproduce your program with cleaned-up style, deleting comments would be unacceptable. IDEs need them for syntax highlighting (coloring comments) and for mapping tokens back to exact source positions. Documentation generators (Javadoc) treat specially-formatted comments as their actual input. Transpilers preserve comments so translated code keeps its documentation. In short, the moment a tool has to display, reformat, document, or translate source rather than just run it, the scanner has to keep what a plain interpreter throws away. So anytime you might need to fully recreate the source code or preserve everything that existed in it you would not want to disregard whitespace and comments.


