# PaniniVM

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

**PaniniVM** is a natural programming language in which morphologically
segmented Sanskrit is executable source code. Its Kotlin runtime combines a
Pāṇinian derivation engine (*Aṣṭādhyāyī Rūpa-Siddhi*) with typed semantic
execution: grammatical relationships determine data flow, verbal roots select
operations, and derived identities carry meaning through a program.

Implemented sūtras carry typed metadata, executable eligibility, rule
dependencies, blocking rules, and state transitions. Every derivation retains
an ordered, auditable trace of applied sūtras, *Adhikāras*, *Paribhāṣās*,
*Niṣedhas*, conflicts, and alternatives.

---

## Sanskrit as a Natural Programming Language

Pāṇini’s Aṣṭādhyāyī is the earliest formal, rule-based computational system in human history. PaniniVM leverages this grammatical machinery to treat Sanskrit not merely as text, but as a **fully executable natural programming language**. By writing structured, morphologically segmented Sanskrit sentences (*Uktis*), you write program specifications directly in natural language:

- **Meaning comes from grammar**: Case markings (*Vibhaktis*) establish
  computational relationships through *Kārakas*. Accusative forms identify
  operands, dative forms identify destinations, and ablative forms can identify
  sources or exclusions.
- **Words remain morphologically visible**: Programs are written as fully
  segmented `Prakṛti + Pratyaya` expressions. Each segment contributes to the
  grammatical analysis instead of being treated as punctuation-like syntax.
- **Dhātus express operations**: A verbal root and its affixes select an action;
  nominal expressions supply its typed participants. Pipelines pass the typed
  result of one sentence directly into the next.
- **Natural control and reuse**: Repetition, conditions, ranges, collections,
  and reusable Sanskrit *kriyā* definitions are language constructs rather
  than host-language API calls.
- **Auditable execution**: Source is parsed into a grammatical AST, resolved
  through Pāṇinian identities, planned, and executed with a traceable semantic
  path. Capability-sensitive work can pause for approval and resume safely.

For example, this segmented sentence adds two numbers and sends its result
directly to `योग`:

```pvm
द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः योग + ङे दा + लोट् + सिप् ।
```

The source is Sanskrit, while its execution model remains statically
inspectable: `द्वि + अम्` and `त्रि + अम्` are operands, `युज्` supplies
the operation identity, and `योग + ङे` is the result destination.

---

## What is implemented

- Fully segmented Sanskrit `.pvm` source and a canonical grammatical AST.
- Typed values, direct result pipelines, conditions, bounded and
  condition-controlled loops, collections, ranges, and validated input.
- Reusable Sanskrit kriyā definitions with typed and named parameters,
  structured results, overloads, visibility, and multi-file composition.
- 893 executable Aṣṭādhyāyī sūtras from the 3,959-sūtra catalog, spanning
  derivation, Sandhi, Samāsa, Kāraka, Vibhakti, Subanta, and Tiṅanta, alongside
  Uṇādi and Liṅgānuśāsanam subsystems.
- A direct CLI, JVM bytecode compiler, and IntelliJ IDEA/Android Studio plugin.

See [Architecture and implementation coverage](ARCHITECTURE.md) for the module
map, execution pipeline, grammatical subsystems, and current coverage details.

---

## Input Boundary

PaniniVM accepts annotated morphological notation, not ordinary surface Sanskrit. Declinable and conjugable inputs expose their grammatical components (e.g. `एक + अम्` or `युज् + णिच् + लोट् + सिप्`).

The execution path follows a single direction:

```text
segmented input → vyākaraṇa AST → binding → operation resolution → planning → runtime
```

## Prerequisites

- JDK 25, as configured by the Gradle toolchain. Generated JVM bytecode targets
  Java 21.
- A UTF-8 terminal capable of displaying Devanagari.
- The repository's Gradle wrapper; a separate Gradle installation is not
  required.

Build the direct CLI launcher once from the repository root:

```sh
./gradlew :cli:installDist
```

Interactive `.pvm` programs should use the generated launcher rather than
Gradle's `:cli:run` task, because Gradle's console proxy may buffer input.

