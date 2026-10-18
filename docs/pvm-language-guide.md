# Writing Programs in PaniniVM `.pvm` Files

This guide explains how to write executable PaniniVM programs using segmented
Sanskrit. It begins with individual sentences and builds toward input,
conditionals, loops, reusable typed kriyās, pipelines, structured results, and
multi-file projects.

For planned language additions and their proposed syntax, see the
[`PVM language enhancement plan`](pvm-language-enhancement-plan.md).

The examples describe syntax implemented by the current repository. Optional
parameters are not yet part of the language. Explicit procedure results use a
nominative `फल … अस्ति` predication inside a procedure, as described below.

Ordinal results use a distinct `SanskritValue.Purana` (पूरण) value: for example,
`द्वि + तीय` represents position two, not the cardinal number two. Positions are
positive and one-based. A productive ordinal used as the value in a procedure's
`फल … अस्ति` statement remains an ordinal through execution, persistence, and
JVM compilation. Numeric arithmetic does not implicitly accept this value type.
Collection-member selectors and productive ordinal action operands retain this
type. List indexing explicitly accepts a cardinal index or an ordinal position;
this does not make ordinals arithmetic operands. Remaining older numeric
fallbacks are tracked in the natural-language roadmap.

Procedure arguments also preserve ordinal values. An unbound lexical ordinal
such as `द्वितीय` cannot satisfy a cardinal-number parameter. A lexical name may
still resolve to an existing value of that parameter's type; productive ordinal
literals such as `द्वि + तीय` remain ordinal regardless of same-spelled names.

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
segmented Sanskrit
    → grammatical AST and occurrence evidence
    → kāraka candidates and typed semantic operands
    → operation-specific resolution
    → resolved semantic program
    → interpreter or JVM compiler
```

The grammatical AST is parsed once and retained as evidence. Syncretic endings
remain candidates until the selected operation can resolve them; the binder does
not discard an otherwise valid interpretation merely because another operation
uses a different kāraka. Literal values, named references, action history, member
selectors, and procedure arguments cross this boundary as typed semantic
operands. Unknown references remain symbolic constraints during planning instead
of being replaced with invented numbers, words, or collections. A diagnostic is
blocking only when its structured severity says execution would be unsafe.

PaniniVM does not treat a `.pvm` file as free-form surface Sanskrit. Case and
verbal suffixes are part of the program and carry executable meaning.

Copular comparisons retain the adjective's lexical meaning: `सम` uses an
instrumental standard for equality; `न्यून` and `अधिक` use an ablative standard
for numeric less-than and greater-than respectively. An unrelated adjective
is not silently treated as `न्यून`, and changing a runtime value does not
change the predicate identity.
The three nominal participants may occur in any order: their cases, not
adjacency, determine subject, standard, and predicate. This is supported for these
copular comparison frames; it is not a claim of unrestricted sentence understanding.

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

For a `णिच्` causative in `लट्` or `लोट्`, explicit `शप्` belongs to the
derived stem, not to the original root's gaṇa. Thus
`एक + अम् द्वि + अम् च युज् + णिच् + शप् + लोट् + सिप् ।`
has the same addition semantics as the form with implicit vikaraṇa. This
does not yet provide general sanādi/vikaraṇa compatibility validation.
Explicit non-`शप्` affixes on this single-`णिच्`, `लट्`/`लोट्`, parasmaipada
construction are rejected rather than selecting the base root's gaṇa.
Passive forms and other derivational combinations require separate analysis.
Readable generation uses the same lexical-root constraint: explicit causative
`शप्` does not prevent a supported non-bhvādi root from rendering.

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

### 2.4 Multiple ways to express the same goal

PVM deliberately supports more than one grammatical route for several common
tasks. Choose the form that makes the intended relation clearest; these are
supported alternatives, not a license to ignore case or agreement.

| Goal | Concise form | More explicit or reusable form |
|---|---|---|
| Use the preceding result | `ततः` supplies the missing object of the next stage | Write `फल` as the next action's object |
| Use an older result | Qualify `फल` with `पूर्व` or an ordinal | Assign the result to a named location and use that name later |
| Print a preceding result | Use an objectless display stage after `ततः` or a prior action | Write `फल + अम्` explicitly |
| Supply procedure arguments | Place values positionally in declaration order | Pair each locative parameter name with its accusative value |
| Feed a procedure from a pipeline | Let the prior result fill one missing parameter | Supply all arguments explicitly at the call site |
| Name a procedure | Use `इति प्रक्रिया … अस्ति` | Use the preferred `नाम प्रक्रिया … अस्ति` form |
| Name a list declaration | Put a nominative name before `इति` | Put the nominative name immediately before `नाम` inside the declaration |
| Read a collection member | Use a genitive whole with an agreeing ordinal object | Use an ablative source with a locative numeric or ordinal position |
| Build a collection | Use a copular `… सूची अस्ति` declaration | Request collection formation with the supported `सम् + ग्रहँ` action |

For example, both of these print the immediately preceding sum:

```pvm
द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।

