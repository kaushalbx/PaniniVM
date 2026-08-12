# PaniniVM Architecture

This document describes the workspace modules, execution pipeline, major
grammatical subsystems, and implementation coverage. For an introduction to
writing and running Sanskrit programs, start with the [main README](README.md).

## Workspace modules

```text
PaniniVM Root
├── :core            Domain models, sūtras, derivation state, saṃjñā, and artha
├── :ashtadhyayi     Executable Aṣṭādhyāyī sūtra catalog
├── :dhatupatha      Ten-gaṇa Dhātupāṭha root catalog
├── :unadipatha      Declarative Uṇādipāṭha and stem analysis
├── :linganushasanam Pāṇinian gender resolution
├── :derivation      Subanta, Tiṅanta, Samāsa, Saṅkhyā, and Uṇādi derivation
├── :analysis        Sentence, Kāraka, and morphological analysis
├── :parser          ANTLR4 segmented Sanskrit parser and AST construction
├── :actions         Typed action dispatch
├── :compiler        `.pvm` to JVM bytecode compiler
├── :cli             Command-line execution and inspection
├── :aryabhatiya     Āryabhaṭīya numerical encoding and decoding
├── :bhutasamkhya    Bhūtasaṅkhyā symbolic-number decoding
├── :sankhya         Numeral generation and transformation
├── :katapayadi      Kaṭapayādi encoding and decoding
├── :ganapatha       Gaṇapāṭha nominal-list registry
├── :execution       Planning, typed runtime values, scope, and safety gates
└── :idea-plugin     IntelliJ Platform language support
```

## Language execution pipeline

```text
segmented input → vyākaraṇa AST → binding → operation resolution → planning → runtime
```

The parser retains `Prakṛti + Pratyaya` structure. Analysis assigns grammatical
and semantic identities. Binding resolves Kāraka-marked participants and typed
values. Planning determines executable actions and control flow. The runtime
executes the plan while retaining semantic values, scopes, result history, and
capability approvals.

## Major grammatical subsystems

- **Aṣṭādhyāyī:** executable rules for nominal and verbal derivation,
  compounds, cases, semantic roles, and morphophonology.
- **Central saṃjñā and artha model:** typed unit, affix, phonological, stem,
  avyaya, Kāraka, conventional, dispositional, and Taddhita identities.
- **Uṇādipāṭha:** suffix assignment, etymological reverse lookup, and the
  distinction between conventional and derivable nominal stems.
- **Liṅgānuśāsanam:** sūtra-driven gender resolution for nominal, affixal,
  and compound contexts.
- **Vākya analysis:** contextual resolution of syncretic case endings through
  Kāraka and Vibhakti rules.
- **Samāsa and Sandhi:** typed compound formation and phonological derivation
  driven by registered sūtras and the Pratyāhāra engine.
- **Numerical traditions:** Sanskrit numerals, Āryabhaṭīya, Bhūtasaṅkhyā,
  and Kaṭapayādi representations.

## Runtime and tooling

- Typed `SanskritValue` values pass through pipelines without rendering and
  reparsing.
- Reusable saṃjñā-kriyā definitions support typed signatures, structured
  results, local scopes, overload selection, visibility, and memoization.
- The compiler emits native JVM `.class` bytecode from `.pvm` source.
- The CLI supports execution, readable rendering, compilation, canonical
  grantha files, derivation inspection, and an interactive REPL.
- The IDEA plugin provides file recognition, highlighting, completion,
  diagnostics, documentation, structure view, and gutter execution.

## Implementation coverage

- 893 registered executable Aṣṭādhyāyī sūtras out of the 3,959-sūtra catalog,
  across derivation,
  Sandhi, Samāsa, Kāraka, and Vibhakti scopes.
- 33 declaratively registered Uṇādipāṭha sūtras.
- Full classical non-Vedic coverage of Aṣṭādhyāyī 2.3 Vibhakti and
  1.4 Kāraka sūtras.
- Classical nominal stem classes across three genders, pronouns, numerals, and
  consonant stems.
- Dhātupāṭha coverage across all ten verbal gaṇas.

Coverage figures describe the current implementation rather than a claim that
every domain of the complete grammatical tradition is finished.
