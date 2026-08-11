# PaniniVM Language Enhancement Plan

This document describes the next evolution of the PaniniVM `.pvm` language.
It records what is already implemented, what remains planned, and the expected
syntax and acceptance criteria for each enhancement.

The design goal is to make Sanskrit programs more natural, typed, composable,
and pleasant to write without introducing unnecessary variables or
runtime-specific keywords.

## 1. Design principles

Language enhancements should follow these principles:

1. **Grammar carries meaning**: case, derivation, and sentence structure should
   express programming roles wherever practical.
2. **Typed values remain semantic**: pipelines must transport values, not render
   and reparse strings.
3. **Few temporary variables**: direct results, structured access, and pipelines
   should cover short-lived data flow.
4. **Natural reuse**: repeated behavior belongs in a named saṃjñā-kriyā.
5. **Early feedback**: syntax and type mistakes should appear in the IDEA editor
   before execution when they can be determined statically.
6. **Backward compatibility**: existing `.pvm` examples should remain valid
   unless a migration is explicitly documented.
7. **English source comments**: implementation and example comments remain in
   English; executable program sentences remain Sanskrit.

## 2. Current language baseline

The following features are implemented and form the baseline for future work.

### 2.1 Core program structure

- Segmented prakṛti/pratyaya source notation.
- Canonical grammatical AST construction.
- Multi-sentence `.pvm` scripts.
- Single and double danda block boundaries.
- Sanskrit numeral evaluation and Subanta-derived forms.
- Typed runtime values and semantic result history.

### 2.2 Data flow and control flow

- Direct `फल` references.
- `ततः` result pipelines.
- Typed operands throughout execution pipelines.
- Conditional expressions and nested conditionals.
- Bare conditional branch values.
- Fixed repetition through `कृत्वः`.
- Condition-controlled `यावत् … तावत्` loops.
- Bounded loops and `अन्यथा` exhaustion branches.
- Natural nearest-loop termination.
- Immediate console-output flushing.
- Scoped range declarations and range-validated input.

### 2.3 Reusable first-class Sanskrit kriyās

- Reusable saṃjñā-kriyā definitions.
- Multi-sentence function bodies.
- Typed `सङ्ख्या`, `शब्द`, and `सूची` parameters.
- Individually named parameters.
- Declared primitive and structured results.
- Positional calls.
- Named ṣaṣṭhī/dvitīyā call arguments in any order.
- Arity and argument-type validation.
- Result-type and result-schema validation.
- Nested calls and isolated child scopes.
- Multi-file definitions.
- Public and file-private `अन्तरङ्गा` visibility.
- अधिकार domain dispatch.
- Typed अन्तरतम overload selection.
- `अपवाद`, `अन्तरङ्ग`, and `नित्य` precedence.
- `क्त`-based memoization.
- Typed saṃjñā stages in pipelines.

### 2.4 Structured values and tooling

- मतुप्/वत् structure construction.
- Nested ṣaṣṭhī attribute access.
- Named `…परिणाम` schemas.
- Typed `SanskritValue.Rupa` fields.
- Semantic and persistent structured-value codecs.
- Signature, call, pipeline, and schema diagnostics.
- IDEA syntax highlighting, live annotations, and run integration.

## 3. Planned enhancement overview

| Priority | Enhancement | Primary benefit | Status |
|---|---|---|---|
| P1 | Optional and default parameters | Shorter reusable calls | Planned |
| P1 | Explicit return expressions | Predictable function results and early return | Planned |
| P1 | Structured result pattern matching | Concise outcome handling | Planned |
| P2 | Generic collection signatures | Safe typed collection algorithms | Planned |
| P2 | IDEA signature assistance | Faster and safer authoring | Planned |
| P2 | Exhaustiveness and reachability analysis | Safer conditionals and matches | Planned |
| P3 | Higher-order kriyā values | Reusable algorithm composition | Exploratory |
| P3 | Destructuring structured values | Concise field binding | Exploratory |
| P3 | Module imports and explicit exports | Scalable project organization | Exploratory |

## 4. P1: Optional and default parameters

### Goal

Allow a typed parameter declaration to provide a default value. Calls may omit
that argument while retaining arity and type safety.

### Proposed syntax

```pvm
अनुमानक्रीडा + ल्युट् + सुँ ।
न्यूनसीमा + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
उच्चसीमा + सुँ सङ्ख्या + सुँ दश + शस् इति विकल्पमान + सुँ ।
प्रयत्नसीमा + सुँ सङ्ख्या + सुँ पञ्च + शस् इति विकल्पमान + सुँ ।
शब्द + सुँ इति परिणाम + सुँ ।
...
```

Named calls could provide only the values that differ from the defaults:

