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
