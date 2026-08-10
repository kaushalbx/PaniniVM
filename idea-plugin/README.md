# Module `:idea-plugin`

IntelliJ IDEA plugin integration for PaniniVM.

## Overview

Provides IDE integration features:
- `.pvm` script syntax highlighting and file type recognition.
- Sūtra reference navigation and derivation inspection.

## Running interactive scripts

Run a `.pvm` file from its gutter icon and select **Run via VM**. The Run tool
window uses the same interactive execution pipeline as the CLI. When a script
executes `ग्रह्`, click the console, enter the requested value, and press Enter.
Typed number, boolean, and choice validation, capability approvals, `:cancel`,
explicit-output filtering, rollback, and exit codes therefore match terminal
execution.

**Run via CLI Process** starts `dev.panini.MainKt` in an isolated Java process and
forwards Run-console input directly, avoiding Gradle's Windows stdin buffering.
The Stop action closes pending input and terminates the child process.
