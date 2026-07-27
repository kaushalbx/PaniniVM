# Module `:compiler`

Native JVM Bytecode Compiler for `.pvm` PaniniVM script files.

## Overview

The `:compiler` module compiles multi-clause segmented Sanskrit `.pvm` script files directly into native JVM `.class` bytecode:
- **`BytecodeCompiler`**: Generates JVM bytecode carrying stack constants, AST invocations, and runtime continuations.
- **`PaniniRuntime`**: Host execution environment for compiled bytecode execution.
- **`PvmUktiSadhaka`**: Evaluates parsed AST nodes to perform full `rūpa-siddhi` (रूपसिद्धि) on segmented `.pvm` script lines.
