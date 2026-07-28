# Module `:compiler`

Native JVM Bytecode Compiler for `.pvm` PaniniVM script files.

## Named local values

PVM assignment and reference syntax remains a segmented Sanskrit sentence.
With `दा`, the `KARMAN` value is bound to the literal name supplied as
`SAMPRADANA`:

```text
एक + अम् आरम्भ + ङे दा + लोट् + सिप् ।
आरम्भ + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
```

The first sentence binds the typed value `एक` to `आरम्भ`. The second sentence
resolves `आरम्भ` as that local value and produces three. Bindings persist in a
named VM session and are emitted as ordinary entries by compiled programs.

## Overview

The `:compiler` module compiles multi-clause segmented Sanskrit `.pvm` script files directly into native JVM `.class` bytecode:
- **`BytecodeCompiler`**: Generates JVM bytecode carrying stack constants, AST invocations, and runtime continuations.
- **`PaniniRuntime`**: Host execution environment for compiled bytecode execution.
- **`PvmUktiSadhaka`**: Evaluates parsed AST nodes to perform full `rūpa-siddhi` (रूपसिद्धि) on segmented `.pvm` script lines.
