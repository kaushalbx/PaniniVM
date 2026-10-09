# The Dice Problem

Enter a face number from 1 to 6; the program prints its opposite as `7 − face`.
This assumes a standard die, whose opposite faces sum to seven.

Run from the repository root:

```powershell
.\gradlew.bat :cli:run --args="--eval projects/dice-opposite-face/dice_opposite_face.pvm"
```

For example, input `2` gives `पञ्च` (5). Input outside 1–6 is rejected.
The random version remains in `examples/algorithms/dice_opposite_face.pvm`.

Regenerate the readable text through the CLI:

```powershell
.\gradlew.bat :cli:run --args="--render-readable projects/dice-opposite-face/dice_opposite_face.pvm"
```