द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

The first is one pipeline; the second is two sentences joined through discourse
history. Likewise, positional and named procedure calls can compute the same
value, but the named form is clearer when a procedure has several parameters.
Compatibility forms documented later remain accepted, while the natural forms
shown first in each section are preferred for new programs.

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

Named operands retain their referents through semantic serialization and compiler
planning, independently of the current payload. A literal text value does not
become a reference merely because its spelling matches a declared name.

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

An ordered list can also be declared directly by naming its members in ṣaṣṭhī:

```pvm
एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।
```

“एकस्य द्वयोः त्रयाणां च सूची अस्ति।” This creates and stores `[1, 2, 3]`
under `सूची`, preserving member order and duplicates. A single member still
produces a list. The interpreter and JVM compiler share the same typed AST
lowering; no rendered Sanskrit is reparsed to recover the members. `च` only
coordinates members: the explicit `सूची … अस्ति` construction creates the list.
Use `सम् + ग्रहँ` when gathering is itself the requested action. Existing indexing,
printing, and collection operations can consume the declared list.

To explicitly constrain the members to numbers, add the genitive plural type
qualifier `सङ्ख्या + आम्`:

```pvm
एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च
सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
```

PVM reads the coordinated numerals as named values and `सङ्ख्यानाम्` as their
member-type qualifier. This construction creates `[1, 2, 3, 4]`, not four
unspecified numbers. That disambiguation is part of PVM semantics. Word members
are rejected; numeric values retain their actual types rather than being parsed
from display text. The constraint travels with the list through storage, history,
semantic codecs, persistence, reverse, slicing, filtering, flattening, insertion,
and concatenation. Incompatible insertion or concatenation fails. Mapping creates
a new general list because the mapped result may have a different member type.
The constraint belongs to the list value, not a permanently typed variable slot.
A bare `सङ्ख्यानां सूची अस्ति` does not silently create an empty list.

The same grammatical construction supports a word/text member type:

```pvm
राम + ङस् सीता + ङस् च शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।
```

`शब्दानाम्` requires actual word/text values, not numbers whose display happens
to be a word. Declared values still resolve existing bindings before falling
back to literal words. The shared typed-list backend handles both member types;
conflicting declared types cannot be concatenated, even for empty lists.
The currently supported declaration uses present third-person singular `अस्ति`
to agree with singular `सूची`. Disagreeing verbs and duplicate or conflicting
member-type qualifiers are diagnosed rather than silently reinterpreted.

A nominal `इति` naming clause can give the declared list its own referent:

```pvm
क्रम + सुँ इति एक + ङस् द्वि + ओस् च
सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
क्रम + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

The quoted name is one nominative singular nominal. The list is stored as `क्रम`;
subsequent inflected references retain the same nominal identity.
Both backends share this structural lowering. This does not convert quoted
commands into names or reinterpret arbitrary reporting clauses as assignments.
Declared referent gender/agreement beyond this form and “सूच्याः सर्वाः सङ्ख्याः”
quantification remain pending.

To select an ordinal member, express the collection as a genitive whole and
make the ordinal agree with the singular accusative object:

```text
क्रम + ङस् प्रथम + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।
```

Here `प्रथमम्` modifies `मूल्यम्`; it is not a second object or a locative
programming index. The same construction accepts `द्वि + तीय + अम्`. This
currently supports the singular मूल्य object with ग्रह्; it is not a general
adjective-agreement implementation.
Within this single-object construction, the relation does not depend on
adjacency: `मूल्य + अम् क्रम + ङस् प्रथम + अम् ग्रहँ + श्ना + लोट् + सिप्`
selects the same member. Multiple ordinal modifiers are an ambiguous request
and are rejected rather than resolved by nearest-word order.
Feminine ordinal stems such as `प्रथमा` and explicit feminine affixes such as
`प्रथम + टाप्` cannot qualify neuter `मूल्य` in this construction, even when
their case and number endings otherwise match.

Literal and reference identity are distinct. Recognized cardinal numerals and
truth literals keep their grammatical values even if a host environment contains
the same spelling. Ordinal words can name stored values outside a member-selection
construction; within genitive-whole extraction, selector morphology takes
precedence over a same-named host binding. This does not relax adjective agreement
when an explicit member noun is present.

Prefer the indeclinable naming particle `नाम` when the name belongs after the
members, within the declaration itself:

```pvm
एक + ङस् द्वि + ओस् त्रि + आम् च क्रम + सुँ नाम
सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।
```

`नाम` follows a nominative singular name; it is an avyaya, not a shortened form
of the inflected noun `नामन्`. The members and their type qualifier retain their
existing grammatical roles. A declaration supplies one name, not both an इति
name and a नाम name. Both forms retain the most recently declared list under
the discourse noun `सूची`, so `क्रमस्य` and a later `सूच्याः` can access that
declaration after intervening printing or arithmetic. A subsequent declaration
updates `सूची` while preserving earlier explicit names. This is a retained list
value, not a permanent alias that follows later unrelated reassignment of a name.

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
If a stage explicitly names `फल`, that reference supplies the preceding result
once; an additional implicit copy is not injected, including after a conditional.

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

### 6.1 Prior-action clauses: क्त्वा and ल्यप्

An earlier action with the same agent can precede the main finite command:

```pvm
एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

