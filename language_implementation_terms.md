# Language Implementation Terms

A reference for the words that describe how a language runs: *interpreter*,
*compiler*, *JIT*, *AOT*, and the supporting cast.

> [!IMPORTANT]
> **These terms describe an implementation, not a language.**
> "Is Python compiled or interpreted?" is the wrong question. CPython (the usual
> Python) compiles to bytecode and then interprets it. PyPy JIT-compiles the same
> language. Same language, different strategies. Always attach the label to an
> *implementation* (CPython, V8, HotSpot), never to the language name.

> [!NOTE]
> A second idea: real systems **combine** these. A language is often compiled to
> bytecode, then that bytecode is interpreted, and the hot parts are
> JIT-compiled. These are building blocks, not rival camps.

---

## Table of contents

- [Compiler](#compiler)
- [Interpreter](#interpreter)
- [AOT (Ahead-of-Time compilation)](#aot-ahead-of-time-compilation)
- [JIT (Just-in-Time compilation)](#jit-just-in-time-compilation)
- [Supporting terms](#supporting-terms)
- [How real languages combine these](#how-real-languages-combine-these)
- [Quick comparison table](#quick-comparison-table)
- [Where Crafting Interpreters fits](#where-crafting-interpreters-fits)

---

## Compiler

**Definition:** a program that translates code from one form into another, usually
from a higher-level form to a lower-level one, *before* that code runs.

**Plain version:** a translator. It takes your code and produces an equivalent
version in another form (machine code, bytecode, or even another language). The
key part is that it translates and hands off. It does not run the result itself.

> [!TIP]
> "Compiler" is the umbrella word. AOT, JIT, and transpilers are all *kinds* of
> compilers. What varies is *when* they run and *what* they target.

**Examples:** `javac` (Java source to Java bytecode), `gcc` and `clang` (C to
machine code), `rustc` (Rust to machine code).

---

## Interpreter

**Definition:** a program that executes code directly, carrying out its
instructions itself, without producing a separate machine-code output to run
later.

**Plain version:** instead of translating your code and handing it off, it reads
each instruction and *does the thing* right then, using its own code.

Two common kinds, which map straight onto the book:

- **Tree-walking interpreter:** walks the AST and executes each node as it visits
  it. Simplest to build, slowest to run. This is **jlox**, the first interpreter
  in *Crafting Interpreters*.
- **Bytecode interpreter:** first compiles source to compact bytecode, then loops
  over that bytecode executing each instruction. Faster than tree-walking. This
  is **clox**, the second interpreter in the book, and it is how CPython works.

**Examples:** shell scripts (bash), CPython (bytecode interpreter), the reference
Ruby interpreter.

> [!NOTE]
> The bytecode kind already blends compiler and interpreter: a compile step to
> bytecode, then an interpret step over that bytecode.

---

## AOT (Ahead-of-Time compilation)

**Definition:** compiling source code all the way to native machine code *before*
the program is run, producing a standalone binary that the CPU executes directly.

**Plain version:** do all the translating at build time on your machine, ship the
finished executable, and at runtime there is no compiling left to do.

| | |
|:--|:--|
| **Strengths** | fast startup, predictable performance, no compiler needed at runtime, small footprint |
| **Weaknesses** | cannot use runtime behavior to optimize, and binaries are platform-specific |

**Examples:** C, C++, Rust, Go, Swift. Compile once, run the binary on any
compatible machine.

---

## JIT (Just-in-Time compilation)

**Definition:** compiling code to native machine code *at runtime*, while the
program is already running, typically translating bytecode or IR into machine
code for the parts that run often (the "hot" code).

**Plain version:** start out interpreting, watch which code runs a lot, then
compile those hot parts to fast machine code on the fly so they run at native
speed for the rest of the session.

| | |
|:--|:--|
| **Strengths** | near-native speed on hot code, and it can optimize based on what actually happens this run (something AOT cannot see) |
| **Weaknesses** | slow warm-up, carries a full compiler in the runtime, needs writable-executable memory (blocked on platforms like iOS), unpredictable compilation pauses |

**Examples:** Java HotSpot JVM, JavaScript V8 (Chrome and Node.js), the .NET CLR,
PyPy (Python), LuaJIT (Lua).

> [!TIP]
> A JIT only wins when the program runs *long enough and hot enough* for
> compilation to pay for itself. Every "do not JIT" case is a situation where
> that condition fails.

---

## Supporting terms

<details>
<summary><strong>Bytecode</strong> (click to expand)</summary>

**Definition:** a compact, low-level instruction set designed to be executed by a
virtual machine rather than by real hardware.

**Plain version:** a halfway form between source and machine code. Not
human-friendly, not CPU-native, but easy for a VM to run quickly, and the same
across platforms.

**Examples:** Java bytecode (`.class`), Python bytecode (`.pyc`), .NET CIL. In the
book, clox's compiled chunks are bytecode.

</details>

<details>
<summary><strong>Virtual Machine (VM)</strong> (click to expand)</summary>

**Definition:** a program that executes bytecode, acting as a simulated processor
in software.

**Plain version:** the thing that runs bytecode. It plays the role a physical CPU
plays for machine code.

**Examples:** the JVM, the CPython VM, the .NET CLR. clox is a small VM.

> [!WARNING]
> This means a *language* VM (a bytecode executor). It is a different thing from a
> *system* VM like VMware or VirtualBox, which simulates a whole computer. Same
> words, different concept.

</details>

<details>
<summary><strong>IR (Intermediate Representation)</strong> (click to expand)</summary>

**Definition:** an internal form of code a compiler uses between the source and
the final target, designed for analysis and optimization.

**Plain version:** a working format the compiler transforms and optimizes before
emitting the final output. Usually not something you write or see.

**Examples:** LLVM IR (used by clang, rustc, and Swift). Bytecode is sometimes
used as an IR as well.

</details>

<details>
<summary><strong>Transpiler (source-to-source compiler)</strong> (click to expand)</summary>

**Definition:** a compiler whose output is source code in another high-level
language, rather than bytecode or machine code.

**Plain version:** translates one readable language into another readable
language.

**Examples:** TypeScript to JavaScript, Babel (new JavaScript to older
JavaScript), CoffeeScript to JavaScript.

</details>

---

## How real languages combine these

Almost nothing is purely one thing:

- **Java:** `javac` compiles source to bytecode (a compiler). The JVM then
  interprets that bytecode and JIT-compiles the hot parts (interpreter plus JIT).
- **Python (CPython):** compiles source to bytecode, then interprets it. Compiler
  plus bytecode interpreter.
- **Python (PyPy):** the same language, JIT-compiled. The clearest proof that the
  term attaches to the implementation, not the language.
- **JavaScript (V8):** parses to bytecode, interprets it, JIT-compiles hot code.
- **C, Rust, Go:** AOT compile straight to a native binary. No interpreter, no VM,
  no runtime compiler.

---

## Quick comparison table

| Approach | When translation happens | Produces | Runtime speed | Startup | Example |
|:---------|:-------------------------|:---------|:--------------|:--------|:--------|
| Interpreter (tree-walk) | while running, per AST node | nothing saved | slow | instant | jlox, bash |
| Interpreter (bytecode) | compile to bytecode first, then run | bytecode | medium | fast | CPython, clox |
| AOT compiler | before running | native binary | fast | instant | C, Rust, Go |
| JIT compiler | while running, on hot code | native code in memory | fast after warm-up | slow warm-up | V8, HotSpot |

---

## Where Crafting Interpreters fits

- **jlox** (part 2): a tree-walking interpreter. Source to AST, then walk the AST
  and execute each node.
- **clox** (part 3): a bytecode interpreter and VM. Source to bytecode, then a VM
  loop executes it. Same shape as CPython.

> [!NOTE]
> The book deliberately stops short of JIT and AOT, because those add a large
> amount of complexity for the same conceptual payoff. Knowing where they would
> slot in (compile the bytecode to machine code, either ahead of time or on the
> fly) is enough to understand them.
