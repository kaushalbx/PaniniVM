# Module `:derivation`

## Runtime-grantha migration

The opening saṃjñā cluster is the first Aṣṭādhyāyī slice migrated into the
shared runtime-grantha architecture:

- 1.1.1 `वृद्धिरादैच्`
- 1.1.2 `अदेङ्गुणः`
- 1.1.3 `इको गुणवृद्धी`
- 1.1.7 `हलोऽनन्तराः संयोगः`
- 1.1.8 `मुखनासिकावचनोऽनुनासिकः`
- 1.1.9 `तुल्यास्यप्रयत्नं सवर्णम्`

`Ashtadhyayi.executableSutras` owns the complete runtime membership.
`AshtadhyayiRuntimeGrantha` materializes those catalog entries with runtime
identities, inspectable artha, dependency ordering, and exports. The original
grammatical conditions and state changes
remain in the established rule objects while execution is verified through
`SutraMachine`; this permits incremental migration without duplicating the
Sanskrit rules or changing derivation results.

Rules 1.1.4–1.1.6 participate in the same runtime grantha so the shared
scheduler can apply niṣedha conflict precedence before the blocked rule's effect.

Core derivation engines and morpho-phonological pipeline execution.

## Overview

The `:derivation` module houses:

- **`DerivationEngine`**: State-transition execution engine evaluating ordered Aṣṭādhyāyī sūtras on `DerivationState`.
- **`SubantaEngine`**: 21-slot nominal paradigm derivation engine across all 31 Classical nominal stem classes.
- **`TingantaEngine`**: Verbal conjugation derivation engine across all 10 Lakāras and 10 Gaṇas.
- **`SankhyaGenerator`**: Cardinal and ordinal numeral derivation generator.
- **`UnadiDerivationBridge` & `UnadiDerivationEngine`**: Bridges Uṇādi matches into `DerivationState` to run full Aṣṭādhyāyī step-by-step derivation traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*).

## Derivation mutation lifecycle

Derivation rules must keep grammatical identity separate from the current written surface:

- Use `replaceWholeAffix` for an entire pratyaya or āgama substitution and choose an explicit designation policy: preserve/remap surviving exact designations, consume superseded designations, or introduce a fresh `RAW_UPADESHA`.
- Use `substituteTermSurface` for a phonological change within one surviving term. It records `VarnaSubstitution` provenance atomically and rejects stale active or deferred designations.
- Use `mergeTermsByVarnaSubstitution` when sandhi leaves one adjacent term and consumes the other. Affixes are lifecycle-consumed with the deleting sūtra.
- Use `redistributeAdjacentTermsByVarnaSubstitution` when both adjacent terms survive but written material moves across their boundary.
- Use `consumeAffixForDrop`, or `removeTerm(id, sutra)`, for affix deletion. Anonymous affix removal is rejected.

`TasyaLopah` deletes only the exact segment carried by a surviving `ItDesignation`. Rules must never infer deletion from `ItMarker`, spelling, term IDs, or affix shape. A completed derivation must pass `requireCompleteItProcessing()` and contain neither pending lifecycle phases nor unconsumed immediate or deferred designations.

### Grammatical provenance and internal transitions

`DerivationApplication` and `DerivationEvent.RuleApplied` are reserved for real
grammatical rules. Their identifiers must name catalogued Aṣṭādhyāyī sūtras (or
an explicitly selected external grammatical corpus such as the Uṇādipāṭha).
Lifecycle bookkeeping must not be represented by an invented sūtra, a
synthetic rule number, or a dummy `VarnaSubstitution`.

The scheduler records successfully applied grammar in `appliedSutras` for
ordering and one-application guards. `VarnaSubstitution` is reserved for an
actual change of written material and carries its target, source, replacement,
and assigning sūtra. Consequently a saṃjñā, adhikāra, or other non-phonological
application can be present in `appliedSutras` and the public trace without
creating a fictitious character substitution.

Every `DerivationResult` checks that its `appliedSutras` is exactly the initial
history followed by its public `DerivationApplication`s. Completed workflow
boundaries enforce the it-processing invariant; phase-local results may still
carry raw material specifically owned by a following IT-processing phase.

After 1.3.9, `ItMarkerProvenance` retains the exact designated segment and the
1.3.2–1.3.8 sūtra that assigned it. Downstream accent computation receives a
typed `SvaraContext` built from this evidence and exact sup identity; it does not
infer a sup or marker trigger from the final spelling or from generic pratyaya
shape.

Accent assignment itself belongs to the Aṣṭādhyāyī registry. Executable rules
3.1.3, 3.1.4, 6.1.158, and 6.1.197 operate in `SutraStage.SVARA` and write
`SvaraAssignment` values whose source is either the assigning sūtra or explicit
lexical accent metadata. `SvaraEngine` discovers vowel positions, schedules
that registry stage, and renders the resulting accents; it contains no
sūtra-number dispatch table.

The transition from `RAW_UPADESHA` to `PROCESSED` when 1.3.2–1.3.8 designate
no segment is such bookkeeping. `DerivationEngine` performs it without adding
an application or rule event, and only when its active rule set contains the
designation rules. A partial phase that introduces a raw upadeśa leaves it raw
for the later `IT_PROCESSING` phase. This ownership rule prevents an aṅgakārya,
rutva, or sandhi-only phase from prematurely completing a newly introduced
affix or āgama.

Immediate exact designations remain active until 1.3.9 consumes them. Deferred
designations remain attached while the term is in `DEFERRED_SUBSTITUTION` and
become eligible for deletion only after the licensed whole-affix substitution.
These transitions may change internal lifecycle state, but only the grammatical
sūtras that designate, substitute, or delete material appear in the derivation
trace.

Control domains are likewise separate from sūtra identity. For example, the
prohibition of feminine-affix selection is represented by the typed
`BlockedOperationDomain.STRI_PRATYAYA_SELECTION`; `blockedSutras` contains only
actual sūtra numbers, so symbolic control labels cannot leak into grammatical
provenance.