```pvm
न्यूनसीमा + ङस् एक + अम्
अनुमानक्रीडा + ल्युट् + टा कृ + लोट् + सिप् ।
```

### Semantic rules

- Required parameters must precede optional parameters in positional calls.
- Named calls may omit any optional parameter.
- A default value must match its declared type.
- Defaults are evaluated in declaration scope and should be immutable.
- Omitting a required parameter remains an error.
- Existing `मान` declarations remain required parameters.

### Implementation areas

- Extend `SamjnaParameter` with optional default expression/value metadata.
- Extend the signature declaration parser.
- Apply defaults in `NamedSamjnaArgumentResolver` and positional binding.
- Include defaulted parameters in overload ranking.
- Add IDEA hints showing default values.

### Acceptance criteria

- Positional and named calls can omit optional parameters.
- Incorrect default types are diagnosed before execution.
- Missing required parameters still fail.
- Defaults remain typed when passed into a pipeline.
- Existing function definitions and calls behave unchanged.

## 5. P1: Explicit return expressions

### Goal

Allow a kriyā body to state exactly which value it returns instead of always
using the last successful body sentence.

### Proposed syntax

```pvm
फल + अम् प्रति + अर्प् + लोट् + सिप् ।
```

The segmented `प्रति + अर्प्` action expresses returning the supplied value to
the caller.

Example:

```pvm
परम + ल्युट् + सुँ ।
वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
सङ्ख्या + सुँ इति परिणाम + सुँ ।
यदि वाम + अम् दक्षिण + अम् च विद् + लोट् + सिप्
तर्हि वाम + अम् प्रति + अर्प् + लोट् + सिप्
अन्यथा दक्षिण + अम् प्रति + अर्प् + लोट् + सिप् ॥
```

### Semantic rules

- A return terminates the nearest saṃjñā-kriyā invocation, not an outer caller.
- The returned semantic value must satisfy the declared result type/schema.
- A return inside a loop exits the function, not merely the loop.
- A body without an explicit return retains the current last-success behavior
  for backward compatibility.
- Returning from top-level script code is invalid.

### Implementation areas

- Add a function-return control signal distinct from `BREAK_LOOP`.
- Add a return AST/execution node or a canonical return action.
- Propagate the return through nested body sequences and loops.
- Validate return types at each return site when statically known.
- Stop memoized evaluation after the selected return.

### Acceptance criteria

- Early return works in conditionals and loops.
- Nested function returns do not escape their invocation boundary.
- Return validation preserves structured values.
- Existing last-result functions remain valid.

## 6. P1: Structured result pattern matching

### Goal

Handle structured outcomes directly without repeatedly writing separate field
access and comparison pipelines.

### Proposed syntax

```pvm
अनुमानपरिणाम + मतुप् + ङस् अवस्था + अम् विजय + अम् चेत्
जय + अम् मुद्र् + लोट् + सिप् ।
```

A complete multi-arm form could be:

```pvm
अनुमानपरिणाम + मतुप् + ङस् अवस्था + अम्
विजय + अम् चेत् जय + अम् मुद्र् + लोट् + सिप्
समाप्ति + अम् चेत् प्रयत्नाः + अम् समाप्ताः + अम् च मुद्र् + लोट् + सिप् ।
```

The final surface grammar should be selected only after parser experiments
confirm that it remains unambiguous with existing conditional and attribute
syntax.

### Semantic rules

- Matching uses typed schema identity and field values.
- Arms are evaluated in source order.
- Only the selected arm executes.
- Nested fields may participate in a pattern.
- A fallback arm is permitted for open-ended values.
- Known closed variants should receive exhaustiveness analysis.

### Implementation areas

- Add pattern and match nodes to the grammar and typed AST.
- Reuse `TaddhitaAttributeAccess` for field paths.
- Add typed literal and schema-pattern representations.
- Add exhaustiveness and unreachable-arm diagnostics.
- Teach IDEA highlighting and inlays about pattern boundaries.

### Acceptance criteria

- Guessing-game success and exhaustion handling can use one match expression.
- Missing required schema fields are diagnosed.
- Only one matching arm produces effects.
- Pattern results can continue through `ततः`.

## 7. P2: Generic collection signatures

### Goal

Distinguish collections by element type and preserve that information through
collection pipelines.

### Proposed syntax

```pvm
मानानि + सुँ सङ्ख्या + ङस् सूची + सुँ इति मान + सुँ ।
```

This represents a list whose elements are numbers. A text list would use:

```pvm
पदानि + सुँ शब्द + ङस् सूची + सुँ इति मान + सुँ ।
```

### Semantic rules

- `सूची` without an element type remains the compatible unconstrained form.
- Element types participate in overload ranking.
- Map-like stages transform the output element type.
- Filter-like stages preserve the element type.
- Fold-like stages declare a separate accumulator/result type.