## Write your first `.pvm` program

The [number-guessing game](projects/number-guessing-game/number_guessing_game.pvm)
chooses a number from one through ten and gives the player five attempts. Its
complete source is segmented Sanskrit; comments remain in English.

```pvm
# One range is reused by random choice, input validation, and output.
एक + ङसिँ दशन् + ङि इति सीमा + सुँ ।

# Define one reusable guessing attempt.
प्रयत्न + ल्युट् + सुँ ।
निवेश + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
यदि रहस्य + अम् ग्रह् + घञ् + ङस् फल + टा अस् + लोट् + सिप् तर्हि विजयः
अन्यथा यदि ग्रह् + घञ् + ङस् फल + अम् रहस्य + अम् च नि + विद् + लोट् + सिप्
तर्हि लघु अन्यथा गुरु ततः मुद्र् + णिच् + लोट् + सिप् ॥

# Choose the secret and store the direct pipeline result.
दिव् + णिच् + लोट् + सिप् ततः रहस्य + ङे दा + लोट् + सिप् ।

# Print a dynamically rendered instruction.
सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।

# Repeat until success or five attempts are exhausted.
पञ्चन् + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप्
अन्यथा प्रयत्नाः + अम् समाप्ताः + अम् च मुद्र् + णिच् + लोट् + सिप् ।

# Reveal the secret.
रहस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

### How the grammar becomes a program

| Segmented form | Programming role |
|---|---|
| `एक + ङसिँ दशन् + ङि इति सीमा + सुँ` | Declares the inclusive range used by random choice and validation. |
| `निवेश + अम्` | Marks the input value as *karman* through the accusative ending. |
| `सङ्ख्या + ङे` | Supplies the numeric input type through the dative ending. |
| `ग्रह् + णिच् + लोट् + सिप्` | Forms the executable input command from a dhātu and verbal affixes. |
| `ततः रहस्य + ङे दा ...` | Pipes the preceding typed result directly into `रहस्य`. |
| `यदि ... तर्हि ... अन्यथा` | Selects success, low, or high feedback conditionally. |
| `पञ्चन् + कृत्वः` | Bounds repetition to five attempts. |
| `यावत् फल + सुँ न तावत्` | Continues while the latest comparison result is false. |
| `अन्यथा` after the loop | Runs the exhaustion branch only if all attempts are consumed. |

Run it with the installed launcher:

```sh
./cli/build/install/cli/bin/cli --eval projects/number-guessing-game/number_guessing_game.pvm
```

One possible session is shown below. The secret is random, so feedback and the
revealed number will vary.

```text
[PaniniVM CLI] Executing file: number_guessing_game.pvm
एकतः दशन्पर्यन्तं सङ्ख्याम् अनुमिनु
Enter value for निवेश (number):
लघु
Enter value for निवेश (number):
लघु
Enter value for निवेश (number):
लघु
Enter value for निवेश (number):
लघु
Enter value for निवेश (number):
लघु
प्रयत्नाः समाप्ताः
षट्
```

This session entered `1`, `2`, `3`, `4`, and `5`; the generated secret was
`6`. Input outside the declared range is rejected without consuming an attempt.

## Reusable Typed Sanskrit Kriyās

For a complete language tutorial, see
[`docs/pvm-language-guide.md`](docs/pvm-language-guide.md).
For upcoming syntax and tooling milestones, see the
[`PVM language enhancement plan`](docs/pvm-language-enhancement-plan.md).

A `.pvm` program can declare a reusable operation using a nominal
`saṃjñā-kriyā` header. Signature declarations are grammatical sentences inside
the block and are not executed as body actions.

```pvm
योजन + ल्युट् + सुँ ।
वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
सङ्ख्या + सुँ इति परिणाम + सुँ ।
वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
```

The operation accepts the original positional call form:

```pvm
द्वि + अम् त्रि + अम् च योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

It also accepts named arguments. A parameter name uses ṣaṣṭhī and its value
immediately follows in dvitīyā; named arguments may appear in any order.

