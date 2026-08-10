# Module `:cli`

Command-line interface runner for PaniniVM.

## Overview

Main entry point (`Main.kt`) supporting CLI flags:
- `--eval <file.pvm>`: Evaluates `.pvm` script files.
- `--compile <file.pvm> [ClassName]`: Compiles `.pvm` files directly to JVM bytecode.
- `--unadi [lookup|pair|list]`: Uṇādipāṭha etymological inspection and reverse lookup.
- `--derive-unadi <dhatu> <pratyaya>`: Step-by-step Aṣṭādhyāyī derivation trace for Uṇādi stems.
- `--paradigm <pratipadika>`: Prints 21-slot nominal subanta paradigms.
- `--derive <pratipadika> <vibhakti> <vacana>`: Step-by-step nominal derivation trace.
- `--verb <dhatu> [lakara] [vacana]`: Step-by-step verbal tiṅanta derivation trace.
- `--coverage`: Prints registered sūtra count and role breakdowns.

## Interactive execution

Build the CLI launcher once, then run it directly. The direct launcher is
recommended for interactive input because Gradle's Windows console proxy may
not deliver terminal lines until EOF.

```powershell
.\gradlew.bat :cli:installDist
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

The Gradle `:cli:run` task remains suitable for non-interactive commands.

The CLI displays successful values according to structured runtime metadata:
`CONSOLE` and `EXTERNAL` results are visible, while `INTERNAL` intermediate
results remain suppressed. Output selection does not depend on trace text.
