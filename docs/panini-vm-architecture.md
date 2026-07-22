# PaniniVM Architecture & Segmented ANTLR4 Parser Specification

## Overview

PaniniVM provides a complete execution virtual machine for Sanskrit utterances specified in Pāṇinian segmented form (`Prakṛti + Pratyaya`). The parser front-end is implemented by the canonical ANTLR4 pair `VyakaranamLexer.g4` and `VyakaranamParser.g4`. `VyakaranamAstBuilder` constructs the canonical typed AST, `VyakaranamExecutionAnalyzer` derives execution semantics directly from it, and `BhashaExecutionEngine` performs rule execution.

---

## 1. Grammatical Form Specification (सुप्तिङन्तं पदम्)

In accordance with Pāṇini's definition *सुप्तिङन्तं पदम्* (1.4.14), every declinable nominal or conjugable verb must be explicitly segmented with its suffix:

### Nominal Subanta Padas (`subantaPada`)
- **Format**: `nominalBase + supPratyaya`
- **Examples**:
  - `एक + अम्` (Numeral stem `एक` + 2nd Vibhakti singular `अम्`)
  - `द्वि + औट्` (Numeral stem `द्वि` + 2nd Vibhakti dual `औट्`)
  - `त्रि + शस्` (Numeral stem `त्रि` + 2nd Vibhakti plural `शस्`)
  - `यन्त्र + सुँ` (Nominal stem `यन्त्र` + 1st Vibhakti singular `सुँ`)
  - `फल + अम्` (Result reference `फल` + 2nd Vibhakti singular `अम्`)
  - `पूर्वफल + अम्` (Historical reference `पूर्वफल` + 2nd Vibhakti singular `अम्`)

### Verbal Tiṅanta Padas (`tingantaPada`)
- **Format**: `(upasarga +)* dhatu (+ sanadiPratyaya)* + lakara + tingPratyaya`
- **Examples**:
  - `युज् + णिच् + लोट् + सिप्` (Dhātu `युज्` + Causal `णिच्` + Imperative `लोट्` + 2nd person singular `सिप्`)
  - `गण + णिच् + लोट् + सिप्` (Dhātu `गण` + Causal `णिच्` + Imperative `लोट्` + 2nd person singular `सिप्`)
  - `कृ + लोट् + सिप्` (Dhātu `कृ` + Imperative `लोट्` + 2nd person singular `सिप्`)

---

## 2. Multi-Vākya Sentence Chaining & Result References

A single utterance can contain multiple vākyas chained by connectives (`ततः`, `अथ`, `अनन्तरम्`) or sentence daṇḍas (`।`, `॥`):

```text
एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।
```

### Execution Flow:
1. **Clause 1** (`योग-1`): Computes $1 + 2 = 3$ (`त्रीणि`).
2. **Clause 2** (`योग-2`): `फल + औट्` references `योग-1` ($3$). Computes $3 + 2 = 5$ (`पञ्च`).

---

## 3. `.pvm` Script File Format

`.pvm` files store sequential PaniniVM program instructions. Blank lines and comments (`#`, `//`) are automatically ignored.

### Running `.pvm` Scripts:
```powershell
./gradlew run --args="--eval path/to/script.pvm"
```

---

## 4. Verification

Run the full test suite:
```powershell
./gradlew test --no-daemon
```