```pvm
दक्षिण + ङस् त्रि + अम् वाम + ङस् द्वि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

Supported signature types are `सङ्ख्या`, `शब्द`, and `सूची`. A result may also
name a declared `…परिणाम + मतुप्` schema. Typed values retain their semantic
type when one saṃjñā-kriyā feeds another pipeline stage. The runtime and IDEA
plugin diagnose duplicate declarations, missing or unknown named arguments,
arity/type mismatches, incompatible pipeline stages, and invalid result schemas.

Function bodies have isolated child scopes and may call other public or
file-private `अन्तरङ्ग` definitions. Domain dispatch through `अधिकार`,
`अपवाद`/`नित्य`/`अन्तरङ्ग` precedence, typed overload selection, and `क्त`
memoization continue to apply. Optional/default parameters and explicit early
return syntax are planned conveniences; they are not required for reusable
first-class definitions.

---

## Running PaniniVM

The command-line module evaluates and compiles `.pvm` programs, renders
readable Sanskrit companions, and provides an interactive REPL. Build the CLI
distribution once, then use its launcher directly. This is required for
interactive programs because Gradle's console proxy may not forward terminal
input correctly.

```sh
./gradlew :cli:installDist
```

Run a segmented Sanskrit program, including one that reads interactive input:

```sh
./cli/build/install/cli/bin/cli --eval examples/algorithms/fibonacci_array.pvm
./cli/build/install/cli/bin/cli --eval examples/io/io_demo.pvm
./cli/build/install/cli/bin/cli --eval projects/number-guessing-game/number_guessing_game.pvm
```

Running the launcher without arguments starts the PaniniVM REPL:

```sh
./cli/build/install/cli/bin/cli
```

Generate readable Sanskrit companions from segmented source, or compile a
`.pvm` program to a JVM class:

```sh
./cli/build/install/cli/bin/cli --render-readable examples/algorithms/fibonacci_array.pvm
./cli/build/install/cli/bin/cli --compile examples/arithmetic/addition.pvm SanskritAddition
```

Canonical sūtra programs can be emitted, checked, and executed independently:

```sh
./cli/build/install/cli/bin/cli --emit-grantha examples/arithmetic/addition.pvm addition.sutra
./cli/build/install/cli/bin/cli --check-grantha addition.sutra
./cli/build/install/cli/bin/cli --grantha addition.sutra
```

See the complete [CLI command reference](cli/README.md) for Windows launcher
commands, typed interactive input, derivation tools, and every supported flag.

For `.pvm` editing, diagnostics, navigation, and gutter execution in IntelliJ
IDEA or Android Studio, see the detailed
[IDEA plugin installation guide](idea-plugin/README.md#install-the-plugin-from-disk).

---

## Programmatic API

```kotlin
import dev.panini.execution.PaniniVM
import dev.panini.execution.ExecutionResult
import dev.panini.unadipatha.UnadiDerivationEngine
import dev.panini.unadipatha.analysis.UnadiAnalyzer
import java.io.File

// Uṇādi Etymological Stem Analysis
val stemAnalysis = UnadiAnalyzer.analyzeStem("पितृ")
println(stemAnalysis.classification) // Output: YAUGIKA_PRATIPADIKA

// Uṇādi Derivation Tracing
val derivationResult = UnadiDerivationEngine.derive("कृ", "उण्")
println(derivationResult.final.surface) // Output: कारु

// Single utterance evaluation via VM
val vm = PaniniVM()
val result = vm.eval("दशन् + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
if (result is ExecutionResult.Success) {
    println(result.value) // Output: द्वादश
}
```

---

## Test Suite Verification

Run all automated unit and integration tests across the Gradle workspace:

```sh
./gradlew check --no-daemon
```

---

## Contributing and license

PaniniVM is copyright © 2026 Kaushal Kumar Singh and is licensed under the
[Apache License, Version 2.0](LICENSE). Contributions are welcome under the same
license; see [CONTRIBUTING.md](CONTRIBUTING.md) for testing, grammatical-source,
and attribution guidance. Third-party data acknowledgements are recorded in
[NOTICE](NOTICE).

---

## Text Encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the executable specification, so editors and terminals must preserve UTF-8 when changing Devanagari text.
