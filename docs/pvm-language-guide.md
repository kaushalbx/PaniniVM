# Writing Programs in PaniniVM `.pvm` Files

This guide explains how to write executable PaniniVM programs using segmented
Sanskrit. It begins with individual sentences and builds toward input,
conditionals, loops, reusable typed kriyās, pipelines, structured results, and
multi-file projects.

For planned language additions and their proposed syntax, see the
[`PVM language enhancement plan`](pvm-language-enhancement-plan.md).

The examples describe syntax implemented by the current repository. Optional
parameters and explicit early-return statements are not yet part of the
language.

## 1. What a `.pvm` file contains

A `.pvm` file is a UTF-8 text file containing Sanskrit program sentences.
Declinable and conjugated words are written as grammatical segments joined by
`+`.

```pvm
द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
```

The sentence above supplies two accusative operands and commands the addition
action.

The execution path is:

```text
segmented Sanskrit → grammatical AST → kāraka binding → operation selection → runtime
```

PaniniVM does not treat a `.pvm` file as free-form surface Sanskrit. Case and
verbal suffixes are part of the program and carry executable meaning.

Copular comparisons retain the adjective's lexical meaning: `सम` uses an
instrumental standard for equality; `न्यून` and `अधिक` use an ablative standard
for numeric less-than and greater-than respectively. An unrelated adjective
is not silently treated as `न्यून`, and changing a runtime value does not
change the predicate identity.

Equality compares typed values, not their rendered spelling. Different words
for the same number compare equal; a text value is not equal to a numeric
value merely because they display alike. Structured values compare their
contents. A copular equality clause requires one subject value and one
standard value; a collection can be such a value, but coordination does not
implicitly mean “any matching pair.”

Locative collection membership uses the same typed equality. It requires one
list/group as the location and one subject value; a scalar location is an
error, not an implicit singleton collection.

See `examples/collections/membership.pvm` for present/absent member tests.
Evaluate such a present-tense proposition inside `यदि` or a loop condition;
as a standalone statement it is understood as a declaration, not a command
to execute an action.

## 2. Basic formatting

### 2.1 Segments

Place `+` between a prakṛti and each pratyaya:

```pvm
दशन् + शस्
मुद्र् + णिच् + लोट् + सिप्
```

Whitespace around `+` is recommended. It makes source readable and produces
better editor diagnostics.

Write a verbal root or its upadeśa before verbal suffixes, not an already
inflected verb or a derived noun. For example, use `स्था + णिच् + लोट् + सिप्`,
not `तिष्ठति + लोट् + सिप्` or `स्थानम् + लोट् + सिप्`. Source-root binding
does not accept historical surface aliases as roots; sanādi affixes remain
explicit parts of the derivation.

Root lookup does not choose between distinct executable lexical identities
by registration order. Supply distinguishing morphology, such as the matching
vikaraṇa, when required. An incompatible vikaraṇa is rejected rather than
ignored.

Multiple upasargas retain their source order. Readable generation composes
their boundaries from the verb outward, applying the supported consonant
sandhi at each boundary. For example,
`सम् + नि + क्षिप् + लोट् + सिप्` renders as `सन्निक्षिप`.
Homogeneous vowels at a prefix boundary also coalesce through the existing
savarṇa-dīrgha rule: `उप` followed by the completed form `आगच्छ` becomes
`उपागच्छ`. Other vowel combinations and root-specific exceptions are not yet
fully covered by this rendering helper.

Readable generation preserves unsupported feminine-suffix forms as segmented
source. In particular, it does not substitute `टाप्` for `डाप्` or `चाप्`.
Such retained source indicates an unresolved derivation, not a verified
surface Sanskrit form.

Failed nominal or verbal derivations retain their complete segmented source,
not a bare stem with its case, number, prefixes, or verbal suffixes discarded.
An incompatible explicit vikaraṇa likewise remains unresolved in readable
generation; it is not silently replaced by another gaṇa's form.

For स्था, ordinary and causative morphology now use distinct derivation
chains: `स्था + शप् + लोट् + सिप्` derives `तिष्ठ` through 7.3.78,
whereas `स्था + णिच् + लोट् + सिप्` derives `स्थापय` through raw पुक्
introduction (7.3.36), it-processing, and placement (1.1.46). The causative
renderer no longer substitutes a hardcoded स्थापय surface. These verified
स्था cases do not imply complete coverage of every root listed in either rule.

