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

## 2. Basic formatting

### 2.1 Segments

Place `+` between a prakṛti and each pratyaya:

```pvm
दश + शस्
मुद्र् + णिच् + लोट् + सिप्
```

Whitespace around `+` is recommended. It makes source readable and produces
better editor diagnostics.

### 2.2 Sentence terminators

Use a single danda `।` to end an ordinary sentence:

```pvm
दश + शस् मुद्र् + णिच् + लोट् + सिप् ।
```

Use a double danda `॥` to close the final sentence of a reusable kriyā block:

```pvm
दर्शन + ल्युट् + सुँ ।
सन्देश + अम् मुद्र् + णिच् + लोट् + सिप् ॥
```

Do not insert another danda immediately before `यदि`, `ततः`, `तर्हि`, or
`अन्यथा` when those words continue the same program expression. For example:

```pvm
# Correct: one conditional sentence
यदि द्वि + औट् द्वि + औट् च अस् + लोट् + सिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् ।
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
| `+ ङे` | caturthī | assignment or destination |
| `+ ङसिँ` | pañcamī | lower range bound |
| `+ ङस्` | ṣaṣṭhī | domain, attribute owner, or named parameter |
| `+ ङि` | saptamī | upper range bound or location |

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
| named `…परिणाम` | structured value with typed fields |

Sanskrit number stems can be used directly:

```pvm
एक + अम्
द्वि + औट्
पञ्च + शस्
दश + शस्
```

The numeral engine evaluates and renders the semantic number; program logic
does not depend on manually maintained word-to-number tables.
Source-written counting numerals use their intrinsic number: `एक` takes
singular सुप्, `द्वि` takes dual सुप्, and numerals from `त्रि` onward take
plural सुप्. A typed numeric value stored in a variable remains a single
program value and is not subject to this source-literal agreement check.

When a numeral immediately precedes a counted noun with the same case and
number, readable generation takes gender from that noun's lexical identity.
Thus masculine `द्वि` renders as `द्वौ`, while feminine or neuter `द्वि`
renders as `द्वे`. A standalone program numeral defaults to neuter because it
denotes the numeric value itself rather than an omitted masculine noun.

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
पञ्च + शस् मुद्र् + णिच् + लोट् + सिप् ।

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
सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।
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

## 7. Assignment and variables

Use `दा` with a caturthī destination to retain a result:

```pvm
दिव् + णिच् + लोट् + सिप् ततः रहस्य + ङे दा + लोट् + सिप् ।
```

Later sentences can consume the named value:

```pvm
रहस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।
```

Prefer direct `फल` references and `ततः` pipelines when a value is used only
once. Introduce a name when the value must survive across several sentences.

## 8. Input and validation

Use `ग्रह्` to request input. A typed number request places the `सङ्ख्या`
marker in the declaration:

```pvm
निवेश + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
```

The CLI waits for input and validates it before continuing. ASCII digits and
Devanagari digits are accepted for numeric input. Enter `:cancel` to cancel an
interactive request.

### 8.1 Scoped numeric range

Declare one inclusive range with pañcamī and saptamī bounds:

```pvm
एक + ङसिँ दश + ङि इति सीमा + सुँ ।
```

The active range can be reused by random selection, numeric input validation,
and dynamically rendered instructions:

```pvm
दिव् + णिच् + लोट् + सिप् ।
निवेश + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
```

No separate lower-bound and upper-bound variables are required.

## 9. Conditionals

Use `यदि … तर्हि … अन्यथा …`:

```pvm
यदि द्वि + औट् द्वि + औट् च अस् + लोट् + सिप्
तर्हि जय + अम् मुद्र् + लोट् + सिप्
अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
```

Nested alternatives are supported:

```pvm
यदि रहस्य + अम् फल + अम् च अस् + लोट् + सिप्
तर्हि विजयः
अन्यथा यदि फल + अम् रहस्य + अम् च नि + विद् + लोट् + सिप्
तर्हि लघु
अन्यथा गुरु
ततः मुद्र् + णिच् + लोट् + सिप् ।
```

Bare branch values such as `लघु` and `गुरु` are lowered to direct result values.
The shared pipeline target executes only for the selected branch.

## 10. Repetition and condition-controlled loops

### 10.1 Fixed repetition

`कृत्वः` supplies a repetition count:

```pvm
पञ्च + कृत्वः प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
```

### 10.2 Condition-controlled loop

Use `यावत् … तावत्` to continue while a condition holds. A preceding `कृत्वः`
count places a safety bound on the loop:

```pvm
पञ्च + कृत्वः
यावत् फल + सुँ न
तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप्
अन्यथा प्रयत्नाः + अम् समाप्ताः + अम् च मुद्र् + णिच् + लोट् + सिप् ।
```

`अन्यथा` is the exhaustion branch. It runs only when the bounded loop consumes
all attempts. Runtime loop termination uses the typed truth result; it does not
require a separate break action.

The loop automatically publishes a structured `परिणाम` with:

- `अवस्था`: `विजय` or `समाप्ति`;
- `प्रयत्नसङ्ख्या`: the number of completed iterations.

## 11. Reusable first-class Sanskrit kriyās

### 11.1 Basic definition

A nominal header opens a reusable saṃjñā-kriyā. The final body sentence ends
with `॥`:

```pvm
प्रयत्न + ल्युट् + सुँ ।
निवेश + अम् सङ्ख्या + ङे ग्रह् + णिच् + लोट् + सिप् ।
फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
```

Invoke it through instrumental `ल्युट् + टा` and `कृ`:

```pvm
प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
```

### 11.2 Typed named parameters and result

Place signature declarations at the beginning of the block:

```pvm
योजन + ल्युट् + सुँ ।
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
द्वि + औट् त्रि + शस् च योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

