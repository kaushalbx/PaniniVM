# PaniniVM Architecture & Segmented ANTLR4 Parser Specification

## Overview

PaniniVM provides an execution runtime for Sanskrit utterances specified in Pāṇinian segmented form (`Prakṛti + Pratyaya`). The canonical ANTLR4 pair `VyakaranamLexer.g4` and `VyakaranamParser.g4` creates the grammar parse tree, and `VyakaranamAstBuilder` constructs the typed AST. `VyakaranamExecutionAdapter` binds that AST directly to dhātu invocations and an `ExecutionProgram`. `OperationResolver` selects a registered executable meaning from grammatical features and kāraka signatures, after which the internal pipeline plans and executes it.

```text
segmented utterance
    -> canonical vyākaraṇa AST
    -> direct execution binding
    -> registered dhātu-operation resolution
    -> planning and authority checks
    -> action execution
```

Executable meanings are registered in the execution package. Dhātupāṭha entries contain linguistic data only and do not depend on runtime actions.

Kāraka and vibhakti rules use the same `Sutra<C, R>` model as the rest of the
Aṣṭādhyāyī catalogue. Each rule occupies its own source file under its chapter
and pāda. Sentence rules use `SutraScope.VAKYA`; no separate kāraka-rule base
class or parallel rule framework exists.

---

## 1. Grammatical Form Specification (सुप्तिङन्तं पदम्)

In accordance with Pāṇini's definition *सुप्तिङन्तं पदम्* (1.4.14), every declinable nominal or conjugable verb must be explicitly segmented with its suffix:

### Nominal Subanta Padas (`subantaPada` / `sankhyaPada`)
- **Format**: `nominalBase (+ stem)* + supPratyaya`
- **Examples**:
  - `एक + अम्` (Numeral stem `एक` + 2nd Vibhakti singular `अम्`)
  - `द्वि + औट्` (Numeral stem `द्वि` + 2nd Vibhakti dual `औट्`)
  - `त्रि + शस्` (Numeral stem `त्रि` + 2nd Vibhakti plural `शस्`)
  - `द्वि + तीय + अम्` (Ordinal stem `द्वि + तीय` + 2nd Vibhakti singular `अम्`)
  - `त्रि + तीय + अंश + अम्` (Fraction stem `त्रि + तीय + अंश` + 2nd Vibhakti singular `अम्`)
  - `सार्ध + द्वि + अम्` (Rational fraction `सार्ध + द्वि` = 2.5 + 2nd Vibhakti singular `अम्`)
  - `अभि + अधिक + शत + अम्` (Modifier stem `अभि + अधिक + शत` = 100 + 2nd Vibhakti singular `अम्`)
  - `ज्या + नवति + अम्` (Trigonometric stem `ज्या + नवति` = $\sin(90^\circ)$ + 2nd Vibhakti singular `अम्`)
  - `यन्त्र + सुँ` (Nominal stem `यन्त्र` + 1st Vibhakti singular `सुँ`)
  - `फल + अम्` (Result reference `फल` + 2nd Vibhakti singular `अम्`)
  - `पूर्वफल + अम्` (Historical reference `पूर्वफल` + 2nd Vibhakti singular `अम्`)

### Numerical System Subsystems
- **Āryabhaṭīya System (`:aryabhatiya`)**: Consonant-vowel numeral decoder (`ख्युघृ` = 4,320,000).
- **Bhūtasamkhyā System (`:bhutasamkhya`)**: Symbolic noun decoder (`नेत्र`=2, `वेद`=4, `अग्नि`=3) with right-to-left digit placement (*अङ्कानां वामतो गतिः*).

---

## 2. Syncretic Sup Endings and Kāraka Resolution

A sup surface may represent more than one slot. In particular:

```text
भ्याम् → {तृतीया-द्विवचन, चतुर्थी-द्विवचन, पञ्चमी-द्विवचन}
```

`PadaAnalyzer` preserves all candidates. The sentence analyzer then applies
semantic dhātu-valency facts and ordered sūtras:

```text
राम + भ्याम् … दा
  → 1.4.32 सम्प्रदान
  → 2.3.13 चतुर्थी

लेखनी + भ्याम् … लिख्
  → 1.4.42 करण
  → 2.3.18 तृतीया

राम + भ्याम् … पलाय्
  → 1.4.24 अपादान
  → 2.3.28 पञ्चमी
```

If semantic and operation constraints do not select exactly one candidate,
the execution binder carries an `AmbiguousKarakaBinding`. Operation signatures
may resolve it later; otherwise ambiguity is reported instead of defaulting to
the first `SupAffix` entry.

The currently registered sentence rules are stored in
`adhyaya1/pada4/*Sutra.kt` and `adhyaya2/pada3/*Sutra.kt` and are included in
the global Aṣṭādhyāyī registry.

---

## 3. Multi-Vākya Sentence Chaining & Result References

A single utterance can contain multiple vākyas chained by connectives (`ततः`, `अथ`, `अनन्तरम्`) or sentence daṇḍas (`।`, `॥`):

```text
एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।
```

### Execution Flow:
1. **Clause 1** (`योग-1`): Computes $1 + 2 = 3$ (`त्रीणि`).
2. **Clause 2** (`योग-2`): `फल + औट्` references `योग-1` ($3$). Computes $3 + 2 = 5$ (`पञ्च`).

---

## 4. Typed numeral execution

The grammar deliberately does not parse ordinary surface Sanskrit. A finished
word such as `पञ्च` is not reverse-analyzed to discover a number; accepted input
supplies morphological segmentation, such as `पञ्च + शस्`.

