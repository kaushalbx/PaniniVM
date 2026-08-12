# सङ्ख्या-अनुमान-क्रीडा

A console number-guessing project written in Sanskrit as a PaniniVM `.pvm`
program. PaniniVM chooses a number from 1 through 10 and gives the player five
validated numeric guesses.

For a step-by-step introduction to the syntax used here, see the
[`PVM language guide`](../../docs/pvm-language-guide.md).

The example demonstrates:

- random choice with `दिव्`;
- one scoped inclusive range, `एक + ङसिँ दशन् + ङि इति सीमा + सुँ`;
- numeric input with `ग्रह्` and the `सङ्ख्या` type marker;
- the same scoped range reused as input bounds;
- dynamic instruction rendering from segmented ablative and locative bounds;
- grammatical `… इति मुद्र्` quotation without treating the quoted verb as `कर्मन्`;
- implicit reuse of the scoped range by `दिव्`, `ग्रह्`, and `मुद्र्`;
- one persistent variable, `रहस्य`, assigned with `दा`;
- direct use of each action's latest `फल`, without a temporary guess variable;
- direct `ततः` result piping into a following action's missing `कर्मन्`;
- equality-style membership testing with the existing `अस्` operation;
- numeric ordering with `विद्` for greater-than and `नि + विद्` for less-than;
- nested conditionals with `यदि … तर्हि … अन्यथा यदि …`;
- bare branch values in `यदि … तर्हि लघु अन्यथा गुरु ततः मुद्र्`, with one print action;
- dynamic `लघु` and `गुरु` feedback for low and high guesses;
- a reusable kriyā-saṃjñā declared as `प्रयत्न + ल्युट् + सुँ`;
- a bounded result-controlled loop using `पञ्चन् + कृत्वः यावत् फल + सुँ न तावत्`;
- natural loop termination from the comparison's typed truth value.
- an `अन्यथा` exhaustion clause that runs only when all five attempts are consumed.
- a structured `परिणाम` produced automatically by the loop, containing `अवस्था`
  (`विजय` or `समाप्ति`) and `प्रयत्नसङ्ख्या`;
- normal ṣaṣṭhī access through `परिणाम + मतुप् + ङस् अवस्था + अम्`;
- no explicit variable assignment for the loop outcome.

## Reusable kriyā support

The game uses the reusable `प्रयत्न + ल्युट् + सुँ` saṃjñā-kriyā. PaniniVM also
supports explicit typed signatures for reusable operations, including named
parameters, named call-site arguments, and declared result schemas. A typed
definition can therefore be written as:

```pvm
परीक्षण + ल्युट् + सुँ ।
अनुमान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
रहस्य + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
शब्द + सुँ इति परिणाम + सुँ ।
...
```

Callers may pass the values positionally or name them with ṣaṣṭhī/dvitīyā
pairs. This keeps larger game procedures reusable without introducing extra
temporary variables merely to preserve argument order.

## Run

From the PaniniVM repository root, build the direct CLI launcher once:

```sh
./gradlew :cli:installDist
```

Then run the game:

```sh
./cli/build/install/cli/bin/cli --eval projects/sankhya-anumana-krida/mukhya.pvm
```

The direct launcher is used because it passes interactive terminal input to the
program reliably. Enter ASCII digits (`1` to `10`) or Devanagari digits
(`१` to `१०`). Enter `:cancel` to stop.

## Main source

All game steps are in [`mukhya.pvm`](mukhya.pvm). Comments are written in
English, while executable sentences are Sanskrit.