### 2.2 Sentence terminators

Use a single danda `।` to end an ordinary sentence:

```pvm
दशन् + शस् मुद्र् + णिच् + लोट् + सिप् ।
```

Use a double danda `॥` to close the final sentence of a reusable kriyā block:

```pvm
दर्शन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
सन्देश + अम् मुद्र् + णिच् + लोट् + सिप् ॥
```

Do not insert another danda immediately before `यदि`, `ततः`, `तर्हि`, or
`अन्यथा` when those words continue the same program expression. For example:

```pvm
# Correct: one conditional sentence
यदि फल + सुँ रहस्य + टा सम + सुँ असँ + लट् + तिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् ।
```

### 2.3 Comments

Lines beginning with `#` are comments. Project convention is to write comments
in English while keeping executable sentences in Sanskrit.

```pvm
# Print the computed result.
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

## 3. Grammatical roles used by programs

The case suffix is not decoration. It tells the binder how a value participates
in an action.

| Segmented ending | Case | Common programming role |
|---|---|---|
| `+ सुँ` | prathamā | subject, declaration name, or result reference |
| `+ अम्` | dvitīyā | input value or कर्मन् |
| `+ टा` | tṛtīyā | reusable kriyā used as an instrument with `कृ` |
| `+ ङे` | caturthī | recipient; older assignment frames |
| `+ ङसिँ` | pañcamī | source collection or lower range bound |
| `+ ङस्` | ṣaṣṭhī | domain, attribute owner, or collection whole |
| `+ ङि` | saptamī | location, stored-value destination, index, or named argument slot |

The exact surface produced by a suffix is derived through the Subanta engine;
source code keeps the segmented upadeśa form.

## 4. Values and built-in semantic types

PaniniVM carries values as typed `SanskritValue` objects. Common types are:

| Sanskrit declaration | Runtime meaning |
|---|---|
| `सङ्ख्या` | integer, rational, or numeric range |
| `शब्द` | text |
| `सूची` | ordered collection |
| `सत्य` | truth value |
| grammatically declared schema | structured value with typed fields |

Sanskrit number stems can be used directly:

```pvm
एक + अम्
द्वि + औट्
पञ्चन् + शस्
दशन् + शस्
```

The numeral engine evaluates and renders the semantic number; program logic
does not depend on manually maintained word-to-number tables.
Source-written counting numerals use their intrinsic number: `एक` takes
singular सुप्, `द्वि` takes dual सुप्, and counting adjectives from three
through nineteen take plural सुप्. Higher nominal quantities can use singular
सुप्: `विंशति + अम्` derives `विंशतिम्`, and `त्रिंशत् + अम्` derives
`त्रिंशतम्`. Plural quantities such as multiple hundreds are not prohibited.
A typed numeric value stored in a variable remains a single
program value and is not subject to this source-literal agreement check.

When a numeral immediately precedes a counted noun with the same case and
number, readable generation takes gender from that noun's lexical identity.
Thus masculine `द्वि` renders as `द्वौ`, while feminine or neuter `द्वि`
renders as `द्वे`. A standalone program numeral defaults to neuter because it
denotes the numeric value itself rather than an omitted masculine noun.
This default does not override lexical numeral-noun gender: primitive tens
such as विंशति and त्रिंशत् remain feminine, and शत remains neuter, in both
the renderer and `SankhyaGenerator.decline`. Compound higher-numeral gender
resolution now follows the canonical numeral expression's grammatical head.
Original alternative numeral-expression provenance remains under audit.

Segmented numeral constructions can include the existing ऊन and अधिक words:
`एक + ऊन + विंशति + अम्` denotes nineteen and renders `एकोनविंशतिम्`;
`द्वि + विंशति + अधिक + शत + अम्` denotes 122. Both constructions are
verified as assignment operands in interpreted and compiled execution. In
`SankhyaPada` rendering, the supplied construction determines the grammatical
head; it is not replaced by another expression with the same numeric value.
Generic normalized or decoded numeral nodes still need fuller provenance.

## 5. Actions, results, and output

### 5.1 Invoke an action

An imperative action normally uses `लोट् + सिप्`. Causative actions also use
`णिच्`.

```pvm
द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
```

Frequently used examples include:

```pvm
# Print a value.
पञ्चन् + शस् मुद्र् + णिच् + लोट् + सिप् ।

