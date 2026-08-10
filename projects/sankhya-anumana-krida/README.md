# सङ्ख्या-अनुमान-क्रीडा

A console number-guessing project written in Sanskrit as a PaniniVM `.pvm`
program. PaniniVM chooses a number from 1 through 10 and gives the player five
validated numeric guesses.

The example demonstrates:

- random choice with `दिव्`;
- an inline inclusive range, `एक + ङसिँ दश + ङि`, consumed directly by `दिव्`;
- numeric input with `ग्रह्` and the `सङ्ख्या` type marker;
- the same inline range cases reused as input bounds;
- one persistent variable, `रहस्य`, assigned with `दा`;
- direct use of each action's latest `फल`, without a temporary guess variable;
- equality-style membership testing with the existing `अस्` operation;
- conditionals with `यदि … तर्हि … अन्यथा`;
- a reusable kriyā-saṃjñā declared as `प्रयत्न + ल्युट् + सुँ`;
- a bounded loop using `पञ्च + कृत्वः` instead of five copied attempt stages.
- early loop termination with `वि + स्था` after a correct guess.

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
