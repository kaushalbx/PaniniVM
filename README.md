# Aṣṭādhyāyī Compiler

Kotlin implementation of an executable Pāṇinian derivation system. Each loaded
sūtra directly extends `BaseSutra` and defines both its eligibility and its
grammatical state transition.

Current executable coverage includes the opening 1.1 rules, it-processing,
selected nominal transformations, and all 21 sup forms of masculine a-stem
`राम`.

## Run

```sh
gradle run --args="--paradigm राम"
gradle run --args="--derive राम SASTHI BAHUVACANA"
gradle run --args="--derive राम षष्ठी बहुवचन"
gradle run --args="--verb भू"
gradle run --args="--verb भू बहुवचन"
gradle run --args="--coverage"
gradle run --args="--sutra 7.1.54"
```

`--derive` prints the resulting form followed by the ordered sūtra trace.
`--sutra` prints the direct `BaseSutra` fields for a loaded rule.

## Test

```sh
gradle test
```