This renders with `योजयित्वा … मुद्रय`: having combined one and two,
print the result. The earlier action owns the operands before its derived
verb; the main action owns the following operands. Multiple prior actions
are executed in source order. `फल` explicitly refers to the preceding result;
क्त्वा itself does not inject an operand into the following action.

Within a prior-action chain, an otherwise objectless मुद्र् command resolves its
omitted object to the immediately preceding result. Thus `चिञ् + क्त्वा मुद्र् +
णिच् + लोट् + सिप्` can mean “having chosen, print it.” Explicit objects take
precedence; independent verbs do not acquire an operand merely from क्त्वा.

A kṛdanta genitive names the action whose result is wanted. In supported compiler
leaves, conditionals, and loop conditions, `युज् + ल्युट् + ङस् फल + अम्` selects the latest completed
युज् action, while `युज् + ल्युट् + ङस् पूर्व + अम् फल + अम्` selects the preceding
matching action. Intervening printing does not change this selection. The
ordinal `प्रथम + अम्` selects the first matching result and `द्वि + तीय + अम्`
the second, in chronological order. Typed lexical ordinals such as `तृतीय + अम्`
also select the corresponding chronological result. An ordinal reference stays
fixed even when newer matching actions execute. Selection includes earlier discourse and preceding clauses in the
current utterance, rather than restarting the ordinal at each sentence boundary.
In conditions the qualifier agrees with the case of फल, for example
`प्रथम + सुँ फल + सुँ`.
Case or number disagreement between an ordering qualifier and फल is rejected.
Conditions can compare independently selected results, for example
`युज् + ल्युट् + ङस् प्रथम + सुँ फल + सुँ युज् + ल्युट् + ङस् द्वि + तीय + ङसिँ फल + ङसिँ न्यून + सुँ असँ + लट् + तिप्`:
the first addition result is less than the second. Each reference keeps its own
ordering and case relation in both backends. History is read only when that
condition is reached; an unselected अन्यथा यदि does not require its antecedents.
An unavailable result in a reached condition is an error, not a false comparison.
Ordinary arithmetic can also consume multiple explicitly named results: each
result keeps its own qualifier and kāraka relation, including within ततः chains.
For a single named-result reference, a unique ordering modifier need not be
immediately before फल. For example, these select the same first completed
युज् result:

```text
प्रथम + अम् युज् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
युज् + ल्युट् + ङस् फल + अम् प्रथम + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

This applies to the unambiguous single-reference frame. It does not infer
arbitrary attachment among multiple objects or named results.

Named-history references can also supply a प्रक्रिया argument, positionally or
after a locative parameter slot: `मान + ङि युज् + ल्युट् + ङस् फल + अम्`.
The genitive qualifies फल; it is not a parameter label. Several procedure
arguments may each carry their own named-result reference and ordering qualifier.
Inside a procedure body, an ordinal qualifying a named फल remains a history
selector; it is not rebound as a positional procedure parameter.
Interpreted procedure overloads rank named-result arguments by their resolved
value type. Equivalent compiled overload dispatch remains incomplete.
The compiler does preserve distinct named-result occurrences in procedure
arguments and supports named-result display and comparison conditions, including
ordered references such as `पूर्व` and productive ordinals. Named history
conditions are reevaluated before each loop iteration; they are not the bare फल
truth shorthand.
An unavailable named result is an error: a missing previous or ordinal result
does not fall back to the latest result of another action.

An unqualified action noun such as `युज् + ल्युट्` searches completed actions of
that root family. Explicit prefixes or sanādi affixes make the reference exact:
`युज् + णिच् + ल्युट् + ङस् फल + अम्` selects a completed causative action,
not a later plain-root action. `वि + युज् + णिच् + ल्युट्` retains both the
prefix and causative affix. This distinction applies to result and participant
references in both backends. Ordering is applied after identity filtering: a
second or previous qualified action must actually exist. Root-only compatibility
history cannot supply a missing qualified match.

Interpreted discourse can also refer to a remembered action's participants:
`युज् + ल्युट् + ङस् पूर्व + अम् कर्मन् + अम्` selects the previous matching
action's object participants, not its computed result. Ordering modifiers must
agree with the kāraka reference in case and number. A missing action or absent
participant relation is an error, not a literal value named कर्मन्.
Inside a procedure, the ordinal in this construction still selects
the remembered action; it does not refer to the procedure's positional argument.
Compiled display commands support accusative kāraka-history references, including
multiple ordered references and mixed ordinary/history object operands in written
order. Assignment also accepts these remembered participants:

```pvm
एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
युज् + ल्युट् + ङस् कर्मन् + अम् सूची + ङे दा + लोट् + सिप् ।
सूची + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

