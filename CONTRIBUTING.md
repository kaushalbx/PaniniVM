# Contributing to PaniniVM

Thank you for contributing to PaniniVM. Contributions may include Kotlin code,
`.pvm` programs, tests, documentation, grammatical corrections, and source-data
attribution improvements.

## License for contributions

PaniniVM is licensed under the
[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
Unless you explicitly state otherwise, any contribution intentionally submitted
for inclusion in PaniniVM is submitted under Apache-2.0, as described in
section 5 of the license, without additional terms or conditions.

By submitting a contribution, you represent that you have the right to submit
it. Do not submit code, text, datasets, or other material whose license is
incompatible with Apache-2.0. A Contributor License Agreement and Developer
Certificate of Origin sign-off are not currently required.

## Before opening a pull request

- Treat grammatical validity as a hard acceptance requirement: every `.pvm`
  program accepted by the parser must be valid Sanskrit under PaniniVM's
  supported grammatical system.
- Design programming behavior around a supported Sanskrit construction. Do not
  introduce DSL-only particles, case uses, word-order rules, implicit operands,
  or undeclined placeholders merely because they simplify implementation.
- For every new language construct, test (1) its segmented derivation, (2) its
  grammatical AST and semantic relations, (3) its grammatical readable surface,
  (4) execution parity, and (5) rejection of tempting but ungrammatical
  shorthand.
- Keep executable `.pvm` sentences in segmented Sanskrit and source comments in
  English.
- Add or update tests for behavioral changes.
- Regenerate readable `.txt` companions with `./gradlew renderExamples` when a
  checked-in `.pvm` example changes.
- Run the relevant module tests, or `./gradlew check --no-daemon` for a complete
  verification.
- Cite authoritative grammatical or dataset sources when introducing rules or
  externally sourced material.
- Update `NOTICE` when a contribution introduces material that requires
  attribution.

## Reporting grammatical issues

Include the segmented input, actual result, expected result, relevant sūtra or
source citation, and the smallest reproducible example. Distinguish a surface
rendering problem from an AST, derivation, binding, or runtime problem whenever
possible.

A construct that executes but cannot be rendered as an equivalent grammatical
Sanskrit utterance is a language-validity defect, not merely a presentation
issue.
