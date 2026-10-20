# :linganushasanam Module

The **`:linganushasanam`** module implements Pāṇini's *Liṅgānuśāsanam* (लिङ्गानुशासनम्) as an executable Sūtra-driven engine for authentic nominal gender (*liṅga*) determination across nominal stems, derived affixes (*kṛt*/*taddhita*), and compounds (*samāsa*).

---

## 🏛️ Sūtra-by-Sūtra Architecture

The registry contains all 189 dedicated, semantically named sūtra implementations from the
[Ashtadhyayi.com Liṅgānuśāsanam](https://ashtadhyayi.com/linganushasanam) edition.
Each object owns the source text and its complete `targetLingas` set. Phonological rules use
the shared varṇa model, while semantic and derivational rules consume explicit
`LingaRuleContext` provenance instead of guessing from surface spelling.

The edition's numbering is continuous from 1 through 189.

Canonical rules are organized by the traditional section in which they occur:

```
linganushasanam/
└── src/main/kotlin/dev/panini/linganushasanam/
    ├── LinganushasanaSutra.kt          # Abstract Sūtra base class
    ├── LinganushasanamRegistry.kt       # Sūtra catalog & lookup
    ├── LinganushasanamEngine.kt         # Evaluates gender rules
    └── sutras/                          # one implementation file per sūtra

Sections: 1–34 स्त्री, 35–117 पुं, 118–170 नपुंसक, 171–175 स्त्रीपुंस,
176–182 पुंनपुंसक, and 183–189 अविशिष्टलिङ्ग.
```

---

## ⚡ Integration

- **`SubantaEngine`**: Uses `LinganushasanamEngine` to assign grammatical gender for un-annotated nominal stems.
- **Compound rules**: Canonical compound conditions are represented by sūtras
  123–133 and explicit compound metadata in `LingaRuleContext`.