This assigns the two inputs, not their sum: the list is `[एक, एक]`. A sole
list-valued participant stays a list without another wrapping layer or member
expansion. Ordinary objects can appear alongside remembered groups; written
operand order is preserved. The assignment's remembered कर्म contains its actual
inputs, not one collection fabricated from its result.

Compiled numeric-fold actions also accept remembered participants alongside
written operands:

```pvm
एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
युज् + ल्युट् + ङस् कर्मन् + अम् नवन् + शस् च युज् + णिच् + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।
```

This adds the original inputs `1, 2` to `9`, producing `12`, rather than adding
the earlier result as a single operand. Numeric folds preserve written order
(important for subtraction and division), validate the expanded operand count,
and reject collection-valued participants instead of flattening them into numbers.
Bare accusative history groups also work with numeric/counting verbs, without
another written operand. Their actual participant shape selects the operation
at runtime: several numeric objects can select multiplication under गण, whereas
one list-valued object selects list counting without flattening that object.
Mixed remembered/written objects likewise retain operation selection when the
verb has counting alternatives. Numeric signatures require number payloads;
collections do not become numeric operands merely by inheriting सङ्ख्या annotations.
Multiple coordinated history groups and explicit dative result destinations are
supported for these verbs. Destination writes follow the selected operation's
result-binding contract and update procedure-local slots when present. Completed
history captures the original destination participant before it is overwritten.
Other verbs and condition queries remain explicitly unsupported. Participant
history is recorded for resolved ordinary leaves; specialized action paths are
still incomplete.
The supported compiled display form also works inside procedure bodies and
conditional branches; unexecuted branches do not evaluate their history loads.

For a prefixed prior action, use ल्यप्. For example, subtract one from two,
then print the result:

