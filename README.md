# Aṣṭādhyāyī Compiler

Kotlin implementation of an executable Pāṇinian derivation system. Implemented
sūtras carry typed metadata and executable eligibility and state-transition
logic. Derivations retain an ordered rule trace, including conflicts and
blocked alternatives where available.

## Current coverage

- 292 implemented sūtras out of the 3,959-rule target.
- It-marker processing, grammatical saṃjñās, rule ordering, substitutions,
  augment insertion, deletion, and selected Tripādī transformations.
- All 21 `sup` forms for masculine a-stems such as `राम` and `देव`.
- All ten lakāras: `LAT`, `LIT`, `LUT`, `LRT`, `LET`, `LOT`, `LANG`, `LING`,
  `LUNG`, and `LRNG`.
- Parasmaipada, Ātmanepada, and explicit Ubhayapada selection through the
  verbal API.
- Gaṇa-aware present-stem derivation for all ten Dhātupāṭha gaṇas.
- Complete Kryādi present and imperative paradigms for `डुक्रीञ्`, including
  strong/weak `श्ना` formation and the expected `क्रीणातु`/`क्रीणीताम्` forms.

Coverage is deliberately plan-based: a declared form is accepted only when
its required sūtras occur in an end-to-end derivation. This is not yet a
complete implementation of every environment or exception in the
Aṣṭādhyāyī.

## Run

```sh
./gradlew run --args="--paradigm राम"
./gradlew run --args="--derive राम SASTHI BAHUVACANA"
./gradlew run --args="--derive राम षष्ठी बहुवचन"
./gradlew run --args="--verb भू"
./gradlew run --args="--verb भू LOT बहुवचन"
./gradlew run --args="--verb डुक्रीञ् LOT एकवचन"
./gradlew run --args="--coverage"
./gradlew run --args="--sutra 7.1.54"
```

`--derive` prints a nominal form followed by its ordered sūtra trace.
`--verb` accepts any `Lakara` enum name or its Devanagari upadeśa, followed by
an optional number. It defaults to `LAT` and singular. `--sutra` prints the
typed metadata for an implemented rule, and `--coverage` prints the current
implementation count.

Programmatic paradigm generation is available through `SubantaEngine` and
`TingantaEngine`. Their paradigm results expose both final surfaces and the
complete derivation for every supported slot.

## Test

```sh
./gradlew test
```

To print the step-by-step Kryādi LOT derivations for both padas:

```sh
./gradlew test --tests "dev.sanskrit.ScratchTest.testDerivationTrace" --info
```

## Text encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the
executable specification, so editors and terminals must preserve UTF-8 when
changing Devanagari text.
