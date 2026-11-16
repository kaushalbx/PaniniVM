# SandhiKosh evaluation

This is the reproducible benchmark contract and latest verified checkpoint.
Generated reports contain row-level details; Git history records older batches.

## Run

Sources are the unchanged SandhiKosh
[Aṣṭādhyāyī](https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/Astaadhyaayii%20Corpus.xls)
and [UoH](https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/UoH_Corpus.xls)
workbooks. Extraction preserves spelling, whitespace, shaping controls,
malformed input, and row identifiers.

```powershell
.\gradlew.bat -I scripts/sandhikosh-evaluation.init.gradle `
  -PsandhikoshEvaluationDir=build/sandhikosh-evaluation `
  -PsandhikoshLexicalAnalysis=declared `
  :derivation:sandhiKoshEvaluation --no-parallel
```

Use `build/sandhikosh-uoh-evaluation` for UoH and add
`-PsandhikoshAlternatives=true` for alternatives. Reports are
`results-{sequence|alternatives}-declared-lexicon.*`.

## Policy

- Evaluate completed padas left-to-right with `CONTINUATION`; only the surviving
  rightmost phonological term enters the next boundary.
- Preserve term identity, annotations, signs, metadata, pada ownership, and
  traces. Use canonical `term.varnas` and exact Varṇa edits.
- Default chooses the configured derivation; alternatives enumerates `joinAll`.
  More than 4,096 outcomes is an error, never silently truncated.
- Match NFC with whitespace ignored; report exact NFC including spacing
  separately. Apply no other normalization.
- Never infer an underlying s/r source from written visarga. Declared forms
  require independent evidence and successful forward derivation.
- Empty/unusable records are explicit skips; exceptions remain errors.

## Verified checkpoint — 2026-10-19

| Corpus and mode | Pass | Fail | Errors | Skipped | Exact |
| --- | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī default | 890 | 1,805 | 4 | 1 | 621 |
| Aṣṭādhyāyī alternatives | 1,172 | 1,523 | 4 | 1 | 643 |
| UoH default | 5,203 | 4,161 | 0 | 4 | 4,614 |
| UoH alternatives | 6,015 | 3,349 | 0 | 4 | 4,677 |

Declared coverage is 252 Aṣṭādhyāyī rows/285 occurrences and 2,583 UoH
rows/2,936 occurrences. The complete 77-task suite and `git diff --check` pass.

## Checkpoint — 2026-11-15

| Corpus and mode | Pass | Fail | Errors | Skipped | Exact |
| --- | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī default | 1,408 | 1,287 | 4 | 1 | 774 |
| Aṣṭādhyāyī alternatives | 1,764 | 929 | 6 | 1 | 800 |
| UoH default | 6,009 | 3,354 | 1 | 4 | 4,983 |
| UoH alternatives | 6,872 | 2,482 | 10 | 4 | 5,053 |

- Against 2026-11-08: Aṣṭādhyāyī default −2 pass / −1 exact, alternatives −2 / −1 (errors 8 → 6), UoH
  alternatives −4 (errors 6 → 10, all over the 4,096-branch limit). Not yet attributed row by row; 8.4.47
  अनचि च and its junction-only rule below are the main change in branching.
- Alternatives evaluation keeps only each branch's choices and re-derives the selected proof (a guard
  fails the run if a replay diverges); identical branches merge. Aṣṭādhyāyī alternatives runs in 16 GB.
- 8.4.46/8.4.47 at a join of completed padas are offered only at the join's own sites: conditions that
  span the junction or that the join itself changed (दधि + अत्र → दद्ध्यत्र, from the yaṇ य). A word's
  interior was settled when it was formed; re-offering it made one long compound (row 463) run out of
  memory. `isJoinOfCompletedPadas` / `isJoinSite` in `PhonologicalPadaBoundary.kt`.
- Upasarga ṇatva by root upadeśa (`sourceDhatuId`): 8.4.14 (प्रणमति; प्रनन्दति from टुनदिँ stays),
  8.4.15 (प्रहिणोति, प्रमीणाति), 8.4.17 (प्रणिगदति), 8.4.18 (optional, declined by default), 8.4.19
  (प्राणिति, after the 6.1.101 merge via the dropped verb's root). A vowel-final upasarga with a known
  root now joins through the sūtras (`joinPrefix`); सम् keeps the anusvāra path.
- Upasarga ṣatva: 8.3.65 (निषिञ्चति, अभिष्टौति), 8.3.66 (निषीदति, प्रतिसीदति), 8.3.70 (परिषेवते), 8.3.77
  (वि + स्कभ्नाति). Open: the Kāśikā has विष्कभ्नाति, but 8.4.2 then gives ण (विष्कभ्णाति); the blocking
  rule is not identified. The अट्-intervened forms (8.3.63, 8.3.71) merge by yaṇ and are not reached.
- 6.1.72, 6.1.84 (headings), 6.1.85 (atideśa) and 6.1.86 (paribhāṣā) are registered without state
  changes. 6.1.86 is not enforced as a general asiddhatva yet. The headings use the Anuvṛtti role: an
  Adhikara role gates every rule in its range on an activation, which broke 6.1.72–157 in a first try.

### Follow-up batch — 2026-11-15 (later)

All four benchmarks are unchanged from the checkpoint above (no row of either corpus reaches these forms):

- 2.4.77 now covers all its roots (गा, स्था, घु, पा पाने, भू) and only parasmaipada endings: अदात्, अस्थात्.
- 6.1.68 no longer takes a root-final आ/ई as आप्/ङी: अयात् (was अया), अदात् (was अदाः).
- New 3.4.110 आतः and 6.1.96 उस्यपदान्तात्: अदुः, अस्थुः.
- New 8.4.34 न भाभूपूकमिगमिप्यायीवेपाम्: प्रभवन, प्रगमन, प्रकमन, प्रभान.
- New optional 8.4.30 णेर्विभाषा, 8.4.31 हलश्चेजुपधात्, 8.4.33 वा निंसनिक्षनिन्दाम् (declined by default:
  प्रयापन, प्रकोपन, प्रनिन्दन; the ṇ forms are offered as alternatives).
- 8.4.2 no longer reaches a root's own न across an upasarga (प्रनन्दन); that is 8.4.14's, which now reads
  the root's Dhātupāṭha upadeśa (प्रणमन, परिणयन).
- Kṛdanta root terms now carry their Dhātupāṭha id (`sourceDhatuId`).

### Follow-up batch — 2026-11-16: 8.4.20–8.4.28

All four benchmarks unchanged from the checkpoint.

- 8.4.20 अन्तः (प्राण्; 8.4.19 no longer takes the pada-final न), 8.4.21 उभौ साभ्यासस्य (प्राणिणिषति).
- 8.4.22 हन्तेरत्पूर्वस्य (प्रहण्यते, प्रहणन; not प्रघ्नन्ति; प्रहन्ति keeps न because 8.3.24 acts first).
- 8.4.23 वमोर्वा (प्रहन्मः by default, प्रहण्मः offered).
- 8.4.24 अन्तरदेशे and 8.4.25 अयनं च: whether a place is meant is not known to the derivation, so the
  ṇ forms (अन्तर्हण्यते, अन्तरयण) are offered and the unchanged forms are the default. अन्तर् is known by
  its upadeśa, since 8.3.24 has changed its surface by then. `joinPrefix` routes अन्तर् + हन् through the sūtras.
- 8.4.26–8.4.28 (Vedic) apply only under the new `DerivationalEnvironment.CHANDAS`; `SandhiEngine.join`
  and `joinSamasaMembers` take an `environments` argument. नस् is known by its upadeśa.

## Checkpoint — 2026-11-08

| Corpus and mode | Pass | Fail | Errors | Skipped | Exact |
| --- | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī default | 1,410 | 1,285 | 4 | 1 | 775 |
| Aṣṭādhyāyī alternatives | 1,766 | 925 | 8 | 1 | 801 |
| UoH default | 6,009 | 3,354 | 1 | 4 | 4,983 |
| UoH alternatives | 6,876 | 2,482 | 6 | 4 | 5,053 |

- 6.1.95 ओमाङोश्च: अवर्ण before ओम् or आङ् (by upadeśa) takes पररूप (शिवोम्). 6.1.112 ख्यत्यात्परस्य gives
  सख्युः and पत्युः; 1.4.8 पतिः समास एव keeps पति out of घि outside compounds (पत्ये, पत्युः). 8.3.42
  तिरसोऽन्यतरस्याम् optionally gives तिरस्कृत्वा, only when तिरस् is given with its own स्.
- Upasarga vowel sandhi now goes through the sūtras (`SandhiEngine.joinPrefix`, क्रियायोग state).
  6.1.91 उपसर्गादृति धातौ is added (प्र + ऋच्छति → प्रार्च्छति; the optional 8.4.65 then gives
  प्रार्छति by default). 6.1.94 and 6.1.91 require the following word to be the upasarga's own
  verbal form, so an ordinary प्र + ऋषभ keeps प्रर्षभ. 6.1.89 एत्येधत्यूठ्सु applies when the caller
  gives the root (उपैति, परैति, उपैधते); a typed form with no root stays उपेति. आ is read as आङ्
  (आ + एजते → एजते). Before this, 6.1.89 and 6.1.94 were reached only by unit tests.
- 8.3.40 नमस्पुरसोर्गत्योः (नमस्करोति, पुरस्करोति) needs the गति saṃjñā on the word; 8.3.43
  optionally gives द्विष्करोति, त्रिष्करोति from द्विस्/त्रिस् with their own स्. चतुर् is not taken.
- रु of a final nasal: 8.3.6 पुमः खय्यम्परे (पुंस्कोकिलः), 8.3.10 नॄन् पे (नॄँः पाहि), 8.3.12 कानाम्रेडिते
  (कांस्कान्), with 8.3.2/8.3.4 nasalisation. The vārttika संपुंकानां सो वक्तव्यः (`8.3.5-v1`) gives स् for
  the visarga of पुम्/कान्; it is ordered with 8.3.34 so it sees 8.3.15's visarga. Āmreḍita is taken
  as the repeated कान्; there is no āmreḍita saṃjñā yet.
- 8.3.58 नुम्विसर्जनीयशर्व्यवायेऽपि: ṣatva of an affix स् across visarga or शर् (सर्पिःषु, धनुःषु), and across
  a नुम् anusvāra (a न् inserted by a 7.1 rule). धनूंसि/हवींसि still keep स्: their स् is the uṇādi
  इस्/उस् of the stem, which the data does not record. 8.3.16 रोः सुपि keeps a lexical र् before सुप्
  (चतुर्षु); गिर् still needs 8.2.76 with the root of the stem (गीर्षु). 8.3.25 मो राजि समः क्वौ keeps
  सम्'s म् before bare राज्/राट्/राड् (सम्राट्).
- 8.2.23 संयोगान्तस्य लोपः now also applies to a प्रातिपदिक made pada by 1.4.17 before सुप्, not only to
  the last term, once per derivation: पुम्स् + सु → पुंसु, पुम्स् + भिस् → पुंभिः (formerly पुम्ःसु, पुम्र्भिः).
  The evidenced stem is पुम्स्; input spelled पुंस् (anusvāra) is not a saṃyoga and still gives पुंःसु.
- 7.1.89 पुंसोऽसुङ् (असुँङ् replaces पुम्स्'s final स् before a सर्वनामस्थान), 7.1.70 उगिदचां
  सर्वनामस्थानेऽधातोः (नुम् after the last vowel, only when the aṅga's उक् it is recorded in its ādeśa
  provenance; the अच् half is not modelled), and a नुम् branch of 6.4.10 (the upadhā of a नुम्-formed
  सान्त संयोग is lengthened): पुमान्, पुमांसौ, पुमांसः. `introduceRawAdesha` now keeps the upadeśa
  spelling, so the anunāsika of an ādeśa such as असुँङ् reaches 1.3.2.
- Address: `SubantaDerivationRequest(sambodhana = true)` (प्रथमा only, 2.3.47) sets `Rupa.isVocative`;
  4.1.2 records सम्बुद्धि on a singular सुँ (2.3.49). New 6.1.69 एङ्ह्रस्वात् सम्बुद्धेः (हे राम, हरे,
  गुरो; also the म् of अम् merged by 6.1.107 via 6.1.85: हे फल) and 8.2.8 न ङिसम्बुद्ध्योः (सम्बुद्धि half;
  हे राजन्). असम्बुद्धौ is now enforced in 6.4.8, 6.4.10 and 7.1.94 (हे पुमन्, हे पितः).
- The evaluation task now runs with a 16 GB heap (`-PsandhikoshHeap` overrides): every run failed with
  `OutOfMemoryError` while building the declared lexicon under the default quarter-of-RAM heap.
- 1.4.14 now marks the surviving word a pada when its final sup/tiṅ was elided (1.1.62 प्रत्ययलोपे
  प्रत्ययलक्षणम्; 1.1.63 withholds only aṅga operations). Neuter किम्/वारि + अम् (7.1.23) had no पद,
  while the प्रथमा had one through 1.4.17.
- 8.4.47 अनचि च made alternatives runs branch far more: UoH alternatives errors 6 → 24 (all over the
  4,096-branch limit), and Aṣṭādhyāyī alternatives ran out of memory. The sequence evaluator now
  merges branches that reach identical padas (full term state, rūpa, saṃjñās, visarga source, hiatus),
  keeping the first proof; a doubling declined at one boundary and taken at the next was repeating
  branches. UoH alternatives after the merge: 6,871 / 2,482 / 11 / 4 / 5,053. Aṣṭādhyāyī alternatives
  still exceeds a 16 GB heap (24 GB cannot be reserved on the 32 GB machine): every branch retains its
  full step states. Not yet fixed; Aṣṭādhyāyī default is 1,408 / 1,287 / 4 / 1 / 774 (−2 pass, −1 exact
  against 2026-11-08, not yet attributed).
- Compound members now join with a `SAMASA_PURVAPADA` environment (`SandhiEngine.joinSamasaMembers`,
  used by `SamasaEngine` for स्-final members; only the first boundary counts as अनुत्तरपदस्थ).
  8.3.45 (सर्पिष्कलश), 8.3.46 (अयस्कुम्भ, अयस्पात्र; कृ/कम् only by `sourceDhatuId`, avyayas by
  स्वरादि), 8.3.47 (अधस्पद) use it. 8.3.48 follows the कस्कादि gaṇa's spellings (कस्कः, भ्रातुष्पुत्रः).
  8.3.38/8.3.39 apply before पाशप्, कल्पप्, कप्/कन्, काम्यच्, but no engine yet derives those affixes.
  8.3.44 needs uṇādi evidence of the इस्/उस् affix, which the data does not have.
- 6.1.127 इकोऽसवर्णे शाकल्यस्य ह्रस्वश्च (चक्रि अत्र, कुमारि अत्र) and 6.1.128 ऋत्यकः (ब्रह्म ऋषिः, मह ऋषिः) keep
  both vowels, the left one shortened, as Śākalya's options; yaṇ and guṇa stay the default. 6.1.87 now
  respects a prakṛtibhāva block at its boundary.
- 8.4.47 अनचि च (दद्ध्यत्त्र) and 8.4.57 अणोऽप्रगृह्यस्यानुनासिकः (दधिँ at a pause) are added. Like 8.4.46 they
  decline by default (`appliesByDefault`), so stated forms stay; 8.4.47 never doubles र. 8.4.53 now
  voices any jhal before a jhaś, inside a word too (ध्ध → द्ध). Tests of other sūtras set these
  three options aside (`withoutIndependentOptions`); `TukAndDvitvaSandhiTest` covers them.
- 8.4.61 उदः स्थास्तम्भोः पूर्वस्य: after उद्, the स of स्था/स्तम्भ becomes थ (उत्थानम्), only when the term is
  that dhātu by upadeśa.
- 8.4.43 तोः षि keeps a tu before ष (सन्षष्ठः); 6.1.74 आङ्माङोश्च makes तुक् obligatory after आङ्/माङ्
  (आच्छादयति), which 6.1.76 would otherwise leave optional.
- तुक् before छ: 6.1.73 छे च now covers any short vowel within or across padas (it was limited to
  7.3.77 outputs); 6.1.75 दीर्घात् after a long vowel; 6.1.76 पदान्ताद्वा optional after a pada-final one.
- 8.4.46 अचो रहाभ्यां द्वे doubles a yar after a vowel plus र्/ह् (कर्त्तरि), except a śar before a vowel
  (8.4.49 शरोऽचि). Its default declines, so paradigms keep their stated forms (जुह्वतु); the doubled
  forms are alternatives. 8.4.47 अनचि च is not added: it would branch at almost every cluster.
  The extra alternatives push a few more rows past the 4,096-branch limit (errors 4 → 8, 5 → 6).

- 8.4.44 शात् now restricts 8.4.40: a tu after श stays (प्रश्ने, श्नु), +6 / +2. Most other default
  failures are licensed options the corpus does not take (8.4.59 anusvāra, 8.3.37 visarga, 8.4.65
  त्त); alternatives mode counts them.

- घञ् action nouns (3.3.18 भावे) are derived forward from every plain Dhātupāṭha root (युज् → योग,
  हृ → हार), masculine by Liṅgānuśāsana 36 घञबन्तः (`GhanPratipadikas`): Aṣṭādhyāyī +4, UoH +61.
  Upasarga compounds (आदेश, आधार) are not generated; they would multiply the stems twentyfold.
  A run now takes about 3–4 minutes per corpus.

- Members of nominal gaṇas are stems (`GanaPratipadikas`) when their gender comes from a
  Liṅgānuśāsana sūtra or a sourced lexical gender, never the default fallback. Indeclinable gaṇas
  (avyaya, nipāta, gati), svarādi members and the dhātu gaṇas of adhyāya 3 are excluded. This gave
  Aṣṭādhyāyī +23 and UoH +196. The one UoH error (row 5655, यः+यम्+यम्+परेण) is the 4,096-branch
  limit: every proof is kept through later boundaries, so ambiguous stems multiply.

- The loose match ignores the shaping controls ZWNJ/ZWJ as it ignores spacing: they choose a
  glyph, not a sound (384 rows differed only by them). Exact NFC matching still keeps them.
- Upasargas are named stems by their upadeśa (नि → नेः, आङ् → आङः, उद् → उदः).

- A row ends in a pause (1.4.110 विरामोऽवसानम्), so the default final boundary is now avasāna;
  `-PsandhikoshFinalBoundary=CONTINUATION` restores the old reading.
- Dhātus are also named with इक् (vārttika इक्श्तिपौ धातुनिर्देशे under 3.3.108: मृजि, वचि), only for
  consonant-final roots. 7.3.84 now follows 1.1.5 for every non-tiṅ कित्/ङित् affix.
- A word not ending in visarga keeps its plain reading beside any proof, so a rare dual name
  (मृगी, pragṛhya by 1.1.11) cannot hide the ordinary word.
- The lexicon is built once, in parallel with per-thread engines and merged in request order
  (identical output); a run takes about 1.5–2 minutes per corpus instead of 7+.

- The 14th Māheśvara sūtra is हल् with ह as its only sound; it had sounded ल too, so झल् and शल्
  wrongly contained ल and 8.2.39 turned हल् into हत्.
- The grammar's own names for sounds are lexical stems by 1.1.68, built from varṇas: the
  pratyāhāras it uses (`Pratyahara.ATTESTED`, named by the engine per 1.1.71), single letters, and
  तपर vowels (1.1.70); masculine by the agreement in 1.1.7, 8.2.31 and 7.3.101. Saṃjñās defined
  by their own sūtras (लोप, ह्रस्व, दीर्घ, वृद्धि, उपधा, धातु, …) are added with those sūtras' gender.
- Dhātus (marker-free form, or an upadeśa closing on a consonant it-letter) and affixes are named
  stems too (`DhatuPratyayaNamePratipadikas`), masculine by 2.4.53 ब्रुवो वचिः. One is admitted only
  when its whole paradigm derives and every visarga has a proven source (1,796 admitted, 21 not);
  a name that is also a svarādi member is left out, since 1.1.37 would make it that avyaya.
- Analyses that are identical at every boundary are merged before the sequence branches.
- Prefix sandhi now runs through the sūtras, with 8.3.41 इदुदुपधस्य चाप्रत्ययस्य added.

## Current delta

- Exact āgama/ādeśa and `cli` operations retain target, इत्, Dhātupāṭha, and
  derivational provenance. Prāgdiśīya forms derive through registered rules and
  the existing `SarvadiGana`, without synthetic gaṇas.
- Gender is supplied by registered `LinganushasanaSutra`s over gender-free stems;
  rules 145 and 183 preserve both licensed masculine/neuter readings. Semantic
  rules still require meaning provenance.
- Correct 6.4.11 and 7.1.94 replace the former misnumbered shortcut. Selected
  s/r-final `SvaradiGana` members derive through 1.1.37/2.4.82; सुप्-luk retains
  pada identity and exact 8.3.15 repha provenance; 8.3.36 retains both śar
  branches. Cumulative gain from these gaṇa forms: Aṣṭādhyāyī +1 default/+2
  alternatives and UoH +105 default/+111 alternatives.
- Rule 6.1.132 now requires a forward-derived `तद्`/`एतद्` masculine nominative
  singular and its exact `सुँ`→`रुँ` provenance; bare `सः`/`एषः` spelling is not
  treated as proof. No benchmark row depended on the removed shortcut.
- Amarakośa 3.1.23 supplies `प्रभु`; registered Liṅgānuśāsana 51 supplies its
  gender. Five UoH rows now pass in each mode; undeclared shape matches remain
  excluded from the corpus lexicon.

## Remaining failures

| Category | Required treatment |
| --- | --- |
| Incorrect rules | Source-audit and replace broad checks with exact typed operations. |
| Missing sūtras | Implement/register authentic rules with positive, negative, branch, and trace tests. |
| Applicability/precedence | Model Tripādī order, domains, lifecycle, and context explicitly. |
| Missing lexical/derivational provenance | Add only evidenced stems with successful forward derivations. |
| Optional alternatives | Preserve licensed branches; never tune defaults to expected strings. |
| Input errors | Four Aṣṭādhyāyī rows contain parenthesized material rejected by the parser. |
| Orthographic/boundary policy | Keep spacing, avagraha, shaping, anusvāra/parasavarṇa, and pause differences explicit. |

Largest clusters remain unresolved visarga provenance, multipart metalinguistic
input, optional anusvāra/parasavarṇa spelling, editorial avagraha, and missing
verbal or nominal context. Generated reports contain row-level evidence.