```pvm
द्वि + औट् एक + अम् च वि + युज् + णिच् + ल्यप्
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

ततः remains supported and can follow a main command in the same utterance.
Both interpreter and compiler share prior-action lowering. Different explicit
agents are rejected; passive/bhāve main clauses are not yet supported in this
construction. The existing range-exclusion construction remains specialized.
The main command can invoke a reusable प्रक्रिया; prior actions execute before
that call, without supplying an implicit procedure argument through क्त्वा.
सम् + युज् currently has numeric/collection overload ambiguity and should not
be assumed to resolve as numeric addition.

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

The natural joining frame requires exactly one collection in each role; a scalar
is not silently wrapped into a singleton list. Declared member constraints are
retained and validated, including when a collection is empty. Compatibility
coordination preserves every supplied collection in order rather than discarding
later operands, and cannot bypass those constraints.
Gathering, insertion, and joining require every supplied coordinated participant
to resolve. A missing reference is an error, not an omitted member or a successful
no-op insertion. An explicitly supplied empty list is still a valid collection.
Assignment uses the same complete-resolution rule: every coordinated value must
resolve before a result is produced. This also applies to nested coordination;
an explicitly supplied empty typed list remains a valid value to store.
Display also requires every supplied object reference to resolve; it does not
silently omit missing objects and print only the remaining coordinated values.

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

### 7.7 Summing number members

For member summation, make the collection/member relation explicit:

```text
सूची + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ।
```

The genitive supplies the list; plural accusative सङ्ख्या denotes its number
members. This produces their sum, not collection concatenation. Both backends
require numeric members and preserve the list. Derived member nouns are not
silently interpreted as the plain सङ्ख्या selector. See
`examples/collections/member_sum.pvm` for a declaration and display chain.
The same member frame works before a display verb in a prior-action chain:

```text
सूची + ङस् सङ्ख्या + शस् युज् + णिच् + क्त्वा
मुद्र् + णिच् + लोट् + सिप् ।
```

Here the omitted display object refers to the preceding sum. The explicit
finite `ततः` form remains supported too.

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
These are nominal result clauses, not implicit दा commands: no verbal action
is inserted into their AST or completed-action history. Numerals and underived
सत्य/असत्य retain literal types; named referents resolve to their current typed
values, including procedure arguments and lists. Unbound ordinary nominals
remain word values. Agreement checking for nominal results is still incomplete.
An explicitly inflected standalone nominal result must be nominative; oblique
case forms are not silently rewritten. Numeral leaves retain numeric values:
`द्वि + औ` denotes two and `त्रि + जस्` denotes three. Their grammatical number
does not turn the result into a pair or list.
Primitive bare cardinals receive their understood nominative ending using the
shared cardinal-agreement rule: द्वि receives औ, while त्रि receives जस्.
Explicit incompatible low-cardinal endings are rejected rather than repaired.
This check does not impose cardinal agreement on ordinal adjectives or restrict
the nominal quantity constructions of higher cardinals.
The complete example is [nominal_results.pvm](../examples/control_flow/nominal_results.pvm).

## 10. Repetition and condition-controlled loops

A failed body stops its loop rather than retrying or reporting successful
completion. A procedure body stops before later sentences after failure or break.
The nearest native loop consumes a break's control effect; its action result
remains visible, but the break does not terminate an enclosing loop. Result
callbacks receive the consumed signal state too.
Frequency qualifiers retain their clause scope. A leading frequency on a
connected event chain repeats that chain; a frequency inside a branch or a
distributive body does not repeat the enclosing conditional or foreach.
An explicit count of zero executes no body and preserves the previous result;
a count of one still defines a repetition boundary. Counts outside the current
IR's nonnegative integer range are rejected, not truncated or wrapped.

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
प्रयत्न + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
निवेश + अम् सङ्ख्या + टा ग्रहँ + श्ना + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
```

The first sentence can also declare what the procedure produces:

```pvm
परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
```

Meaning: “The procedure named Parīkṣaṇa produces a number.” The procedure is
nominative singular; its product is accusative singular. The causative form of
Divādi `जनीँ` is `जनयति`; both the root `जन्` and its upadeśa `जनीँ` are accepted.
`JanDhatu` owns this lexical declaration meaning. `सङ्ख्या`, `शब्द`, and `सूची`
declare existing result types; another nominal identifies a declared record schema.
The vikaraṇa may be omitted as above or written explicitly as
`जन् + णिच् + शप् + लट् + तिप्`. The causative takes `शप्`, not the base
root's `श्यन्` ([3.1.68](https://ashtadhyayi.github.io/suutra/3.1/3.1.68/)).
Incompatible explicit affixes are rejected in this declaration.
The interpreter and compiler enforce the same result contract. Do not add a
second `… इति परिणाम …` declaration in the body. This header declares a contract;
it does not execute a generation action or make a body sentence return early.
Existing `… प्रक्रिया … अस्ति` headers and body result declarations still work.
See `examples/control_flow/typed_procedure_header.pvm`.

### Returning a value early

Within a procedure, `फल + सुँ शून्य + सुँ असँ + लट् + तिप्` states
“the result is zero.” Both the result role and its value are nominative; the
present copula is third-person singular. This predication finalizes the current
call. Future tense is not a return alias, and an ordinary assignment does not
become a return because its destination is named परिणाम.

