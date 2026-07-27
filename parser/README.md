# Module `:parser`

ANTLR4 segmented Sanskrit parser and AST construction pipeline.

## Overview

Contains the canonical ANTLR4 grammar files:
- **`VyakaranamLexer.g4`**: Tokenizes Devanagari input, connectives (`ततः`, `अथ`), sentence daṇḍas (`।`, `॥`), and operators (`+`).
- **`VyakaranamParser.g4`**: Parses segmented Pāṇinian expressions (`Prakṛti + Pratyaya`).
- **`VyakaranamAstBuilder`**: Transforms ANTLR4 parse trees into strongly typed Kotlin AST representations.