# Multiply two numbers.
त्रि + शस् द्वि + औट् च गण् + णिच् + लोट् + सिप् ।

# Compare two values.
त्रि + शस् द्वि + औट् च विद् + लोट् + सिप् ।
```

### 5.2 Refer to the latest result

`फल` refers to the current or most recent action result:

```pvm
द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

Results remain typed. A number is still a number when consumed by another
action or reusable kriyā.

### 5.3 Quote a command as data

Use `इति` when the preceding grammatical command should be printed or reported
rather than executed:

```pvm
सङ्ख्या + अम् ऊहँ + लोट् + थास् इति मुद्र् + णिच् + लोट् + सिप् ।
```

## 6. Direct result pipelines

`ततः` sends the typed result of one stage into the missing कर्मन् of the next
stage:

```pvm
त्रि + शस् द्वि + औट् च गण् + णिच् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
```

Pipelines may contain multiple stages:

```pvm
त्रि + शस् द्वि + औट् च गण् + णिच् + लोट् + सिप्
ततः द्वि + औट् च गण् + णिच् + लोट् + सिप्
ततः मुद्र् + लोट् + सिप् ।
```

The runtime transports semantic values rather than rendering and re-parsing
strings between stages.

Reusable procedures can form the same ordered pipeline. Put every procedure's
domain in ṣaṣṭhī, put the action noun in tṛtīyā to mark the means, and use
`ततः` to state the ordering:

```pvm
पञ्चन् + शस् द्वि + औट् च
गणित + ङस् गण + ल्युट् + टा
ततः गणित + ङस् वि + युज् + णिच् + ल्युट् + टा
डुकृञ् + उ + लोट् + सिप् ।
```

This renders as “पञ्च द्वे च गणितस्य गणनेन ततः गणितस्य वियोजनेन
कुरु।” The instrumental endings are semantic: they identify the procedure
stages, while `ततः` determines their execution order. The older
`पूर्वस्य परस्य एका कुरु` directive remains accepted only for compatibility.

## 7. Assignment and variables

Use causative `स्था` with a locative destination to retain a result:

```pvm
सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ततः फल + अम् रहस्य + ङि स्था + णिच् + लोट् + सिप् ।
```

Later sentences can consume the named value:

```pvm
रहस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

Prefer direct `फल` references and `ततः` pipelines when a value is used only
once. Introduce a name when the value must survive across several sentences.

### 7.1 Collection membership

Express membership as an ordinary existential clause: the sought member is
nominative and the collection is locative.

```pvm
यदि द्वि + सुँ सूची + ङि असँ + लट् + तिप्
    तर्हि सत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप्
    अन्यथा असत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप् ।
```

This means “if two is in the list.” The
nominative identifies what exists; the locative identifies where it exists.
A standalone present-tense existential is treated as an assertion. Put it in a
`यदि` or `यावत्` clause when its truth value controls execution. The older
accusative-list/instrumental-member frame remains accepted for compatibility.

### 7.2 Collection concatenation

Join two collections with causative `सम् + युज्`. The collection being
extended is accusative, while the collection joined with it is instrumental:

```pvm
पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।
```

This renders as `पूर्वसूचीम् उत्तरसूच्या संयोजय`—“join the first list with the
second list.” The upasarga `सम्` supplies the “together” sense and distinguishes
collection joining from bare arithmetic `युज्`. The instrumental participant
is resolved as the expressed secondary agent of the causative construction;
the runtime and compiler consume that kāraka binding rather than its position.
The older `सृज्` frame with a dative second collection remains accepted only
for compatibility.

### 7.3 Collection cardinality

Count the members of a collection with ordinary transitive `गण्`:

```pvm
सूची + अम् गण् + णिच् + लोट् + सिप् ।
```

This renders as `सूचीं गणय`—“count the list.” The accusative collection is the
object being counted. Its resolved `कर्मन्` binding supplies the collection to
both the interpreter action and the compiler's direct length instruction; the
meaning does not depend on the variable name or operand position.

### 7.4 Collection slicing

Take an inclusive portion of a collection by expressing the collection as a
genitive whole and its ordinal limits with `पर्यन्तम्`:

```pvm
सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम्
    अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

