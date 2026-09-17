# Module `:parser`

ANTLR4 segmented Sanskrit parser and AST construction pipeline.

## Acceptance contract

The parser is a grammatical boundary, not a permissive DSL front end. A parsed
`.pvm` program is eligible for execution only when it is valid Sanskrit under
the grammatical system supported by PaniniVM. Lexer and parser productions must
not assign programming semantics to forms that lack a supported Sanskrit
derivation and sentence analysis.

Adding a production therefore requires positive tests for derivation, sentence
analysis, and grammatical readable rendering, plus negative tests showing that
ungrammatical programming shorthand is rejected. When the required Sanskrit
analysis is not implemented, the construct remains unsupported.

## Overview

Contains the canonical ANTLR4 grammar files:
- **`VyakaranamLexer.g4`**: Tokenizes Devanagari input, connectives (`ततः`, `अथ`), sentence daṇḍas (`।`, `॥`), and operators (`+`).
- **`VyakaranamParser.g4`**: Parses segmented Pāṇinian expressions (`Prakṛti + Pratyaya`).
- **`VyakaranamAstBuilder`**: Transforms ANTLR4 parse trees into strongly typed Kotlin AST representations.
