# Natural list construction audit

Updated: 2026-10-08.

Execution tests establish computational behavior, not Sanskrit grammaticality.
The current named-list example needs sentence-level verification in addition to
its parser/interpreter/compiler parity tests.

## Naming

`नाम` has an attested indeclinable sense “by name, named”; it must not be
confused with the neuter noun `नामन्`. The construction `हिमालयो नाम
नगाधिराजः` occurs in Kumārasambhava 1.1. This supports a nominal name followed
by the particle and a descriptive nominal, but does not by itself establish
every possible name/head-gender combination.

Sources: [Monier-Williams entry](https://www.sanskritdictionary.com/?q=n%C4%81ma),
[quoted literary example and translation](https://ignca.gov.in/Asi_data/35277.pdf).

Do not mechanically feminize a proper name merely because `सूची` is feminine.
Proper-name apposition and adjective agreement are different questions. The
current `क्रमः नाम ... सूची अस्ति` construction still needs evidence for its
complete naming relation; parser acceptance is not that evidence.

## Selecting an item

The earlier example rendered `क्रमात् प्रथमे मूल्यं गृहाण`. If `प्रथम` is
intended to qualify `मूल्य`, their case endings do not agree: `प्रथमे` is
locative singular, whereas `मूल्यम्` is accusative singular. The runtime instead
treats the locative ordinal as a positional index. That computational convention
does not supply a missing locative head noun in the source sentence.

A grammatical object phrase is `प्रथमं मूल्यम्`: the ordinal adjective agrees
with the neuter accusative singular noun. A genuinely locative position phrase
must express its relation grammatically, rather than silently reinterpret an
adjective as an index slot.

Reference: [nominal gender, case, and number](https://www.learnsanskrit.org/guide/nominals/gender-case-and-number/).

Implemented: shared ordinal-object lowering recognizes the genitive whole and
the singular accusative modifier/object pair, using typed lexical and ordinal
identities. Interpreter and compiler share this projection. The named-list and
ordinal-index examples now use the agreeing object phrase. This does not yet
provide general gender analysis or arbitrary adjective attachment.
Within the supported single-object frame, a unique ordinal is attached by
morphology rather than immediately preceding word order. Multiple competing
ordinals are rejected; no nearest-word preference silently selects one.
Lexical feminine first/second/third ordinals and explicit feminine affixes are
now checked against neuter मूल्य before numeric projection, in both backends.
This is a supported-phrase agreement check, not a general gender inference
engine. Lexical reference: [ordinal paradigms](https://sanskritdocuments.org/learning_tools/subantaruupaNi.html).
Derivation is not transparent to ordinal meaning: for example, a parsed
`प्रथम + तरप्` is not automatically the numeric position one. Shared binding
retains the derived nominal rather than erasing its affix and using the base
stem's rank. Feminine formation preserves rank but remains subject to agreement.

Remaining implementation work:

- Generalize modifier/head analysis beyond the supported singular मूल्य phrase,
  including lexical gender and ambiguous modifier attachment.
- Decide and document the verbal valency of `ग्रह्` and `उद् + हृ` for this
  construction; the latter currently permits only the final member.

The renderer now preserves lexical ordinals instead of converting their numeric
reference into a cardinal stem. This repairs `प्रथम + ङि` → `प्रथमे`, but does
not by itself repair the complete item-selection construction described above.