This renders as `सूच्याः द्वितीयात् तृतीयपर्यन्तम् अंशं गृहाण`—“take the
portion of the list from the second through the third.” The genitive identifies
the whole, the ablative ordinal identifies the inclusive source boundary, and
segmented `परि + अन्त + अम्` licenses the inclusive upper boundary. These
roles become a `CollectionSlice` semantic node consumed identically by the
interpreter and compiler. The older instrumental-start/dative-end `भज्` frame
is retained only for compatibility.

Natural indexing and slicing require a single source collection. Both `सूची`
and `गण` runtime collections expose their members; a scalar source produces a
value error rather than being treated as a one-element collection.
Each index or slice boundary must resolve to exactly one numeric value.
Coordinated or mixed operands are rejected instead of selecting the first
number and silently discarding the remaining words.
Slice bounds use inclusive one-based positions. Current slicing clips a start
below one to the first member and an end beyond the collection to its length;
an empty or reversed clipped interval returns an empty collection. Bounds
outside the supported signed 32-bit position range produce a value error.

### 7.5 One-based indexed retrieval

Take a value from an ablative source collection at a locative position:

```pvm
सूची + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

The ordinal morphology `द्वि + तीय` supplies position two. Positions start at
one; there is no implicit zero-based offset in the source. A named numeric
position can replace the ordinal, as in `क्रमाङ्क + ङि`. An index below one
or beyond the collection length produces a value error. Unlike slicing,
single-member retrieval does not clip an invalid position.

Run the complete example at
[`examples/collections/ordinal_index.pvm`](../examples/collections/ordinal_index.pvm).
It gathers three numbers and prints the second member.

### 7.6 Final member extraction

Express the collection as the genitive whole and its final member as the
accusative object:

```pvm
सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ।
```

This selects the last value of the collection through a `CollectionExtraction`
semantic frame shared by runtime execution and compiler lowering. The stored
collection is unchanged. An empty collection produces a value error; selectors
other than `अन्तिम` are currently rejected. The older accusative-list frame
remains available for compatibility.
The genitive whole must resolve to exactly one collection; a scalar is not
implicitly treated as a single-member list.

Readable generation produces `सूच्याः अन्तिमम् उद्धर ।`. At the prefix
boundary it chooses the assimilated variant licensed by
[Aṣṭādhyāyī 8.4.62](https://sanskritlibrary.org/grammatical/data/A.8.4.62.html):
`ह्` after a stop may take the corresponding fourth consonant of that class.
The underlying segmented source remains `उद् + हृ`.

## 8. Input and validation

Use `ग्रह्` to request input. A typed number request places the `सङ्ख्या`
marker in the declaration:

```pvm
निवेश + अम् सङ्ख्या + टा ग्रहँ + श्ना + लोट् + सिप् ।
```

The CLI waits for input and validates it before continuing. ASCII digits and
Devanagari digits are accepted for numeric input. Enter `:cancel` to cancel an
interactive request.

Unqualified input returns a `शब्द` value even when its characters look numeric.
For example, `००७` remains that exact text rather than becoming the integer
seven. Choice input likewise preserves the declared choice as text. Declare
numeric input explicitly when subsequent computation needs a number.
Conflicting type declarations produce a value error before requesting input;
the action does not choose between number, truth, choice, and text by priority.
Type markers are recognized from retained nominal declaration identities,
not the rendered values of variables with those names. Allowed choice members
still resolve through the environment as data.
Every declared choice reference must resolve; a missing member is an error
even when other choices are valid.

### 8.1 Scoped numeric range

Declare one inclusive range with a pañcamī starting point and an accusative
`पर्यन्तम्` boundary:

```pvm
एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।
```

The active range can be reused by random selection, numeric input validation,
and dynamically rendered instructions:

```pvm
सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।
निवेश + अम् सङ्ख्या + टा ग्रहँ + श्ना + लोट् + सिप् ।
```

No separate lower-bound and upper-bound variables are required. Numeric bounds
apply only to numeric input; an active range does not constrain text input.
The input action also validates the host's returned value against the declared
type, allowed choices, and numeric range. A host that omits prompt validation
cannot bypass these program constraints. Reversed explicit bounds produce a
language value error rather than an uncaught constructor exception.
An explicitly supplied numeric input bound must resolve to exactly one number.
Missing references, text values, and coordinated bounds produce a value error;
only an omitted bound may inherit the active range.

## 9. Conditionals

Use `यदि … तर्हि … अन्यथा …`:

```pvm
यदि फल + सुँ रहस्य + टा सम + सुँ असँ + लट् + तिप्
तर्हि जय + अम् मुद्र् + लोट् + सिप्
अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
```

Nested alternatives are supported:

```pvm
यदि फल + सुँ रहस्य + टा सम + सुँ असँ + लट् + तिप्
तर्हि विजय + सुँ
अन्यथा यदि फल + सुँ रहस्य + ङसिँ न्यून + सुँ असँ + लट् + तिप्
तर्हि लघु
अन्यथा गुरु
ततः मुद्र् + णिच् + लोट् + सिप् ।
```

Bare branch values such as `लघु` and `गुरु` are lowered to direct result values.
The shared pipeline target executes only for the selected branch.

## 10. Repetition and condition-controlled loops

### 10.1 Fixed repetition

The segmented suffix `कृत्वसुच्` supplies a repetition count:

```pvm
पञ्चन् + कृत्वसुच् प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।
```

### 10.2 Condition-controlled loop

Use `यावत् … तावत्` to continue while a condition holds. A preceding `कृत्वसुच्`
count places a safety bound on the loop:

```pvm
पञ्चन् + कृत्वसुच्
यावत् विजय + सुँ न भू + लट् + तिप्
तावत् प्रयत्न + टा डुकृञ् + उ + लोट् + सिप्
अन्यथा प्रयत्न + आम् समाप्ति + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