The older ordinal references `प्रथम`, `द्वितीय`, and so on remain supported for
untyped and migrated definitions.

### 11.4 Named call

For a named argument, write the parameter in ṣaṣṭhī and immediately follow it
with its value in dvitīyā:

```pvm
दक्षिण + ङस् त्रि + शस्
वाम + ङस् द्वि + औट्
योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

Named arguments may appear in any order. One call must be entirely positional
or entirely named. The validator reports unknown, duplicate, missing, mixed,
and incorrectly typed arguments.

### 11.5 Scope and nested calls

Each invocation receives an isolated child environment. It can read caller
values but does not leak temporary body values back into the caller. A body may
invoke another registered saṃjñā-kriyā.

Prefix a definition with `अन्तरङ्गा` to make it file-private:

```pvm
अन्तरङ्गा द्विगुणन + ल्युट् + सुँ ।
प्रथम + अम् द्वि + औट् च गण् + णिच् + लोट् + सिप् ॥
```

### 11.6 Domains and overloads

An अधिकार declaration governs following definitions:

```pvm
गणित + सुँ इति अधिकार + सुँ ।
```

A definition can also carry the domain explicitly:

```pvm
गणित + ङस् योजन + ल्युट् + सुँ इति संज्ञा + सुँ ।
...
```

Typed overloads are selected by अन्तरतम compatibility. `अपवाद`, `अन्तरङ्ग`,
and `नित्य` qualifiers participate in precedence. A definition named with the
`क्त` identity is memoized by its arguments.

## 12. Structured values and result schemas

### 12.1 Construct and access a मतुप् structure

Pairs of accusative values and field names construct a structure:

```pvm
दश + शस् मूल्य + अम् पञ्च + शस् परिमाण + अम् च गुण + मतुप् + सुँ ।
```

Use ṣaṣṭhī to read a field:

```pvm
गुण + मतुप् + ङस् मूल्य + अम् ।
```

Nested genitive access is also supported.

### 12.2 Declare a result schema

A schema name ends in `परिणाम` and lists its required fields:

```pvm
अवस्था + अम् प्रयत्नसङ्ख्या + अम् अनुमानपरिणाम + मतुप् + सुँ ।
```

A typed kriyā can declare that schema as its result:

```pvm
अनुमान + ल्युट् + सुँ ।
अनुमानपरिणाम + सुँ इति परिणाम + सुँ ।
...
```

The runtime validates the schema name and required field set. Structured
`SanskritValue.Rupa` values preserve the types of their fields through calls,
pipelines, compiled semantic codecs, and persisted state.

### 12.3 Read the automatic loop result

```pvm
परिणाम + मतुप् + ङस् अवस्था + अम् ।
परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ।
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
`अपवाद` definition can override a library default according to saṃjñā
precedence.

See these checked-in examples:

- `examples/multifile/`
- `examples/private_scope/`
- `examples/adhikara_domain/`
- `examples/taddhita_inheritance/`

## 14. Running a `.pvm` program

Build the direct launcher:

```sh
./gradlew :cli:installDist
```

Run a file:

```sh
./cli/build/install/cli/bin/cli --eval path/to/mukhya.pvm
```

Compile a file to a JVM class:

```sh
./cli/build/install/cli/bin/cli --compile path/to/mukhya.pvm ProgramName
```

The interactive REPL is useful for one complete utterance at a time. Save
multi-sentence definitions and loops in a `.pvm` file.

## 15. IDEA plugin support

The IDEA plugin provides `.pvm` syntax highlighting, run actions, and live
diagnostics. For typed saṃjñā-kriyās it reports:

- duplicate parameter or result declarations;
- unknown, missing, or duplicate named arguments;
- arity and type mismatches;
- incompatible typed pipeline stages;
- missing structured result schemas.

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
ṣaṣṭhī parameter must be immediately followed by one dvitīyā value.

### A structured result is rejected

Confirm that the schema name ends in `परिणाम`, is declared before use, and that
the returned field names exactly match its declaration.

### Output appears after an input prompt unexpectedly

Use the direct installed CLI launcher. Console output is flushed immediately,
but Gradle-run stdin buffering can make interactive ordering less clear on some
platforms.

## 17. Complete number-guessing example

The repository contains a complete interactive program at:

```text
projects/sankhya-anumana-krida/mukhya.pvm
```

It demonstrates a scoped range, random selection, validated input, assignment,
nested conditionals, a reusable attempt kriyā, direct result pipelines, a
bounded condition-controlled loop, exhaustion handling, and automatic
structured loop results.
