# Interactive REPL workflow

The PāṇiniVM REPL evaluates one Sanskrit utterance at a time while retaining the
same conversational session for the lifetime of the process. Use it to explore
operations, inspect derivations, query the Dhātupāṭha, and convert Sanskrit
numerals. Use file execution when a program contains several ordered statements.

## 1. Build and start the terminal launcher

From the repository root on Windows PowerShell:

```powershell
.\gradlew.bat :cli:installDist
.\cli\build\install\cli\bin\cli.bat
```

The direct launcher is recommended for interactive work. Gradle's Windows
console proxy may buffer standard input, so `:cli:run` is better suited to
non-interactive commands.

After startup, the terminal displays the `pvm>` prompt:

```text
pvm>
```

Enter `:help` at any time to display the command summary.

## 2. Evaluate Sanskrit utterances

Enter one complete PVM utterance, including its `।` terminator:

```text
pvm> दश + अम् विंशति + अम् च युज् + णिच् + लोट् + सिप् ।
⇒ त्रिंशत्
```

Each successful utterance updates the active in-memory session. Later
utterances can therefore use normal PVM result and discourse references. An
empty line performs no operation.

The REPL currently accepts one physical line per utterance. Definitions or
programs that require multiple lines should be saved in a `.pvm` file and run
with `--eval` as described below.

## 3. Inspect derivation traces

Toggle trace output before evaluating an utterance:

```text
pvm> :trace
Derivation trace log: ENABLED
pvm> दश + अम् विंशति + अम् च युज् + णिच् + लोट् + सिप् ।
⇒ त्रिंशत्
  ├─► ... derivation steps ...
pvm> :trace
Derivation trace log: DISABLED
```

Trace mode remains enabled until it is toggled again or the REPL exits.

## 4. Respond to interactive operations

An utterance that executes `ग्रह्` pauses and reads a value from the terminal:

```text
pvm> प्रथम + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
Enter value for प्रथम (number):
10
⇒ दश
```

The sampradāna marker controls validation:

| PVM declaration | Prompt | Accepted values |
| --- | --- | --- |
| No type marker | `Enter value for नाम:` | Any text |
| `सङ्ख्या + ङे` | `(number)` | ASCII or Devanagari digits |
| `सत्य + ङे` | `(boolean)` | `true/false`, `yes/no`, `आम्/न`, `हाँ/नहीं`, `सत्य/असत्य` |
| values followed by `विकल्प + ङे` | `(value1/value2)` | One declared value |

Invalid typed input prints an explanation and repeats the same prompt without
advancing the operation:

```text
Enter value for प्रथम (number):
ten
Invalid number 'ten'. Enter ASCII or Devanagari digits.
Enter value for प्रथम (number):
10
```

Enter `:cancel` at a value or confirmation prompt to stop that evaluation
cleanly. End-of-input also stops it without printing a JVM stack trace.

## 5. Approve external capabilities

Operations outside the default execution scope pause before performing their
effects:

```text
Operation प्रेषण-1 requires: NETWORK, EXECUTE_PROCESS, SEND_MESSAGE.
Allow execution? [y/N]:
```

Enter `y`, `yes`, `हाँ`, `हां`, or `आम्` to grant the displayed capabilities for
the continuation. Any other entered value denies the operation. Enter
`:cancel`, or close standard input, to stop the evaluation.

Requested execution uses the equivalent `Accept request? [y/N]:` prompt.

## 6. Use REPL utility commands

### Dhātu lookup

Look up a Dhātupāṭha entry by upadeśa, identifier, or alias:

```text
pvm> :dhatu युजिँर्
pvm> :dhatu 07.0007
```

Use `:dhatu` without a query to display its usage message.

### Decode a Sanskrit numeral

```text
pvm> :num नेत्र-वेद
```

The command attempts Bhūta-saṅkhyā, Āryabhaṭīya, and Kaṭapayādi decoding and
labels each successful interpretation.

### Encode a decimal number

```text
pvm> :encode 42
pvm> :encode 42 katapayadi
pvm> :encode 42 bhutasamkhya
pvm> :encode 42 aryabhatiya
```

Kaṭapayādi is used when the system argument is omitted.

### Compile a PVM file

```text
pvm> :compile cli/examples/interactive_addition.pvm AdditionProgram
```

The optional class name defaults to `GeneratedProgram`. Output is written below
`build/compiled_classes`.

## 7. Run a complete PVM file

The current REPL has no `:load` command. Exit the REPL and invoke the launcher
with `--eval` to execute an ordered, multiline program:

```text
pvm> :exit
शुभमस्तु! (Exiting PāṇiniVM REPL)
```

```powershell
.\cli\build\install\cli\bin\cli.bat --eval cli/examples/interactive_addition.pvm
```

Example interaction:

```text
[PaniniVM CLI] Executing file: interactive_addition.pvm
Enter value for प्रथम (number):
10
Enter value for द्वितीय (number):
20
त्रिंशत्
```

For all typed input forms:

```powershell
.\cli\build\install\cli\bin\cli.bat --eval cli/examples/interactive_typed_input.pvm
```

A successful file exits with status `0`. A runtime failure, cancellation, or
unexpected EOF exits with status `1`. Cancellation restores the session state
captured before file execution, so a partially executed file is not persisted.

## 8. Exit and current limitations

Exit with any of these commands:

```text
:exit
:quit
:त्यज
```

The current REPL intentionally has a small interface. It does not yet provide:

- multiline entry;
- command-history navigation;
- `:load`;
- named session listing or switching;
- an interactive session-clear command.

Restarting the REPL creates a fresh default VM and session. These limitations
are useful boundaries when deciding whether to use the REPL or a `.pvm` file.

## Command reference

| Command | Purpose |
| --- | --- |
| `:help` | Show built-in command help |
| `:trace` | Toggle full derivation traces |
| `:dhatu <query>` | Query the Dhātupāṭha |
| `:num <word>` | Decode a Sanskrit numeral |
| `:encode <number> [system]` | Encode a decimal number |
| `:compile <file.pvm> [ClassName]` | Compile PVM to a JVM class |
| `:exit`, `:quit`, `:त्यज` | End the REPL |