`अन्यथा` is the exhaustion branch. It runs only when the bounded loop consumes
all attempts. Runtime loop termination uses the typed truth result; it does not
require a separate break action.

The loop automatically publishes a structured `परिणाम` with:

- `अवस्था`: `विजय` or `समाप्ति`;
- `प्रयत्नसङ्ख्या`: the number of completed iterations.

## 11. Reusable first-class Sanskrit kriyās

### 11.1 Basic definition

A grammatical `प्रक्रिया` declaration opens a reusable prakriyā. The final body sentence ends
with `॥`:

```pvm
प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
निवेश + अम् सङ्ख्या + टा ग्रहँ + श्ना + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
```

Invoke the declared name in the instrumental with `कृ`. An ordinary noun
name takes its case suffix directly; do not add `ल्युट्` to it:

```pvm
प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।
```

Derived action nouns still use `ल्युट्` on a verbal root, for example
`गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप्` and the
matching call `गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप्`.
Keep the same nominal identity in the declaration and call. A bare
`… + ल्युट् + सुँ ।` is a nominal statement, not a procedure declaration in
the default native document parser. Use the explicit `इति प्रक्रिया … अस्ति`
declaration. Parsing follows grammatical statement delimiters, not physical
lines, so a declaration or body statement may span several lines.
For migration tooling only, `PvmScript.parseLegacy` and
`PrakriyaScriptValidator.validateLegacy` inspect old bare-header blocks; the
latter suggests the explicit declaration without changing its name or calls.
Execution never silently falls back to legacy parsing on a native parse error.

### 11.2 Typed named parameters and result

Place signature declarations at the beginning of the block:

```pvm
योजन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
सङ्ख्या + सुँ इति परिणाम + सुँ ।
वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
```

Signature declarations describe the kriyā and are not executed. Parameter
names can be used directly in the body. The last successful body result is the
function result and is checked against the declaration.

Supported declared parameter/result types are:

```text
सङ्ख्या   शब्द   सूची
```

### 11.3 Positional call

Accusative arguments bind in declaration order:

```pvm
द्वि + औट् त्रि + शस् च योजन + टा डुकृञ् + उ + लोट् + सिप् ।
```