```pvm
गणन + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
फल + सुँ प्रथम + टाप् + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ॥
द्वि + औट् गणन + टा डुकृञ् + उ + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

Return supplies one resolved value, including a collection or record value, and
terminates the nearest procedure call. It crosses a branch, repetition, while,
or foreach scope without becoming a loop break. Subsequent body statements and
loop-completion results are skipped. The declared result contract still applies,
and the caller resumes after the call. Outside a procedure, this predication
does not request procedure control transfer. A locally declared parameter named
फल shadows the result role; a dynamic caller's similarly named parameter does not.
Returning does not print; use `मुद्र् + णिच्` in the caller when output is wanted.
Without an explicit return, the body's final produced value remains its normal
result, including a nominal value selected by a conditional branch.

See `examples/control_flow/procedure_return.pvm`. Its readable `.txt` is generated
by the CLI; unsupported morphological generation currently remains segmented.
Suspended enclosing procedure continuations still require further consolidation.

Invoke the declared name in the instrumental with `कृ`. An ordinary noun
name takes its case suffix directly; do not add `ल्युट्` to it:

Current invocation selects one procedure: use instrumental singular `टा`, with
an optional genitive singular domain (`ङस्`). Dual/plural procedure or domain
references are rejected as unsupported rather than silently executed once.
This restriction concerns the named referents, not the number of input values.
Declarations likewise use a nominative singular name and, when scoped, a genitive
singular domain. Dual/plural declarations do not currently define multiple symbols.

`नाम` is the naming particle. The existing `इति प्रक्रिया … अस्ति` form remains
supported and produces the same native procedure identity. For a neuter name
such as गणनम्, use nominative `गणन + सुँ`, not accusative `गणन + अम्`:

```pvm
गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
```

This is a header example; a complete procedure needs its body ending in `॥`.
The “this” pronoun is not part of this new construction.

```pvm
प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।
```

Derived action nouns still use `ल्युट्` on a verbal root, for example
`गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप्` and the
matching call `गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप्`.
Keep the same nominal identity in the declaration and call. A bare
`… + ल्युट् + सुँ ।` is a nominal statement, not a procedure declaration in
the default native document parser. Use an explicit `नाम प्रक्रिया … अस्ति` or `इति प्रक्रिया … अस्ति`
declaration. Parsing follows grammatical statement delimiters, not physical
lines, so a declaration or body statement may span several lines.
For migration tooling only, `PvmScript.parseLegacy` and
`PrakriyaScriptValidator.validateLegacy` inspect old bare-header blocks; the
latter suggests the explicit declaration without changing its name or calls.
Execution never silently falls back to legacy parsing on a native parse error.

### 11.2 Typed named parameters and result

Parameters are call-owned slots. Assignment to a parameter changes subsequent
reads in that call without replacing a caller's same-named value. Named and
positional references share the slot. A distributive member can shadow it for
one iteration. Ordinary results and effects remain visible; this does not make
every new assignment name in a procedure local.

Compiled positional references use the same AST rebinding as interpreted calls.
Using only the second position still requires a first slot; productive ordinal
positions are not restricted to the first three. Legacy compiler inference has
a 4096-slot analysis budget. Prefer explicit typed declarations: an ordinal
identifies a position, not the argument's type. Undeclared positional slots are
unconstrained unless an existing type guard supplies a constraint. Operations
still validate their operands; positional syntax does not grant numeric meaning
to text or collections. Calls with declared or inferred positional slots require
exactly that many arguments: missing and excess inputs fail before the body runs.
A list is one argument, regardless of its member count. History/member ordinal
selectors and known procedure names do not create parameter slots. Legacy bodies
without any declared or positional slots retain their existing unrestricted
argument behavior; general typed-valency inference remains unfinished.

### Typed participant phrases

A procedure can infer inputs from ordinal adjectives agreeing with type nouns,
without separate `इति मान` declarations:

```pvm
परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
प्रथम + टाप् + अम् सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।
फल + सुँ प्रथम + सुँ शब्द + सुँ असँ + लट् + तिप् ॥
राम + अम् द्वि + औट् च परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

This procedure takes its first number and its first word. Ordinals are counted
within each type, not across the whole call. The word and number may be supplied
in either order; order among two numbers or two words remains significant.
Referencing the second number reserves two numeric input slots even when the
first is otherwise unused. Actual payload types determine binding: numeric-looking
text remains text, and a list (including an empty list) is one list input.

Missing inputs, excess inputs, and incorrect per-type counts fail before the body
runs. Do not mix inferred typed participants and explicit named declarations in
one procedure. The explicit declaration form below remains supported separately.
Case, singular number, and known grammatical gender must agree. The analyzer
rejects competing compatible heads or several ordinals claiming one head; it does
not use nearest-word guessing. Repeated `च` can explicitly delimit same-case
phrases, for example:

