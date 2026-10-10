# Open sandhi modeling gaps

These are unresolved findings, not supported derivations. Passing the executable
benchmark corpus does not establish complete sandhi coverage.

## Completed-pada visarga provenance

The initial 2026-10-10 benchmark audit reproduced these failures through the string-input
`SandhiEngine.join` API:

| Inputs | Required result | Observed result | Observed rules |
| --- | --- | --- | --- |
| हरिः + रम्यः | हरीरम्यः | हरियरम्यः | 8.3.17 |
| शम्भुः + राजते | शम्भूराजते | शम्भुयराजते | 8.3.17, 8.3.24, 8.4.58 |

The [commentary on ढ्रलोपे पूर्वस्य दीर्घोऽणः](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.3.111.htm)
derives the examples from s-final underlying forms, through rutva and r-lopa.
Executable cases SANDHI_286–287 therefore use हरिस् and शम्भुस् and require
8.2.66, 8.3.14, and the repository's 6.3.111 identifier. They do **not** test or
repair the completed-pada visarga inputs above.

The active `BhoBhagoAghoApurvasyaYoshiSutra` now checks the lexical/a-purva domain,
requires rutva provenance for a final repha, and substitutes only consonantal y.
Regression tests reject the i/u-final visarga inputs above for this rule. The
table records the original diagnostic, not the output after that scope correction.
The external-pada input model still lacks an explicit underlying s/r identity for
a written visarga; rejecting the incorrect yatva is not a complete derivation.
Do not infer a unique historical s/r identity from the
written sign, add output-specific substitutions, or silently treat these failures
as covered by the underlying-form benchmarks.

The source's displayed numbering varies between 6.3.110 and 6.3.111. This document
uses the existing executable repository identifier, without claiming the numbering
audit is complete.
