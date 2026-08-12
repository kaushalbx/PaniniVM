# Derivation Sūtra Roadmap

The project adds Pāṇinian rules as typed executable transformations. A rule is counted when it is registered through the common `Sutra<C, R>` model and has tested applicability and output. Derivation rules operate on derivation state; sentence rules operate on typed kāraka or vibhakti contexts; Uṇādi rules operate on declarative suffix assignment catalogs.

## Current Scope

- **893 registered executable sūtras out of 3,959**: Integrated across nominal derivation, verbal derivation, compound formation (Samāsa), phonology (Sandhi), and sentence-level Kāraka/Vibhakti scopes.
- **33 registered Uṇādipāṭha sūtras**: Integrated under Aṣṭādhyāyī 3.3.1 (*उणादयो बहुलम्*) with etymological reverse lookup and step-by-step derivation tracing (`UnadiDerivationEngine`).
- **Centralized & Partitioned Pāṇinian Saṁjñās & Artha**: Typed in `:core` (`Samjna.Unit`, `Samjna.Affix`, `Samjna.Phono`, `Samjna.Stem`, `Samjna.Avyaya`, `Samjna.Karaka`, `Samjna.Rudhi`, `Artha.Karaka`, `Artha.Dispositional`, `Artha.Taddhita`, `Artha.Rudhi`, `Artha.Explanation`).
- **100% Full Coverage of Aṣṭādhyāyī 1.4 Kāraka Sūtras**: All 33 classical Kāraka saṃjñā sūtras implemented and verified.
- **100% Full Coverage of Aṣṭādhyāyī 2.3 Vibhakti Sūtras**: All 64 classical non-Vedic Vibhakti sūtras implemented and verified with syncretic `sup` resolution.
- **Samāsa (Compound Formation) Subsystem**: Implemented Adhyāya 2.1 & 2.2 Sūtras (`2.1.6` Avyayībhāva, `2.1.24` Dvitīyā Tatpuruṣa, `2.1.37` Pañcamī Tatpuruṣa, `2.2.8` Ṣaṣṭhī Tatpuruṣa, `2.2.24` Bahuvrīhi, `2.2.29` Dvandva).
- **Phonological Transformation & Sandhi Subsystem**: Refactored Sandhi Sūtras (`6.1.109` Pūrvarūpa, `6.1.132` Su-lopa, `6.3.111` Ḍhralope Dīrgha, `8.3.14` Ḍho ḍhe lopa, `8.3.17` Yo 'śi, `8.3.22` Hali sarveṣām, `8.4.59` Vā padāntasya, `8.4.60` Tor li, `8.4.62` Jhayo ho 'nyatarasyām, `8.4.63` Śaś cho 'ṭi, `8.4.65` Jharo jhari savarṇe) with type-safe `PratyaharaEngine` integration and inline rule matching.
- **100% Full Coverage of Subanta Nominal Stem Classes**: All 31 Classical Sanskrit nominal stem categories implemented (vowel, consonant, pronominal, numeral) across 21-slot `sup` paradigms.
- **Verbal Tiṅanta Subsystem**: Full 10-gaṇa Dhātupāṭha catalogue with LAṬ, LOṬ, LAṄ, LIṄ, LṚṬ, LṚṄ, and LIṬ stem formation and paradigm verification in both padas.

---

## Completed Milestones & Updated Focus

1. **[COMPLETED] 100% Kāraka & Vibhakti Coverage**:
   - All 33 classical Kāraka saṃjñā sūtras (Adhyāya 1.4) and 64 Vibhakti sūtras (Adhyāya 2.3) fully implemented and verified.

2. **[COMPLETED] Declarative Uṇādipāṭha & Derivation Bridge**:
   - 33 Uṇādi sūtras implemented across all 5 Adhyāyas.
   - Stem classification (`RUDHI_PRATIPADIKA` vs `YAUGIKA_PRATIPADIKA`) and `VakyaAnalyzer` integration.
   - Derivation engine bridge (`UnadiDerivationEngine`) and CLI commands `--unadi` & `--derive-unadi`.

3. **[COMPLETED] Centralized Partitioned Saṁjñās & Artha Architecture**:
   - `dev.panini.shiksha.Samjna` and `dev.panini.shiksha.Artha` moved into `:core` and partitioned into authentic Pāṇinian sub-enums.

4. **[COMPLETED] Dialogue Session & Nominal Sentence (NamaVakya) Execution Pipeline**:
   - Support turn-based dialogue state tracking via `SambhashanaContext` and resumable safety gating (`ExecutionAuthority`).
   - Map nominal-only sentences implicitly to the copula root `असँ` with direct morphological Kāraka inference.
   - Resolve positional references (`अन्तिम`/`उपान्तिम`) semantically to historical step outcomes with target assignment compatibility.

5. **Future Focus Areas**:
   - Build declarative Taddhita derivation module (`taddhitapatha`) for Apatya (4.1.92), Matvarthīya (5.2.94), and Bhāvārthaka (5.1.119) affixes.
   - Broaden Aorist (*luṅ*) 7-variety dhātu derivations across rare root shapes.
   - Integrate fine-grained Vedic accent-aware tokenization (*Svaravidhi*).
