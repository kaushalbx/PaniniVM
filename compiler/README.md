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

The `:compiler` module compiles multi-clause segmented Sanskrit `.pvm` scripts
into JVM `.class` bytecode. Compilation uses one backend-neutral pipeline:

```text
.pvm source
    -> grammatical parsing and action resolution
    -> CompilerProgram IR
    -> IR verification
    -> JVM bytecode emission
    -> generated-bytecode verification
```

The compiler does not re-enter the PaniniVM interpreter. Operations that have
not yet received primitive IR lowering cross one explicit action-runtime
boundary instead.

## Architecture

- **`BytecodeCompiler`** is the public API for compiling source strings and
  files, loading generated classes, and writing `.class` files.
- **`CompilerFrontend`** parses a complete source unit and lowers it to a
  `CompilerProgram`. It does not emit ASM instructions.
- **`CompilerProgram`** contains an entry point and independently emitted named
  procedures.
- **`CompilerInstruction`** is the backend-neutral IR for values, control flow,
  procedure frames, action calls, and returns.
- **`CompilerProgramVerifier`** and **`CompilerIrVerifier`** validate procedure
  references, frames, labels, locals, loop state, and operand-stack shape before
  emission.
- **`CompilerProgramJvmEmitter`** and **`CompilerIrJvmEmitter`** are the
  standalone IR-to-JVM backend.
- **`GeneratedBytecodeVerifier`** applies ASM verification to the resulting
  class.
- **`CompiledProgramRuntime.executeDirectValue`** is the single generic runtime
  boundary for domain actions that do not yet have direct IR lowering.

## Compiler IR

Control flow is represented explicitly with `Label`, `Branch`, and `Jump`.
Fixed repetition and bounded or unbounded loops are lowered to ordinary IR
back-edges and state operations. Breaks and returns use `RequestBreak`,
`ConsumeBreak`, `ReturnIfBreak`, and `Return`.

Named calls use explicit procedure instructions:

```text
argument values
    -> EnterFrame
    -> InvokeProcedure
    -> ExitFrame
```

Constants, named values, locals, `LastResult`, lists, records, fields,
comparisons, arithmetic, text rendering, and procedure arguments also have
explicit IR instructions. Consequently, the JVM backend consumes only compiler
IR and does not inspect the source AST or execution plans.

## Direct lowering

The following common operations currently avoid the generic action-runtime
boundary:

- numeric addition, subtraction, multiplication, division, remainder, minimum,
  exponentiation, and average;
- numeric comparisons, equality, truth conversion, and evenness checks;
- assignment, named loads, local values, procedure arguments, and
  `LastResult`;
- list construction, length, reversal, concatenation, indexing, containment,
  append, pop, and slicing;
- structured records and field access;
- text rendering and common display-value construction;
- conditionals, fixed repetition, bounded and unbounded loops, breaks, returns,
  and named procedure calls.

Other resolved domain actions remain valid compiler leaves. They are represented
by `Call` IR and dispatched through `executeDirectValue`; this is an action
runtime call, not interpreter re-entry. `CompilerRuntimeBoundaryReport` reports
these operations so that direct-lowering work can be prioritized and measured.

## Known compatibility paths

- The shared analyzed AST does not always retain the `Repeat` wrapper for a
  frequency invocation. `CompilerFrontend` therefore contains one isolated
  `कृत्वः` source compatibility parser. It can be removed when repetition is
  preserved by the parser/execution AST handoff.
- `DirectLeafPlanner` still contains source-pattern compatibility logic for
  several leaf forms. New lowering should prefer resolved grammatical bindings
  and typed values, with the long-term goal of reducing or deleting those
  heuristics.

## Verification

Run the compiler and CLI integration suites with:

```shell
./gradlew :compiler:test :cli:test
```

The tests cover IR structure and stack validation, nested control flow,
recursion, procedure frames, breaks, bounded and unbounded loops, structured
values, invalid programs, runtime-boundary counts, generated-bytecode
verification, and host-budget exhaustion.

The verifier currently tracks exact kinds for constants and primitive results.
Loads from named storage, locals, arguments, and `LastResult` are conservatively
typed as unknown. Propagating stored value kinds through control-flow joins is
the next verifier-strengthening step.

## Benchmark

The repository includes a small reproducible comparison of interpreter
execution, frontend lowering, JVM emission/class loading, and generated-code
execution:

```shell
./gradlew :compiler:benchmarkCompiler -Piterations=1000 -Pwarmups=100
```

This harness is intended for development comparisons. Use JMH and a controlled
runtime environment for publication-grade measurements. The benchmark also
prints the runtime-boundary operations for each compiled case.

## Remaining work

The unified IR and standalone JVM backend are in place. The highest-value next
steps are:

1. propagate stored value kinds through verifier dataflow and control-flow
   joins;
2. preserve repetition in the shared AST and remove the compiler's frequency
   compatibility parser;
3. inventory remaining `executeDirectValue` calls and directly lower the most
   frequent domain operations;
4. replace remaining `DirectLeafPlanner` source heuristics with resolved
   grammatical data;
5. expand machine-readable benchmarks across arithmetic, collections, branches,
   loops, and procedure calls.
