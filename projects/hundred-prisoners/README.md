# One Hundred Prisoners Riddle — Cycle-Following Solution

> **Scope:** The setup supplies a uniformly random box permutation. The
> prisoners do not construct or alter it; they only follow its cycles.

There are 100 prisoners, 100 numbered boxes, and one prisoner number hidden in
each box. Every prisoner may open at most 50 boxes. The prisoners all survive
only if every prisoner finds their own number.

## Strategy

Prisoner *i* opens box *i* first. If that box contains number *j*, the prisoner
opens box *j* next and continues following the numbers as pointers. This walks
the cycle of the box permutation containing *i*. The prisoner stops after
finding *i* or after opening 50 boxes.

## Why it works

Every prisoner finds their number within 50 openings exactly when every cycle
of the permutation has length at most 50. The group loses precisely when the
random permutation contains a cycle longer than 50. There can be at most one
such cycle.

For a uniformly random permutation, the probability of a cycle of length *k*,
where 51 ≤ *k* ≤ 100, is `1/k`. Therefore, the success probability is:

```text
1 - (1/51 + 1/52 + ... + 1/100) ≈ 0.3118
```

This is about 31.18%, dramatically better than the independent random-choice
strategy, whose group success probability is `(1/2)^100`.

## What the executable program demonstrates

The `.pvm` program models the standard setup by drawing all values from 1
through 100 without replacement. Appending the draws in order creates a
uniformly random permutation: the value at position *i* is the slip placed in
box *i*. This setup is isolated in the named `पेटिकाक्रमनिर्माण` procedure so
that permutation construction is distinct from the prisoners' strategy.

It then runs all 100 prisoners. For each prisoner it starts at the box matching
their number, follows each discovered number to the next box, stops after
finding the prisoner's number or after 50 openings, and records whether the
whole group survived. The program uses the first-class Sanskrit truth literals
`सत्य` and `असत्य`, so `सर्वजय` and `स्वसङ्ख्याप्राप्ति` remain typed Boolean
values rather than numeric flags. Because this is a genuine random trial, it
may print either `जय` or `पराजय`.

One execution is a simulation trial, not an empirical estimate of the success
probability. Estimating the approximately 31.18% rate experimentally requires
many independently generated permutations and aggregation of their outcomes.