The older ordinal references `प्रथम`, `द्वितीय`, and so on remain supported for
untyped and migrated definitions.

### 11.4 Named call

For a named argument, place the parameter slot in saptamī and immediately
follow it with its value in dvitīyā. Thus `दक्षिणे त्रि` means “three in the
right-hand slot”:

```pvm
दक्षिण + ङि त्रि + शस्
वाम + ङि द्वि + औट्
योजन + टा डुकृञ् + उ + लोट् + सिप् ।
```

Named arguments may appear in any order. One call must be entirely positional
or entirely named. The validator reports unknown, duplicate, missing, mixed,
and incorrectly typed arguments. The older ṣaṣṭhī label remains accepted only
for source compatibility.

### 11.5 Scope and nested calls

Each invocation receives an isolated child environment. It can read caller
values but does not leak temporary body values back into the caller. A body may
invoke another registered prakriyā.

Qualify `प्रक्रिया` with the segmented feminine adjective `अन्तरङ्गा`
to make the definition file-private:

```pvm
गण + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
प्रथम + अम् द्वि + औट् च गण् + णिच् + लोट् + सिप् ॥
```

### 11.6 Domains and overloads

An अधिकार declaration governs following definitions:

```pvm
गणित + सुँ इति अधिकार + सुँ ।
```

A definition can also carry the domain explicitly:

```pvm
गणित + ङस् योजन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
...
```

Typed overloads are selected by अन्तरतम compatibility. `अपवाद`, `अन्तरङ्ग`,
and `नित्य` qualifiers participate in precedence. A definition named with the
`क्त` identity is memoized by its arguments.

## 12. Structured values and result schemas

### 12.1 Assert and access fields of a मतुप् possessor

Each field is introduced by an ordinary copular assertion: the possessor is in
ṣaṣṭhī, the field is the nominative subject, and its value is the nominative
predicate. Repeated assertions about the same possessor form one structure:

```pvm
गुण + मतुप् + ङस् मूल्य + सुँ दशन् + जस् असँ + लट् + तिप् ।
गुण + मतुप् + ङस् परिमाण + सुँ पञ्चन् + जस् असँ + लट् + तिप् ।
```

Readable Sanskrit: `गुणवतः मूल्यं दश अस्ति। गुणवतः परिमाणं पञ्च अस्ति।`
There is no alternating positional value/name list.

Use ṣaṣṭhī to identify the possessor and an accusative field as the object of
`ग्रह्` (“obtain”):

```pvm
गुण + मतुप् + ङस् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

Readable Sanskrit: `गुणवतः मूल्यं गृहाण।`

Nested genitive access is also supported.

### 12.2 Declare a result schema

A schema declaration states that coordinated nominative subjects are plural
`क्षेत्र` (“fields”) belonging to a genitive schema. The schema name is an
ordinary identifier; its spelling has no hidden suffix convention:

```pvm
अवस्था + सुँ प्रयत्नसङ्ख्या + सुँ च अनुमानपरिणाम + ङस् क्षेत्र + जस् असँ + लट् + झि ।
```

Readable Sanskrit: `अवस्था प्रयत्नसङ्ख्या च अनुमानपरिणामस्य क्षेत्राः सन्ति।`

A typed kriyā can declare that schema as its result:

```pvm
अनुमान + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
अनुमानपरिणाम + सुँ इति परिणाम + सुँ ।
...
```

The runtime validates the schema name and required field set. Structured
`SanskritValue.Rupa` values preserve the types of their fields through calls,
pipelines, compiled semantic codecs, and persisted state.

### 12.3 Read the automatic loop result

```pvm
परिणाम + मतुप् + ङस् अवस्था + अम् ग्रहँ + श्ना + लोट् + सिप् ।
परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

These accesses do not require copying the fields into temporary variables.

## 13. Multi-file projects

Place related `.pvm` files in one project directory. When an entry file is
evaluated, PaniniVM loads reusable definitions from sibling `.pvm` files before
executing the entry file.

Typical layout:

```text
my-project/
├── ganita.pvm       # reusable public and अन्तरङ्ग definitions
└── mukhya.pvm       # entry-point sentences
```

Public definitions are available across files. `अन्तरङ्गा` definitions remain
visible only to calls originating in their defining file. An entry-point
`अपवाद` definition can override a library default according to prakriyā
precedence.

