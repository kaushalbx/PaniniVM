# Sūtra Runtime Migration Path

## Goal

PaniniVM is becoming a programming language whose software is organized and
processed as sūtras. A program should be able to:

1. accept segmented Sanskrit source;
2. compile each executable statement into an inspectable sūtra;
3. preserve ordering, dependencies, governance, and result flow;
4. serialize the program as a canonical sūtra grantha;
5. validate and execute that grantha;
6. inspect its own sūtras and their artha;
7. eventually generate, transform, validate, and execute new sūtras.

The same architecture must also execute migrated Aṣṭādhyāyī rules. Application
software and grammatical software should therefore share the runtime model,
while retaining domain-specific effect interpreters.

## Authoring Principle

Programmers write segmented Sanskrit directly:

```text
दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
```

Artificial wrapper declarations such as the following are not part of the
language:

```text
ग्रन्थ गणितम्
सूत्र प्रथमयोगः
```

The compiler derives sūtra identity, sequence, dependencies, and result flow
from the segmented statements and their grammatical relationships. Explicit
syntax should be introduced only when the grammatical source cannot express a
required concept.

## Architectural Forms

### 1. Segmented Sanskrit source

This is the human-authored form. The existing vyākaraṇa parser and binding
pipeline resolve segmented padas, dhātus, kārakas, operations, and references.

### 2. `SutraBlueprintGrantha`

This is the evaluator-free, inspectable program model. It contains:

- grantha identity;
- sūtra identities;
- Sanskrit source provenance;
- recursive `SutraArtha`;
- dependency and blocking relations;
- governance;
- imports, exports, adhikāras, and saṃjñās.

### 3. Canonical `.sutra` source

This is the stable machine representation produced by
`SutraBlueprintGranthaTextCodec`. It is lossless, portable, reloadable, and
appropriate for generated software, caches, distribution, and tooling.

### 4. `RuntimeSutra` and `SutraMachine`

Domain compilers attach decisions and effects to blueprints. `SutraMachine`
validates, orders, and processes runtime sūtras. Domain effect interpreters
apply program operations or grammatical state changes.

## Completed Foundation

The following runtime foundation is implemented:

- typed `RuntimeSutra`, `SutraProgram`, and `SutraMachine`;
- recursively inspectable `SutraArtha`;
- evaluator-free `SutraBlueprint` and `SutraBlueprintGrantha`;
- canonical codecs for artha, blueprints, and granthas;
- program-blueprint compilation and execution;
- interpreter and JVM planning through blueprint granthas;
- exact dhātu identities in canonical artha;
- segmented `.pvm` source emission to canonical `.sutra`;
- multi-line turn identity and historical result preservation;
- `PaniniVM.evalGrantha` and `evalGranthaFile`;
- sūtra-machine execution as the sole production runtime;
- retirement of the parallel legacy execution pipeline;
- automatic registration of the executing grantha;
- self-reflection over sibling sūtras;
- import/export-aware reflection;
- non-executing validation through `--check-grantha`.

## Migrated Aṣṭādhyāyī Slices

### Opening saṃjñā grantha

`OpeningSamjnaRuntimeGrantha` currently contains:

| Sūtra | Text | Runtime responsibility |
|---|---|---|
| 1.1.1 | वृद्धिरादैच् | Assigns वृद्धि saṃjñā |
| 1.1.2 | अदेङ्गुणः | Assigns गुण saṃjñā |
| 1.1.3 | इको गुणवृद्धी | Applies requested गुण/वृद्धि substitution |

### Phonological saṃjñā grantha

`PhonologicalSamjnaRuntimeGrantha` currently contains:

| Sūtra | Text | Runtime responsibility |
|---|---|---|
| 1.1.7 | हलोऽनन्तराः संयोगः | Assigns संयोग saṃjñā |
| 1.1.8 | मुखनासिकावचनोऽनुनासिकः | Assigns अनुनासिक saṃjñā |
| 1.1.9 | तुल्यास्यप्रयत्नं सवर्णम् | Assigns सवर्ण saṃjñā |

`MigratedAshtadhyayiGranthas` is the discoverable registry for migrated slices.
Their identities, Sanskrit source, artha, relationships, and exports use the
new runtime architecture. During incremental migration, their established
grammatical conditions and state changes remain authoritative and are adapted
into inspectable runtime decisions and effects.

## Migrated Dhātu Actions

`NumericDhatuActionGrantha` contains the first application-action slice:

| Action sūtra | Dhātu action | Declarative operation |
|---|---|---|
| `dhatu-action.numeric.add` | सङ्ख्यायोजनम् | checked numeric `ADD` fold |
| `dhatu-action.numeric.subtract` | सङ्ख्यावियोगः | checked numeric `SUBTRACT` fold |
| `dhatu-action.numeric.multiply` | सङ्ख्यागुणनम् | checked numeric `MULTIPLY` fold |

Each action now carries an evaluator-free blueprint describing its operator,
operand kāraka, minimum operand count, overflow policy, and result type. A
shared numeric-fold interpreter executes those meanings. The individual action
objects retain their existing names and operation registrations but no longer
contain separate Kotlin execution algorithms.

## Migration Rules

Every migrated slice must satisfy all of the following:

1. Use original Aṣṭādhyāyī numbers as sūtra identities.
2. Preserve original Sanskrit text as source provenance.
3. Encode type, role, action, scope, optionality, dependencies, and blocks in
   inspectable artha.