```text
annotated subanta
    -> VyakaranamParser AST
    -> canonical numeral-prātipadika identity
    -> ExecutionExpression.Pada(value = SanskritValue.Sankhya)
    -> arithmetic action consumes Sankhya.value
    -> SankhyaGenerator derives the result word
    -> ExecutionResult retains display text and typed value
```

The numeric identity is assigned exactly once from the annotated AST. Result
references carry that typed value directly, so chained operations need no
word-to-number dictionary. `SankhyaWordLexicon` and `SanskritNumbers` are not
part of this architecture.

`SankhyaGenerator.annotatedPratipadikaValue` accepts only canonical annotated
numeral prātipadikas known to the numeral domain. It is not a surface-form parser
and does not accept arithmetic notation such as `त्रि + द्वि × शत` or wrapper
syntax such as `एकोन(विंशति)`.

Runtime invariants:

- Use `ExecutionExpression.sankhya(value, prakriti)` for an explicitly typed
  numeral leaf.
- `SanskritValue.of(text)` never infers `Sankhya` from Sanskrit text.
- Arithmetic actions reject `Shabda`, even if its display text resembles a
  numeral.
- Generated numeral words are outputs; they are not reparsed as inputs.
- Legacy text-only persisted state remains text rather than receiving a guessed
  numeric type.

---

## 5. `.pvm` Script File Format & `PvmUktiSadhaka` Engine

`.pvm` (PaniniVM) files store sequential Pāṇinian grammatical and semantic program instructions. Blank lines and script comments (`#`, `//`) are automatically ignored.

### `PvmUktiSadhaka` (Pāṇinian Grammatical Rūpa-Siddhi)
The `PvmUktiSadhaka` engine evaluates parsed AST nodes to perform full `rūpa-siddhi` (रूपसिद्धि) on segmented `.pvm` script lines:
- **`sadhayaScript(scriptContent)`**: Processes multi-line script content sequentially.
- **`sadhayaLine(lineText)`**: Evaluates Pāṇinian `Ukti` ASTs containing optional `Sambodhana` (vocative calling), `Vākya` clauses, and `Pada` sequences.
- **`sadhayaSubanta(subantaPada)`**: Binds `MūlaPrātipadika`, `KṛdantaPrātipadika`, `UṇādyantaPrātipadika`, or `SamāsaPrātipadika` with `SupAffix` via `SubantaEngine` to derive exact declension surfaces.
- **`sadhayaTinganta(tingantaPada)`**: Binds Dhātu root, `Upasarga` prefixes, Lakāra mode, and `TingAffix` via `TingantaEngine` to derive exact verbal conjugation surfaces.

### Dhātu Action Resolution Architecture (`DhatuAction` & `DhatuOperation`)
Semantic execution maps resolved grammatical features to registered `DhatuAction` instances:
- **`DhatuAction`**: Abstract base carrying operational metadata (`name`, `description`) and `execute(context, operation)` handler logic.
- **`OperationResolver`**: Resolves Dhātu root signatures, Kāraka roles, and valencies into executable `DhatuOperation` plans.
- **`ExecutionContext`**: Maintains environment state, discourse models (`DiscourseModel`), active `Samjñā` assignments (`ExecutionSamjna`), typed `SanskritValue` stores, and inter-clause continuations.

### Running `.pvm` Scripts:
```powershell
./gradlew :cli:run --args="--eval path/to/script.pvm"
```

---

## 7. Samāsa (Compound Formation) & Phonological Transformation Engine

### Samāsa Subsystem (Adhyāya 2.1 & 2.2)
Compound formation sūtras operate on `DerivationState` sequences containing multiple component subantas:
- **Avyayībhāva** (`2.1.6` *Avyayaṁ vibhakti-samīpa-*): Combines an Avyaya prefix with a subanta term into a unified compound stem.
- **Tatpuruṣa** (`2.1.24` Dvitīyā, `2.1.37` Pañcamī, `2.2.8` Ṣaṣṭhī): Combines case-marked subanta terms (*kṛṣṇa + śritaḥ* $\rightarrow$ *kṛṣṇaśritaḥ*, *cora + bhayam* $\rightarrow$ *corabhayam*, *rāja + puruṣa* $\rightarrow$ *rājapuruṣa*).
- **Bahuvrīhi** (`2.2.24` *Anekam anyapadārthe*): Combines subantas into an exocentric compound (*pīta + ambara* $\rightarrow$ *pītāmbara*).
- **Dvandva** (`2.2.29` *Cārthe dvandvaḥ*): Combines co-ordinate nominal terms (*rāma + kṛṣṇa* $\rightarrow$ *rāmakṛṣṇa*).

Each compound formation rule replaces component terms with a unified `DerivationTerm("samasa", surface, TermKind.PRATIPADIKA)` and assigns `Samjna.PRATIPADIKA` to enable downstream inflection.

### Phonological Sandhi Engine & Pratyāhāra Integration
Phonological transformations (`6.1.109`, `6.1.132`, `6.3.111`, `8.3.14`, `8.3.17`, `8.3.22`, `8.4.59`, `8.4.60`, `8.4.62`, `8.4.63`, `8.4.65`) use `PratyaharaEngine` for type-safe Māheśvara-sūtra sound matching (`Pratyahara.EN`, `Pratyahara.YAY`, `Pratyahara.JHAR`, `Pratyahara.ASH`, `Pratyahara.HAL`). Rule applicability logic is implemented directly in `override fun matches(context: DerivationState): Boolean` without external helper classes or heuristic string matching shortcuts.

---

## 8. Verification

Run the full test suite:
```powershell
./gradlew test --no-daemon
```
