# Ashtadhyayi Sandhi

A Kotlin project for modeling Sanskrit sandhi sutras from Panini's Ashtadhyayi.

The project separates three concerns:

- strongly typed varna model: `Svara`, `Vyanjana`, `Ayogavaha`, and `Varna`
- Paninian sutra identity: `SutraMetadata`, `SutraType`, and `BaseSutra`
- Maheshvara Sutra based pratyahara derivation through `PratyaharaEngine`

Sandhi sutras are arranged by Ashtadhyayi location:

```text
  dev.sanskrit.patha.adhyaya1.pada1
    1.1.1 - 1.1.10

dev.sanskrit.sandhi.patha.adhyaya6.pada1
  6.1.77
  6.1.78
  6.1.87
  6.1.101

dev.sanskrit.sandhi.patha.adhyaya8.pada3
  8.3.15
  8.3.34
```

The CLI accepts Devanagari directly. The engine normalizes boundary svaras from
independent svaras, matras, and inherent `अ` before applying sutras.

## Coverage

The full Ashtadhyayi is tracked as a target of 3959 sutras. The current project
catalogs and executes the six sandhi sutras below. Remaining sutras should be
added to `SutraPatha` first, then promoted to `KRIYAVAT` when implemented.

```powershell
gradle run --args="--coverage"
```

## Included kriyavat sutras

- 6.1.77 इको यणचि
  - हिन्दी: इक् वर्णों के बाद अच् आए तो उनके स्थान पर यण् वर्ण होते हैं।
- 6.1.78 एचोऽयवायावः
  - हिन्दी: एच् वर्णों के बाद अच् आए तो क्रम से अय्, अव्, आय्, आव् आदेश होते हैं।
- 6.1.87 आद्गुणः
  - हिन्दी: अ या आ के बाद इ/ई, उ/ऊ, ऋ/ॠ आदि स्वर आएं तो गुणादेश होता है।
- 6.1.101 अकः सवर्णे दीर्घः
  - हिन्दी: अक् प्रत्याहार के स्वर के बाद उसी सवर्ण का स्वर आए तो दोनों के स्थान पर दीर्घ स्वर होता है।
- 8.3.15 खरवसानयोर्विसर्जनीयः
  - हिन्दी: र् या स् के बाद खर् वर्ण हो या पद का अवसान हो तो विसर्ग आदेश होता है।
- 8.3.34 विसर्जनीयस्य सः
  - हिन्दी: विसर्ग के बाद खर् वर्ण आने पर विसर्ग के स्थान पर स् होता है।

## Run

```powershell
gradle run --args="राम इति"
```

Sandhi-viccheda support is available through `VicchedaEngine` for the currently
implemented `KRIYAVAT` sutras. It returns possible splits because viccheda can be
ambiguous.

## Test

```powershell
gradle test
```

## Input

This project expects Devanagari input directly.

```text
राम + इति -> रामेति
गुरु + अस्ति -> गुर्वस्ति
हरि + अत्र -> हर्यत्र
रामः + करोति -> रामस्करोति
```