4. Represent local dependency and blocking relationships structurally.
5. Export the intended public sūtras through a runtime grantha.
6. Execute through `SutraMachine`.
7. Preserve grammatical state and trace behavior.
8. Round-trip through canonical blueprint source without information loss.
9. Keep existing tests and add runtime-grantha tests.
10. Avoid migrating a rule when the shared scheduler cannot yet preserve its
    semantics.

## Incremental Migration Phases

### Phase 1 — Declarative and independent saṃjñās

Status: **in progress**

Migrate rules that:

- make local state assignments;
- do not require conflict resolution;
- can be processed once in textual or dependency order;
- already have focused derivation tests.

The 1.1.1–1.1.3 and 1.1.7–1.1.9 slices establish this pattern.

Suggested next candidates:

- other independent saṃjñā rules from 1.1;
- small it-saṃjñā clusters from 1.3;
- well-tested pada and prātipadika saṃjñās;
- self-contained vibhakti selection clusters.

### Phase 2 — Conflict and niṣedha scheduling

Status: **next architectural milestone**

Rules 1.1.4–1.1.6 are deliberately deferred:

- 1.1.4 `न धातुलोप आर्धधातुके`
- 1.1.5 `क्ङिति च`
- 1.1.6 `दीधीवेवीटाम्`

These rules must prevent 1.1.3 before its effect is applied. A single
dependency-ordered pass is insufficient.

Required scheduler work:

- evaluate applicable rules before committing effects;
- establish niṣedha and blocking decisions;
- resolve priority and विप्रतिषेध;
- respect adhikāra and anuvṛtti scope;
- apply visibility regimes such as asiddha and asiddhavat;
- support an agenda with repeated evaluation when state changes expose rules;
- retain deterministic traces explaining selection and suppression.

After this phase, migrate 1.1.4–1.1.6 as the first conflict-sensitive cluster.

### Phase 3 — Shared derivation grantha engine

Status: **planned**

Move derivation orchestration from compatibility adaptation to a native
grantha engine:

1. load migrated Aṣṭādhyāyī granthas;
2. build the active agenda from grammatical state;
3. evaluate blueprints through a derivation-domain compiler;
4. interpret typed derivation effects;
5. update state and repeat until stable;
6. preserve alternatives for optional rules.

At the end of this phase, migrated sūtras no longer require their legacy
evaluator closures.

### Phase 4 — Expand by grammatical subsystem

Status: **planned**

Migrate coherent, tested slices rather than isolated files:

1. opening and interpretative saṃjñās;
2. it-marker processing;
3. pratyāhāra and varṇa rules;
4. kāraka and vibhakti selection;
5. pratyaya selection;
6. aṅga operations;
7. sandhi and final phonology;
8. optional and exception-heavy clusters.

Each subsystem should expose its migrated granthas through a registry and retain
parity tests until its former execution path is retired.

### Phase 5 — Native sūtra artha

Status: **planned**

Replace adapter-held Kotlin behavior with declarative artha and typed effects:

- conditions represented as inspectable expressions;
- grammatical changes represented as data;
- a derivation blueprint compiler;
- no evaluator closure stored in canonical source;
- the same canonical grantha executable after serialization.

This phase makes Aṣṭādhyāyī software genuinely portable instead of merely
runtime-adapted.

### Phase 6 — Sūtra transformation and generation

Status: **planned**

Extend reflection into controlled metaprogramming:

1. inspect a sūtra or artha field;
2. construct a modified `SutraBlueprint`;
3. validate the generated grantha without execution;
4. enforce authority and capability boundaries;
5. execute or persist the generated grantha;
6. retain provenance linking generated rules to their source sūtras.

This is the stage at which PaniniVM fully processes newly generated sūtra
software using the same language mechanisms.

## Application-Program Path

Application programs follow a parallel incremental path:

```text
segmented Sanskrit
    → vyākaraṇa AST
    → executable binding
    → SutraBlueprintGrantha
    → canonical .sutra
    → validation
    → runtime grantha
    → SutraMachine
```

Immediate application-program work:

1. consolidate duplicated script planning used by bytecode and canonical
   emission;
2. make the JVM compiler consume canonical granthas directly;
3. preserve loops and turn semantics in canonical source;
4. load imported granthas from explicit host-provided sources;
5. add source maps from runtime sūtras back to segmented statements.

## Verification Gates

An increment is complete only when:

- focused tests cover the migrated rules;
- canonical encoding and decoding preserve the blueprint;
- runtime grantha validation passes;
- migrated execution produces the established grammatical state;
- trace ordering and applied sūtra identities are correct;
- existing module tests pass;
- no unsupported conflict-sensitive rule is silently migrated;
- the working diff contains no unrelated changes.

Recommended regression command for derivation migrations:

```sh
./gradlew :core:test :ashtadhyayi:test :derivation:test :compiler:test :cli:test --no-daemon
```

## Immediate Next Step

Implement conflict-aware scheduling in `SutraMachine` or a derivation-specific
agenda layered above it. Use 1.1.4–1.1.6 as the acceptance slice:

1. construct a state where 1.1.3 would otherwise apply;
2. make the relevant niṣedha applicable;
3. establish the block before applying 1.1.3;
4. verify that the substitution does not occur;
5. record an explicit blocked trace entry;
6. serialize and reload the complete 1.1.1–1.1.9 grantha;
7. confirm identical behavior after reload.

This milestone unlocks migration of rule systems that behave like the
Aṣṭādhyāyī rather than merely running as a linear list.