See these checked-in examples:

- `projects/multifile/`
- `projects/private_scope/`
- `projects/adhikara_domain/`
- `projects/taddhita_inheritance/`

## 14. Running a `.pvm` program

Build the direct launcher:

```sh
./gradlew :cli:installDist
```

Run a file:

```sh
./cli/build/install/cli/bin/cli --eval path/to/mukhya.pvm
```

On Windows, use:

```powershell
.\gradlew.bat :cli:installDist
.\cli\build\install\cli\bin\cli.bat --eval projects\number-guessing-game\number_guessing_game.pvm
```

Rebuild `:cli:installDist` after changing the source. `:cli:run` uses the
current build, but does not refresh the libraries copied into the installed
launcher directory.

Compile a file to a JVM class:

```sh
./cli/build/install/cli/bin/cli --compile path/to/mukhya.pvm ProgramName
```

The interactive REPL is useful for one complete utterance at a time. Save
multi-sentence definitions and loops in a `.pvm` file.

## 15. IDEA plugin support

The IDEA plugin provides `.pvm` syntax highlighting, run actions, and live
diagnostics. For typed prakriyās it reports:

- duplicate parameter or result declarations;
- unknown, missing, or duplicate named arguments;
- arity and type mismatches;
- incompatible typed pipeline stages;
- missing structured result schemas.

It also warns when a completed numeral surface is placed before `+` as though
it were a segmented prātipadika. The quick fix replaces it with the canonical
stem without rejecting older source:

```pvm
# Warning and quick fix: पञ्च -> पञ्चन्
पञ्च + शस्

# Canonical segmented source
पञ्चन् + शस्
```

Compound numerals retain their canonical component identities during
derivation. Thus `पञ्चन्` and `दशन्` remain identifiable beneath `पञ्चदश`.
Lexicalized `षोडश` remains the correct visible form while retaining the
component identity `षष् + दशन्`; it is not produced by an invented general
sandhi rule.

Within a `ततः` pipeline, assignment consumes the immediately preceding typed
result directly:

```pvm
पूर्व + अम् वर्तमान + अम् च युज् + णिच् + लोट् + सिप्
ततः उत्तर + ङे दा + लोट् + सिप् ।
```

The IDEA plugin warns about the longer `क्रिया + ल्युट् + ङस् फल + अम्`
lookup in this position and can replace it with the direct assignment. Local
bindings updated inside a `कृत्वसुच्` body are carried into the next iteration,
so variables such as Fibonacci state and collection accumulators need no
special loop-only syntax.

For random selection from an active range, state exclusion explicitly with the
absolutive `वर्जयित्वा`. This selects from `सीमा` after removing values in
`क्रम`:

```pvm
क्रम + अम् वृज् + णिच् + क्त्वा सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।
```

Keep segments spaced consistently so the highlighted range identifies the
intended operand precisely.

## 16. Common errors

### `no viable alternative at input '।यदि'`

A danda ended the sentence before `यदि`. Remove that danda if the conditional
continues the same expression.

### A danda appears between pipeline stages

Keep `ततः` inside one sentence and place the danda only after the final stage.

### A reusable kriyā body executes as top-level code

Ensure the final body sentence ends with `॥`. A single `।` does not close the
definition block.

### Wrong number or type of arguments

Compare the call with the `… इति मान + सुँ` declarations. In a named call, each
saptamī parameter slot must be immediately followed by one dvitīyā value.

### A structured result is rejected

Confirm that the schema was declared before use with the plural
`…स्य क्षेत्राः सन्ति` frame and that the returned field names exactly match
its declaration.

### Output appears after an input prompt unexpectedly

Use the direct installed CLI launcher. Console output is flushed immediately,
but Gradle-run stdin buffering can make interactive ordering less clear on some
platforms.

## 17. Complete number-guessing example

The repository contains a complete interactive program at:

```text
projects/number-guessing-game/number_guessing_game.pvm
```

It demonstrates a scoped range, random selection, validated input, assignment,
nested conditionals, a reusable attempt kriyā, direct result pipelines, a
bounded condition-controlled loop, exhaustion handling, and automatic
structured loop results.
