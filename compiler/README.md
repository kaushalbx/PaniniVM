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
  exponentiation, average, doubling/scale, and exact integer square root;
- numeric comparisons, equality, truth conversion, and evenness checks;
- assignment, named loads, local values, procedure arguments, and
  `LastResult`;
- list construction, length, reversal, concatenation, indexing, containment,
  append, pop, slicing, one-level flattening, and coordinated-value counting;
- structured records and field access;
- text rendering and common display-value construction;
- conditionals, fixed repetition, bounded and unbounded loops, breaks, returns,
  and named procedure calls.

Other resolved domain actions remain valid compiler leaves. They are represented
by `Call` IR and dispatched through `executeDirectValue`; this is an action
runtime call, not interpreter re-entry. `CompilerRuntimeBoundaryReport` reports
these operations so that direct-lowering work can be prioritized and measured.

## Remaining adapter boundary

- `DirectLeafPlanner` remains the adapter from grammatical execution planning to
  leaf IR. Symbolic operands, operation disambiguation, and fixed repetition now
  use parsed padas, resolved grammatical features, and the shared program AST
  rather than compiler-side source matching. Its remaining role can shrink as
  the shared planner exposes a compiler-oriented resolved-leaf API.

## Verification

Run the compiler and CLI integration suites with:

```shell
./gradlew :compiler:test :cli:test
```

The tests cover IR structure and stack validation, nested control flow,
recursion, procedure frames, breaks, bounded and unbounded loops, structured
values, invalid programs, runtime-boundary counts, generated-bytecode
verification, and host-budget exhaustion.

The verifier propagates value kinds through named storage, locals, `LastResult`,
and control-flow joins. External values and procedure arguments remain
conservatively typed as unknown unless a named procedure signature supplies a
kind. Procedure IR records parameter and return kinds, and call frames are
checked against those contracts. Static operand checks cover arithmetic,
cardinalization, branches, and collection operations.

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

Generate a CSV inventory for all compilable repository examples with:

```shell
./gradlew :compiler:inventoryCompilerBoundaries
```

Use `-PexamplesDir=/path/to/examples` to inventory another example tree. Files
outside the compiler's current language subset are reported as `unsupported`
with a stable category and source-bearing diagnostic instead of aborting the
inventory.

For measurements across isolated JVM processes, use:

```shell
./gradlew :compiler:macrobenchmarkCompiler \
  -Pforks=5 -Piterations=1000 -Pwarmups=100
```

Each fork uses a fresh JVM and emits the same CSV schema with an explicit fork
number. This is the preferred repository macrobenchmark; JMH remains appropriate
for instruction-level publication measurements.

## Remaining work

The unified IR and standalone JVM backend are in place. The highest-value next
steps are:

1. lower additional deterministic domain operations identified by the boundary
   inventory while retaining random, I/O, resource, and linguistic actions as
   explicit runtime boundaries;
2. expose the resolved-leaf planning implementation from the shared execution
   planner so the compiler-local adapter can be removed completely;
3. support the remaining inventory categories: complex nominal result bindings,
   cross-file procedure resolution, and structured pipelines;
4. extend value-kind inference to externally supplied initial state.