```pvm
योग + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
प्रथम + टाप् + अम् सङ्ख्या + अम् च द्वितीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ॥
द्वि + औट् त्रि + शस् च योग + टा डुकृञ् + उ + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

Here each `च` closes one input phrase. Each phrase contributes one operand,
not separate adjective and noun values. The same phrases can place their ordinal
after the noun. Source operand order remains significant for noncommutative
operations. With several indistinguishable heads and only one final `च`, missing
phrase boundaries are not manufactured from word proximity. Different case roles,
such as coordinated agents followed by objects, stay separate. See
`examples/control_flow/coordinated_typed_inputs.pvm`.

Established collection-member and history roles
are protected rather than rebound as inputs. Productive ordinals can retain
an explicit feminine affix, for example `द्वि + तीय + टाप् + अम् सङ्ख्या + अम्`.
Its position stays two; टाप् supplies feminine morphology, not another input.
These productive ordinal nodes also work inside native coordinated phrases;
`प्रथ् + अमच् + टाप्` and `द्वि + तीय + टाप्` retain their derivational segments
instead of being rewritten as lexical stems. The coordinated-input example uses
these productive forms, with the same readable sentence and execution result.
Recognized affix spelling alone does not establish licensing: the existing
derivation engine must support that combination, preserving the written ordinal
affix rather than replacing it with a default ordinal of the same numeric value. See
`examples/control_flow/typed_ordinal_participants.pvm`. General phrase attachment
and broader derivational licensing remain unfinished. Coordinated phrase members
currently use the `SubantaPada` representation; combining productive
`SankhyaPuranaPada` ordinals with these explicit phrase groups still needs a
general nominal-phrase representation that preserves the ordinal derivation.

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

A list parameter can serve as the genitive whole in a member sum:

```text
मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ।
```

Pass one list-valued argument to that declared parameter. It remains a single
collection value rather than becoming a nested list or a series of scalar
arguments.

`समवाय` is an ordinary name, not an implicit collection of all procedure
arguments. Declare a list parameter and express its member relation as above.

Supported declared parameter/result types are:

```text
सङ्ख्या   शब्द   सूची
```

Argument types belong to resolved values, not to the spelling of a name. A
nominal argument can refer to a list declared earlier. Compiled calls check
known values statically and validate unresolved arguments against the declared
parameter types after resolving their values, before entering the procedure.
An ordinal such as `प्रथम` may name a bound value, including a collection.
Its spelling alone does not establish a numeric argument type during static
analysis; use explicit parameter declarations for the intended contract.
A word or scalar does not become a list merely because the parameter expects one.

### 11.3 Positional call

Accusative arguments bind in declaration order:

```pvm
द्वि + औट् त्रि + शस् च योजन + टा डुकृञ् + उ + लोट् + सिप् ।
```

The older ordinal references `प्रथम`, `द्वितीय`, and so on remain supported for
untyped and migrated definitions.

An explicitly declared parameter name takes precedence over ordinal position.
For example, if `द्वितीय` is declared first and `प्रथम` second, `प्रथम + अम्`
refers to the second declared slot by its name. Without a matching declaration,
the legacy ordinal reference selects its corresponding argument position.

### 11.4 Named call

For a named argument, place the parameter slot in saptamī and its value in
dvitīyā. With several parameter/value pairs, immediately follow each slot
with its value. Thus `दक्षिणे त्रि` means “three in the
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

With exactly one written slot and one accusative value, their attachment does
not depend on adjacency or order: `मान + ङि पञ्च + अम्` and
`पञ्च + अम् मान + ङि` bind the same named argument. The procedure noun and
finite कृ verb may occur between them. This does not establish arbitrary
attachment for several separated slot/value pairs.

Field-valued arguments can also follow a record method name. Projection retains
the genitive receiver and instrumental method while resolving each separate
field phrase; encountering the method does not end argument analysis.

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
and `नित्य` qualifiers participate in precedence. A `क्त`-derived procedure name
retains its nominal identity; it does not imply memoization or purity. Every call
executes its body and validates its result normally.

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

The interpreter and compiled normal returns validate the schema name and exact
field set; field ordering is not significant. A declared result must be produced
by the call, not inherited from the caller's previous result. Compiled contracts
also use schemas imported through module metadata. Structured
`SanskritValue.Rupa` values preserve the types of their fields through calls,
pipelines, compiled semantic codecs, and persisted state.

Primitive signature types (`सङ्ख्या`, `शब्द`, `सूची`) and declaration markers
(`मान`, `परिणाम`) have their signature meaning only when underived. For example,
`सङ्ख्या + त्व` does not declare a number type. A derived result-schema name keeps
its complete morphological identity and must have a matching schema declaration;
it does not alias its underived base.

Parameter names must be unique and a procedure may declare only one result.
Conflicting legacy argument-type guards are invalid. A signature declaration
may include `असँ + लट् + तिप्`; extra participants, negation, or an unrelated
reporting action are not silently discarded to manufacture a declaration.
The supported one-slot/one-result form uses singular nominatives for its names,
type noun, and marker. Dual and plural forms do not declare that singular
contract. Singular `सूची` denotes one collection value, not a one-member list.

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

Render the segmented source as readable Sanskrit without executing it:

```sh
./cli/build/install/cli/bin/cli --render-readable path/to/mukhya.pvm
```

Portable sūtra-grantha source has a separate compile, validation, and execution
workflow:

```sh
./cli/build/install/cli/bin/cli --emit-grantha path/to/mukhya.pvm path/to/mukhya.sutra
./cli/build/install/cli/bin/cli --check-grantha path/to/mukhya.sutra
./cli/build/install/cli/bin/cli --grantha path/to/mukhya.sutra
```

`--emit-grantha` compiles every executable source line without executing it.
References from a later line to an earlier result keep the earlier operation's
symbolic result type while planning. They are not guessed from a display label
or represented by a fabricated placeholder value. `--check-grantha` validates
the emitted source, and `--grantha` executes it.

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

### A grammatical role remains ambiguous

For a morphologically ambiguous participant, dispatch considers the grammatical
candidate roles jointly with the other participants and the operation's typed
valency. It preserves established roles and does not choose by word order.
An ambiguous participant cannot be silently discarded when none of its candidate
roles is accepted by the operation.
If multiple complete assignments fit, execution reports ambiguity. If the
assignment search exceeds its analysis budget, it reports a limit failure rather
than selecting a partial interpretation. This does not yet provide general
modifier or whole/member attachment.

### Output appears after an input prompt unexpectedly

Use the direct installed CLI launcher. Console output is flushed immediately,
but Gradle-run stdin buffering can make interactive ordering less clear on some
platforms.

## Distributive list iteration

Substantival ordinals can select a member directly from a genitive whole:

```pvm
सूची + ङस् द्वितीय + अम् उद् + हृ + लोट् + सिप् ।
```

This extracts the second member without mutating the list. The ordinal must be a
singular accusative; positions are one-based and out-of-bounds extraction fails.
Productive pūraṇa syntax such as `द्वि + तीय + अम्` uses the same semantic frame.
The selector may precede or follow the genitive whole. This does not yet implement
general adjective/head attachment or quantified collection phrases.

```pvm
एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।
सूची + ङस् सदस्य + अम् प्रति सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

