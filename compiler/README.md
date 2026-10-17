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
.pvm module sources
    -> module-wide declaration collection and symbol resolution
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

Named action-result references (`युज् + ल्युट् + ङस् फल + अम्`) lower to
explicit history loads and per-occurrence operand slots, not substitutions of
`LastResult`. Single and multiple references support arithmetic, procedure
arguments, conditions, and loop predicates. Ordering qualifiers retain their
case agreement; chronological ordinals remain fixed while latest references
reload when a loop condition is evaluated. Unselected branches do not load
history, and missing reached antecedents fail explicitly.

Explicitly prefixed or sanādi-derived action nouns use structural history
identity (Dhātupāṭha entry, ordered prefixes, ordered sanādi), retained in compiler
IR. The JVM indexes the same completed event under both structural and root-family
identities; ordering is applied after identity filtering. Unqualified action nouns
retain root-family lookup. Missing qualified matches cannot fall back to unrelated
same-root events.

Kāraka-history queries such as `युज् + ल्युट् + ङस् कर्मन् + अम्` are a
different feature: compiled display and assignment preserve remembered
participants and their order. Assignment expands explicit history-group wrappers,
packs several participants into a Suchi, and retains a single collection-valued
participant as that collection. The assignment's own history records the original
expanded inputs, not its packed result. Numeric-fold actions also consume
explicitly expanded history groups through numeric-fold IR, without flattening
collection-valued participants. Their operator and minimum operand count come
from the resolved action contract. Subtraction/division retain coordinated input
order; this does not introduce general ablative/instrumental arithmetic lowering.
Other non-display participant-history
operations still need valency-aware IR lowering. These references are not general
सर्वनाम resolution.

Bare accusative history groups for numeric/counting verbs use `CallParticipantGroup`
IR: canonical verb features and the group survive until the shared operation
resolver sees actual runtime participants. This preserves गण's multiplication
versus counting distinction, records successful action inputs, and does not
reparse source or re-enter the interpreter. It is an action-runtime boundary;
other verb families and general kāraka roles remain pending.
Mixed-history expressions also use this boundary when numeric placeholders cannot
prove a unique verb meaning. Group packing preserves actual collection-valued
participants, and resolved agent bindings are retained in completed action history.
Coordinated groups and explicit destinations share this path. The selected
operation's result-binding contract controls destination writes through the
scope-aware runtime, after successful execution and input-history capture.
Ordinary named operands use the same deferred selection when payload types are
unknown; proven numeric AST literals retain primitive lowering. Boundary reports
include these calls as `dynamic:<canonical dhātu>` rather than omitting them.
Typed operands retain explicit reference identity: value lowering reads a current
binding when available and otherwise uses the captured fallback. Literal text
without a reference identity never becomes a collection/position reference merely
because a variable has the same spelling.
Generic-call expression serialization preserves these referents, explicit Pada
annotations, and collection-member selections rather than reconstructing meaning
from value spellings or dropping grammatical metadata.
Ordinary Pada literals are not implicitly rebound by their spelling. Copular
planning retains subject/standard reference identity and nominal fallbacks
explicitly, so generic equality calls do not need runtime name guessing.

- **`BytecodeCompiler`** is the public API for compiling single-source programs
  or source-file modules, loading generated classes, and writing `.class` files.
- **`PaniniModuleDescriptor`** defines source membership, entry points, and
  separately compiled dependencies. `PaniniModuleAnalyzer` collects declarations,
  resolves domains/inheritance, infers legacy implicit parameters from parsed
  morphology, checks duplicates, and assigns stable JVM method identities.
- **`.pvmmeta` artifacts** contain versioned exported procedures, typed
  signatures, domains, inheritance, and record schemas. Dependency calls resolve
  from this metadata and compile to direct JVM calls; source files are not needed
  when consuming an artifact.
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
back-edges and state operations. Loop breaks use `RequestBreak`, `ConsumeBreak`,
and `ReturnIfBreak`. Procedure result predications (`फल … अस्ति`, with singular
nominative role and value) lower their value to IR and use `RequestProcedureReturn`. Sequence and loop guards
propagate that request to the owning procedure's contract exit, without
publishing loop-completion values. `Return` ends the generated JVM procedure.
The result contract still applies, lexical foreach frames are unwound, and a
nested callee's return does not terminate its caller. Outside procedure scope,
the predication is not interpreted as a return request.

Named calls use explicit procedure instructions:

```text
argument values
    -> EnterFrame
    -> InvokeProcedure
    -> ExitFrame
```

Calls into separately compiled modules use the corresponding
`InvokeDependencyProcedure` instruction and the same runtime argument frame.
Generated exported methods are public static JVM methods; internal saṃjñās never
appear in module metadata.

## Module descriptors

The Gradle compiler treats `src/main/pvm` as one module. An optional
`src/main/pvm/panini.module` file controls its boundary:

```properties
name=calculator
sources=ganita.pvm,mukhya.pvm
entries=mukhya.pvm
dependencies=../libraries/ganita.pvmmeta
```

Without a descriptor, all `.pvm` files below the source directory are members;
files named `*mukhya*` are entry points when present, otherwise non-`*_lib`
files are entry points. Compilation emits `<Module>Program.class` and
`<module>.pvmmeta`.

Metadata format 3 preserves procedure precedence and per-type participant
ordinals across separately compiled libraries. Regenerate format-1/2 metadata
with the current compiler; it is rejected
because it cannot represent this dispatch contract.

`BytecodeCompiler.compileModule` accepts an in-memory descriptor, while
`compileModuleDirectory` performs descriptor discovery and writes both artifact
files. Metadata has an explicit format version and rejects incompatible input.

Generated programs also expose `execute(Map<String, SanskritValue>)`. The IR
verifier can seed named-value kinds from `CompilerProgram.initialValueKinds`, and
procedure/dependency result kinds propagate to `LastResult`.

Constants, named values, locals, `LastResult`, lists, records, fields (including
explicit `LoadFieldOrLopa` absent-field semantics),
comparisons, arithmetic, text rendering, and procedure arguments also have
explicit IR instructions. Consequently, the JVM backend consumes only compiler
IR and does not inspect the source AST or execution plans.

## Direct lowering

The following common operations currently avoid the generic action-runtime
boundary:

- numeric addition, subtraction, multiplication, division, remainder, minimum,
  exponentiation, average, doubling/scale, exact integer square root, and
  integer hypotenuse;
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

## Shared leaf-planning boundary

- `ResolvedLeafPlanner` lives in the shared execution module and exposes typed,
  resolved leaf plans to the compiler and other tooling. Symbolic operands,
  operation disambiguation, and fixed repetition use parsed padas, resolved
  grammatical features, alternative typed placeholders, and the shared program
  AST. The former compiler-local `DirectLeafPlanner` has been removed.

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

Generate a CSV inventory for all repository example modules with:

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
2. add richer field-level types to exported record schemas;
3. package `.class` and `.pvmmeta` pairs into a single distributable archive.
