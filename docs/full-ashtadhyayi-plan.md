# Full Ashtadhyayi Implementation Plan

The full Ashtadhyayi is tracked as a target of 3959 sutras. This project should
not mark a sutra `KRIYAVAT` until it has executable conditions, transformation
logic, and tests.

## Sutra avastha

- `PATHITA`: the sutra is present in the catalog, but not executable.
- `KRIYAVAT`: the sutra has executable logic in the engine.

## Order of work

1. Build a complete `SutraPatha` catalog from a reviewed source.
2. Add adhikara and anuvritti metadata.
3. Add paribhasha and asiddha/asiddhavat governance.
4. Implement domain engines:
   - shiksha and pratyahara
   - sandhi
   - sup and ting pratyaya
   - dhatu, lakara, and pada generation
   - krdanta
   - taddhita
   - samasa
5. Promote sutras from `PATHITA` to `KRIYAVAT` only with tests.

## Current executable scope

The current code executes six external sandhi sutras. Use:

```powershell
gradle run --args="--coverage"
```

to see the current patha/kriyavat counts.