The genitive names the list; the singular accusative before distributive प्रति
introduces the member referent for the body. This prints each member in order.
The body may contain nested clauses or a ततः chain. A new danda-separated
sentence is outside the loop. Verbal prefix `प्रति + …` remains separate syntax.
The same distributive clause can be the consequent or alternate of यदि … तर्हि
… अन्यथा; an unselected branch does not resolve or iterate its collection.

Iteration snapshots list membership on entry, preserves member value types,
and uses the condition-iteration host budget. An empty list runs no body.
`वि + स्था + लोट् + सिप्` exits the nearest loop. Non-list sources fail.
See `examples/control_flow/foreach.pvm` and its CLI-generated readable text.

### Local pronoun references

Underived `तद्` can refer to a grammatically compatible declared procedure
participant or foreach member. Its सुप् ending supplies its role in the current
clause; it need not repeat the antecedent's case.

```pvm
एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।
सूची + ङस् मान + अम् प्रति तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

This prints the three members in order. Resolution uses declaration number and
established lexical or explicit feminine-derivation gender evidence, not payload type, list length, or the most
recent value. Missing evidence, missing antecedents, and multiple compatible
antecedents are errors. A singular noun naming a list is still grammatically
singular and may compete with a singular member; use an explicit name when
agreement cannot distinguish them.
Further derivation does not automatically inherit the base noun's gender; when
gender evidence is incomplete, use the declared name rather than relying on तद्.
An explicit feminine suffix supplies evidence at the final derivational layer;
an earlier feminine suffix does not override a later unsupported derivation.

The selected referent remains live: assignment through `तद् + ङे` updates its
local cell. References work in ordinary invocations, predicates, and implicit
nominal branch results in both interpreter and compiler. Foreach scope ends
with its body. A genitive pronoun may name the foreach collection itself; its
antecedent is resolved before the new member enters scope. A procedure's pronouns
do not capture its dynamic caller's
participants. General top-level discourse references are not yet supported.
This is not blanket support for every सर्वनाम: derived forms and other senses
such as interrogatives, relatives, and quantifiers are not automatic antecedent
lookups.
See `examples/control_flow/local_pronouns.pvm` for procedure assignment, an
implicit branch return, and foreach references; its `.txt` is CLI-generated.

After ततः, a display verb with a written accusative object prints that object
once; the preceding result is not added as a second object. An objectless display
can still consume the preceding result. This rule includes coordinated nouns and
typed numeral/ordinal operands and does not disable arithmetic result pipelines.

## 17. Complete number-guessing example

The repository contains a complete interactive program at:

```text
projects/number-guessing-game/number_guessing_game.pvm
```

It demonstrates a scoped range, random selection, validated input, assignment,
nested conditionals, a reusable attempt kriyā, direct result pipelines, a
bounded condition-controlled loop, exhaustion handling, and automatic
structured loop results.
