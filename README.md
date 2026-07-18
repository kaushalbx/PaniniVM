# Aṣṭādhyāyī Compiler

Kotlin implementation of an executable Pāṇinian derivation system. Implemented
sūtras carry typed metadata and executable eligibility and state-transition
logic. Derivations retain an ordered rule trace, including conflicts and
blocked alternatives where available.

## Current coverage

- 299 implemented sūtras out of the 3,959-rule target.
- It-marker processing, grammatical saṃjñās, rule ordering, substitutions,
  augment insertion, deletion, and selected Tripādī transformations.
- All 21 `sup` forms for masculine a-stems such as `राम` and `देव`.
- All ten lakāras: `LAT`, `LIT`, `LUT`, `LRT`, `LET`, `LOT`, `LANG`, `LING`,
  `LUNG`, and `LRNG`.
- Parasmaipada, Ātmanepada, and explicit Ubhayapada selection through the
  verbal API.
- Gaṇa-aware stem derivation for `LAT`, `LOT`, `LANG`, and `LING` across all
  ten Dhātupāṭha gaṇas, with class-specific strong/weak stem selection.
- Complete representative `LAT`, `LOT`, `LANG`, and `LING` paradigms in both padas
  across all ten gaṇas, including class-specific strong and weak stems.
- Exact `LAT`, `LOT`, and `LANG` root-shape coverage includes vowel-final, consonant-final,
  irregular `गम् → गच्छ्`, and Ubhayapada representatives.

Coverage is deliberately plan-based: a declared form is accepted only when
its required sūtras occur in an end-to-end derivation. This is not yet a
complete implementation of every environment or exception in the
Aṣṭādhyāyī.

## Run

```sh
# Nominal paradigms and individual derivations
./gradlew run --args="--paradigm राम"
./gradlew run --args="--paradigm देव"
./gradlew run --args="--derive राम SASTHI BAHUVACANA"
./gradlew run --args="--derive राम षष्ठी बहुवचन"
./gradlew run --args="--derive देव SAPTAMI EKAVACANA"

# Verbal derivations (defaults to LAT and EKAVACANA)
./gradlew run --args="--verb भू"
./gradlew run --args="--verb भू LING EKAVACANA"
./gradlew run --args="--verb भू लिङ् बहुवचन"
./gradlew run --args="--verb भू LOT बहुवचन"
./gradlew run --args="--verb अद् LING EKAVACANA"
./gradlew run --args="--verb हु LOT EKAVACANA"
./gradlew run --args="--verb रुधिँर् LANG EKAVACANA"
./gradlew run --args="--verb डुक्रीञ् LOT एकवचन"
./gradlew run --args="--verb चुरँ LING EKAVACANA"

# Other supported lakāras
./gradlew run --args="--verb भू LIT EKAVACANA"
./gradlew run --args="--verb भू LUT DVIVACANA"
./gradlew run --args="--verb भू LRT BAHUVACANA"
./gradlew run --args="--verb भू LET EKAVACANA"
./gradlew run --args="--verb भू LUNG EKAVACANA"
./gradlew run --args="--verb भू LRNG EKAVACANA"

# Registry inspection and implementation coverage
./gradlew run --args="--coverage"
./gradlew run --args="--sutra 7.1.54"
./gradlew run --args="--sutra 3.4.103"
```

`--derive` prints a nominal form followed by its ordered sūtra trace.
`--verb` derives prathama-puruṣa and accepts any `Lakara` enum name or its
Devanagari upadeśa, followed by an optional number. It defaults to `LAT` and
singular. `--sutra` prints the typed metadata for an implemented rule, and
`--coverage` prints the current implementation count.

Programmatic paradigm generation is available through `SubantaEngine` and
`TingantaEngine`. Their paradigm results expose both final surfaces and the
complete derivation for every supported slot.

## Test

```sh
./gradlew test
```

To print the step-by-step Curādi Ātmanepada `LING` derivation:

```sh
./gradlew test --tests "dev.sanskrit.ScratchTest.testDerivationTrace" --info
```

## Text encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the
executable specification, so editors and terminals must preserve UTF-8 when
changing Devanagari text.
