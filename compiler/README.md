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

## Segmented loops

A `यावत्` condition can govern a multi-sentence body. The first body sentence
starts with `तावत्`; subsequent sentences start with `ततः`:

```text
यावत् गणना + अम् शून्य + अम् च विद् + णिच् + लोट् + सिप्
तावत् पूर्व + अम् वर्तमान + औट् च युज् + णिच् + लोट् + सिप्
ततः फल + अम् अग्रिम + ङे दा + लोट् + सिप्
ततः गणना + अम् एक + औट् च वि + युज् + णिच् + लोट् + सिप्
ततः फल + अम् गणना + ङे दा + लोट् + सिप् ।
वृत् + यङ् + लोट् + थास् ।
```

The condition must produce `सत्य`. Assignments in the body are visible to the
next iteration. A 100,000-iteration limit guards against accidental endless
loops. Compiled scripts emit JVM branches for the loop and reevaluate the
condition at runtime; the compiler does not expand or execute loop iterations.
The final `वृत् + यङ् + लोट्` sentence is mandatory: `वृत्` selects the existing
`LoopAction`, and `यङ्` marks क्रियासमभिहार, repeated execution.

## Overview

The `:compiler` module compiles multi-clause segmented Sanskrit `.pvm` script files directly into native JVM `.class` bytecode:
- **`BytecodeCompiler`**: Generates JVM bytecode carrying stack constants, AST invocations, and runtime continuations.
- **`PaniniRuntime`**: Host execution environment for compiled bytecode execution.
- **`PvmUktiSadhaka`**: Evaluates parsed AST nodes to perform full `rūpa-siddhi` (रूपसिद्धि) on segmented `.pvm` script lines.