### Implementation areas

- Replace flat `SamjnaValueType` usage with a recursive type representation.
- Carry element types in `SanskritValue.Suchi` metadata or inferred flow state.
- Extend अन्तरतम matching to recursive types.
- Add collection-stage type inference and diagnostics.

### Acceptance criteria

- A numeric list cannot be passed to a text-list-only kriyā.
- Collection pipelines preserve or transform element types correctly.
- Untyped existing `सूची` declarations remain valid.

## 8. P2: IDEA signature assistance

### Goal

Use registered saṃjñā signatures to help authors construct correct calls.

### Planned features

- Parameter-name completion after a call begins.
- Expected-type hints beside positional and named arguments.
- Missing-argument quick fixes.
- Named-call templates generated from the selected signature.
- Navigation from a call to its definition.
- Find usages for public and अन्तरङ्ग definitions.
- Result-type and result-schema inlays.
- Completion filtered by अधिकार domain and visibility.

### Acceptance criteria

- Completion never suggests an inaccessible अन्तरङ्ग parameter/function.
- A generated named call parses and validates without manual restructuring.
- Navigation works across sibling `.pvm` files.
- Diagnostics underline the actual call operand rather than the first matching
  definition token.

## 9. P2: Exhaustiveness and reachability analysis

### Goal

Report control-flow mistakes that can be proven without executing the program.

### Planned checks

- Missing arms for closed structured result variants.
- Duplicate or unreachable pattern arms.
- Branch result-type incompatibility.
- Code after unconditional return.
- Functions whose declared result is not produced on every path.
- Loops with statically impossible conditions.
- Pipeline stages that can never accept the previous result.

### Acceptance criteria

- Diagnostics are stable and source-located in IDEA.
- The CLI can run the same validator independently of IDEA.
- Warnings do not reject backward-compatible dynamic code unless execution is
  necessarily invalid.

## 10. P3: Higher-order kriyā values

### Goal

Allow a reusable kriyā to be passed as data to collection and control-flow
algorithms.

Possible applications include typed map, filter, fold, retry, and validation
combinators.

### Open design questions

- Which grammatical form denotes a kriyā value without invoking it?
- How are input and result signatures represented in Sanskrit?
- How does capability checking compose across passed kriyās?
- Should captured values be allowed, and if so, are they immutable?

This item remains exploratory until explicit return semantics and recursive
types are stable.

## 11. P3: Structured destructuring

### Goal

Bind several fields from one structured value without repeated genitive access.

Possible form:

```pvm
अनुमानपरिणाम + मतुप् + ङस् अवस्था + अम् प्रयत्नसङ्ख्या + अम् च विवृ + लोट् + सिप् ।
```

The final syntax must avoid introducing unnecessary variables and must preserve
the declared field types.

## 12. P3: Explicit modules and imports

### Goal

Scale beyond implicit sibling-file discovery while keeping simple projects
simple.

Potential capabilities:

- Explicit grantha/module identity.
- Public export declarations.
- Qualified imports and aliases.
- Import-cycle diagnostics.
- Stable cross-module अधिकार and schema resolution.
- Separate compilation and cached module signatures.

Existing sibling `.pvm` discovery should remain the zero-configuration default.

## 13. Delivery sequence

The recommended implementation order is:

1. Optional/default parameters.
2. Explicit return expressions.
3. Structured result pattern matching.
4. Exhaustiveness and return-path analysis.
5. Generic collection signatures.
6. IDEA signature completion, navigation, and quick fixes.
7. Reassess higher-order kriyās, destructuring, and modules.

This order completes the ordinary function model before adding more advanced
type-system and composition features.

## 14. Testing strategy

Each enhancement should add tests at the following levels:

1. **Parser**: accepted and rejected surface grammar.
2. **AST**: typed node structure independent of source text heuristics.
3. **Binding**: case roles, named parameters, and typed operands.
4. **Runtime**: correct values, effects, scopes, and control signals.
5. **Validator**: early errors with precise source locations.
6. **IDEA plugin**: annotations, inlays, completion, and navigation.
7. **CLI**: complete `.pvm` files and interactive behavior.
8. **Compatibility**: all checked-in examples continue to execute.

The completion gate for every milestone is a passing full repository suite:

```sh
./gradlew test
```

## 15. Documentation requirements

When an enhancement is completed:

- update `docs/pvm-language-guide.md` with executable examples;
- move the item from planned to completed in this file;
- update `README.md` if it changes a major advertised capability;
- update `execution/README.md` for runtime architecture changes;
- update `idea-plugin/README.md` for authoring assistance;
- add or update a checked-in `.pvm` example;
- keep all implementation and example comments in English.
