# Module `:cli`

Command-line interface runner for PaniniVM.

## Overview

Main entry point (`Main.kt`) supporting CLI flags:
- `--eval <file.pvm>`: Evaluates `.pvm` script files.
- `--compile <file.pvm> [ClassName]`: Compiles `.pvm` files directly to JVM bytecode.
- `--render-readable <path>`: Generates readable Sanskrit `.txt` companions.
- `--emit-grantha <file.pvm> <file.sutra>`: Emits a canonical sūtra program.
- `--check-grantha <file.sutra>`: Validates a canonical sūtra program.
- `--grantha <file.sutra>`: Executes a canonical sūtra program.
- `--unadi [lookup|pair|list]`: Uṇādipāṭha etymological inspection and reverse lookup.
- `--derive-unadi <dhatu> <pratyaya>`: Step-by-step Aṣṭādhyāyī derivation trace for Uṇādi stems.
- `--paradigm <pratipadika>`: Prints 21-slot nominal subanta paradigms.
- `--derive <pratipadika> <vibhakti> <vacana>`: Step-by-step nominal derivation trace.
- `--derive-karaka <pratipadika> <karaka> <vacana> <dhatu> <prayoga>`: Derives a nominal form from its semantic role.
- `--verb <dhatu> [lakara] [vacana]`: Step-by-step verbal tiṅanta derivation trace.
- `--sankhya <number> [cardinal|ordinal] [--variants]`: Derives Sanskrit numerals.
- `--sutra <number>`: Inspects one registered sūtra.
- `--coverage`: Prints registered sūtra count and role breakdowns.

## Installing and running the CLI

Build the CLI distribution once, then invoke the installed launcher directly:

```sh
./gradlew :cli:installDist
```

```powershell
.\gradlew.bat :cli:installDist
```

The direct launcher is the supported path for interactive programs. Gradle's
console proxy may buffer input or wait for EOF instead of forwarding terminal
lines as they are entered.

### Evaluating and compiling `.pvm`

```sh
./cli/build/install/cli/bin/cli --eval examples/algorithms/fibonacci_array.pvm
./cli/build/install/cli/bin/cli --eval projects/number-guessing-game/number_guessing_game.pvm
./cli/build/install/cli/bin/cli --compile examples/arithmetic/addition.pvm SanskritAddition
```

Execution and compilation do not rewrite files beside the source. Checked-in
`.txt` companions are readable Sanskrit renderings, not execution output. To
regenerate all companions or render one source or directory:

```sh
./gradlew renderExamples
./cli/build/install/cli/bin/cli --render-readable examples/arithmetic/addition.pvm
```

### Canonical sūtra programs

```sh
./cli/build/install/cli/bin/cli --emit-grantha examples/arithmetic/addition.pvm addition.sutra
./cli/build/install/cli/bin/cli --check-grantha addition.sutra
./cli/build/install/cli/bin/cli --grantha addition.sutra
```

The public API accepts the same evaluator-free source through
`PaniniVM.evalGrantha(...)` and `PaniniVM.evalGranthaFile(...)`.

### Uṇādipāṭha inspection

```sh
./cli/build/install/cli/bin/cli --unadi lookup पितृ
./cli/build/install/cli/bin/cli --unadi pair कृ कनिन्
./cli/build/install/cli/bin/cli --unadi list
./cli/build/install/cli/bin/cli --derive-unadi कृ उण्
./cli/build/install/cli/bin/cli --derive-unadi पा तृन्
```

### Derivations, numerals, and sūtra inspection

```sh
# Nominal derivations
./cli/build/install/cli/bin/cli --paradigm राम
./cli/build/install/cli/bin/cli --derive राम SASTHI BAHUVACANA
./cli/build/install/cli/bin/cli --derive-karaka राम KARMAN EKAVACANA भू KARTARI

# Verbal derivations
./cli/build/install/cli/bin/cli --verb भू
./cli/build/install/cli/bin/cli --verb भू LING EKAVACANA
./cli/build/install/cli/bin/cli --verb भू LOT बहुवचन

# Cardinal and ordinal numerals
./cli/build/install/cli/bin/cli --sankhya 23
./cli/build/install/cli/bin/cli --sankhya 6 ordinal
./cli/build/install/cli/bin/cli --sankhya 42 cardinal --variants

# Registry and implementation coverage
./cli/build/install/cli/bin/cli --coverage
./cli/build/install/cli/bin/cli --sutra 7.1.54
```

## Interactive execution

On Windows, use the generated `.bat` launcher:

```powershell
.\cli\build\install\cli\bin\cli.bat --eval cli/examples/interactive_addition.pvm
```

When the script executes a `ग्रह्` input operation, the CLI prints a prompt using
the कर्मन् variable name and waits for one line from standard input. For example,
`निवेश + अम् ग्रह् + णिच् + लोट् + सिप् ।` displays `Enter value for निवेश:`;
the entered text becomes the operation result and is available to subsequent
statements as `फल`.

To require numeric input, supply `सङ्ख्या` as sampradāna:

```text
प्रथम + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
```

The CLI then accepts ASCII digits such as `20` or Devanagari digits such as `२०`.
Invalid input is reported and the same prompt is repeated without advancing the
PVM program.

Other sampradana type markers enable additional validation:

```text
# Boolean: accepts true/false, yes/no, आम्/न, हाँ/नहीं, and सत्य/असत्य.
अनुमत + अम् सत्य + ङे ग्रह् + णिच् + लोट् + सिप् ।

# Choice: the other sampradana values form the allowed set.
वर्ण + अम् लोहित + ङे नील + ङे विकल्प + ङे ग्रह् + णिच् + लोट् + सिप् ।
```

Boolean values are stored as `SanskritValue.Satya`; selected choices retain the
spelling declared by the script. See `cli/examples/interactive_typed_input.pvm`
for an executable text, boolean, and choice example.

Enter `:cancel` at any value or confirmation prompt to stop the script cleanly.
Closing standard input has the same effect. An interrupted script restores the
session state from before the file started, so values from a partially executed
file are not persisted. Script failures and interruptions return a nonzero CLI
exit code without printing a JVM stack trace.

Operations requiring capabilities outside the default scope pause execution and
ask for confirmation:

```text
Operation प्रेषण-1 requires: NETWORK, EXECUTE_PROCESS, SEND_MESSAGE.
Allow execution? [y/N]:
```

`y`, `yes`, `हाँ`, `हां`, and `आम्` grant only the requested effects and resume
the saved continuation. Any other entered response rejects the operation
cleanly; `:cancel` or end-of-input stops the script. Requested-execution dispositions use the equivalent
`Accept request? [y/N]:` flow.

Running the CLI without arguments starts the interactive REPL.
For a start-to-finish terminal walkthrough, command reference, interactive input
examples, and current limitations, see [Interactive REPL workflow](docs/interactive-repl.md).

The CLI displays successful values according to structured runtime metadata:
`CONSOLE` and `EXTERNAL` results are visible, while `INTERNAL` intermediate
results remain suppressed. Output selection does not depend on trace text.
