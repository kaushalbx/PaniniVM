# :linganushasanam Module

The **`:linganushasanam`** module implements Pāṇini's *Liṅgānuśāsanam* (लिङ्गानुशासनम्) as an executable Sūtra-driven engine for authentic nominal gender (*liṅga*) determination across nominal stems, derived affixes (*kṛt*/*taddhita*), and compounds (*samāsa*).

---

## 🏛️ Sūtra-by-Sūtra Architecture

Every rule is implemented as an individual `LinganushasanaSutra` object in its own file under the corresponding chapter (*adhyāya*) directory:

```
linganushasanam/
└── src/main/kotlin/dev/panini/linganushasanam/
    ├── LinganushasanaSutra.kt          # Abstract Sūtra base class
    ├── LinganushasanamRegistry.kt       # Sūtra catalog & lookup
    ├── LinganushasanamEngine.kt         # Evaluates gender rules
    ├── adhyaya1/                        # अथ स्त्रीलिङ्गम्
    │   ├── SwangebhyahSutra.kt          # 1.1 स्वाङ्गेभ्यः
    │   ├── AbantahSutra.kt              # 1.2 आबन्ताः
    │   ├── NgibantahSutra.kt            # 1.3 ङीबन्ताः
    │   ├── KtinantahSutra.kt            # 1.4 क्तिन्नन्ताः
    │   ├── UjantahSutra.kt              # 1.5 ऊजन्ताः
    │   └── TtalantahSutra.kt            # 1.6 तल्-ता-अन्ताः
    ├── adhyaya2/                        # अथ पुंलिङ्गम्
    │   ├── PumsiSutra.kt                # 2.1 पुंसि (अधिकार)
    │   ├── GhajantahSutra.kt            # 2.2 घञन्ताः
    │   ├── ErachantahSutra.kt           # 2.4 इर्-अच्-अन्ताः
    │   └── NranahSutra.kt               # 2.5 नराख्याः
    ├── adhyaya3/                        # अथ नपुंसकलिङ्गम्
    │   ├── NapumsakeSutra.kt            # 3.1 नपुंसके (अधिकार)
    │   ├── LyudadyantahSutra.kt         # 3.2 ल्युडाद्यन्तः
    │   └── AsunIsunUsunantahSutra.kt    # 3.3 असुन्-इसुन्-उसुन्-अन्तः
    ├── adhyaya4/                        # अथ विशेष्यनिघ्नलिङ्गम्
    │   └── VisesyanighnaSutra.kt        # 4.1 विशेष्यनिघ्नम्
    └── adhyaya5/                        # अथ समासलिङ्गम्
        ├── ParavallingamDvandvaTatpurusayohSutra.kt # 5.1 परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः
        ├── SaNapumsakamSutra.kt        # 5.2 स नपुंसकम्
        └── AnehamAnyapadartheSutra.kt   # 5.3 अनेकमन्यपदार्थे
```

---

## ⚡ Integration

- **`SubantaEngine`**: Uses `LinganushasanamEngine` to assign grammatical gender for un-annotated nominal stems.
- **`SamasaEngine`**: Delegates final compound gender determination to Sūtras 5.1, 5.2, and 5.3 during compound derivation.
