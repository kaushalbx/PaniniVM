# Module `:compiler`

Native JVM Bytecode Compiler for `.pvm` PaniniVM script files.

## Canonical sūtra programs

Segmented Sanskrit `.pvm` source can be emitted as an evaluator-free `.sutra`
grantha and processed by the public VM API:

```kotlin
val result = PaniniVM().evalGranthaFile(File("addition.sutra"))
```

`evalGrantha(source)` accepts the canonical text directly. Both entry points
decode, validate, compile, dependency-order, and execute the grantha through the
sūtra machine. Successful results retain their typed Sanskrit value; malformed
source and invalid granthas are returned as ordinary `ExecutionResult.Failure`
values.

`ProgramBlueprintGranthaEngine.validate(...)` performs the same source,
blueprint, and runtime checks without applying any sūtra effect. The CLI exposes
this safe tooling boundary as `--check-grantha file.sutra`.

During execution, the compiled grantha is automatically installed as the
current grantha in the immutable sūtra registry. It can therefore inspect its
own sūtras and resolve explicitly imported, exported sūtras without the host
manually registering the application. This self-reflection is preserved after
the grantha is encoded to canonical `.sutra` text and loaded again.

The sūtra machine is the sole execution architecture for ordinary
`PaniniVM.eval(...)` calls. The former parallel execution pipeline has been
retired.

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
