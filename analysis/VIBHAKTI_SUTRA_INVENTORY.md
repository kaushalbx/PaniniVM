# Complete 2.3 audit inventory

All 73 canonical headings and unique registered identities are now checked by
`VibhaktiCanonicalMetadataTest` against the Kāśikā heading sequence. The fixture
retains documented project orthography: nasal spellings such as सम्प्रदान/संप्रदान,
इत्थम्भूत/इत्थंभूत and सम्बोधन/संबोधन, and editorial avagraha in 2.3.17,
2.3.64 and 2.3.72. Only whitespace is ignored by the executable comparison;
the fixture fixes the chosen spelling rather than broadly normalizing text.
This is a wording check, not a critical edition or semantic certification.
All 73 numbers have implementations; each remains **Partial** until semantic premises,
exceptions, precedence, optionality and analyzer-level positive/negative
examples have individually been checked. Tests of a supplied semantic marker
alone do not establish automatic construction recognition.

The source files below are relative to
`ashtadhyayi/src/main/kotlin/dev/panini/ashtadhyayi/adhyaya2/pada3/`.
Specific completed checks and outstanding work are recorded in
[KARAKA_SUTRA_AUDIT.md](KARAKA_SUTRA_AUDIT.md).

| Sūtra | Checked declared heading | Semantic audit status | Implementation |
|---|---|---|---|
| 2.3.1 | अनभिहिते | Partial | AnabhihiteSutra.kt |
| 2.3.2 | कर्मणि द्वितीया | Partial | KarmaniDvitiyaSutra.kt |
| 2.3.3 | तृतीया च होश्छन्दसि | Partial | TrtiyaCaHosChandasiSutra.kt |
| 2.3.4 | अन्तरान्तरेण युक्ते | Partial | AntarantarenaYukteSutra.kt |
| 2.3.5 | कालाध्वनोरत्यन्तसंयोगे | Partial | KaladhvanorAtyantasamyogeSutra.kt |
| 2.3.6 | अपवर्गे तृतीया | Partial | ApavargeTrtiyaSutra.kt |
| 2.3.7 | सप्तमीपञ्चम्यौ कारकमध्ये | Partial | SaptamiPancamyauKarakamadhyeSutra.kt |
| 2.3.8 | कर्मप्रवचनीययुक्ते द्वितीया | Partial | KarmapravacaniyayukteDvitiyaSutra.kt |
| 2.3.9 | यस्मादधिकं यस्य चेश्वरवचनं तत्र सप्तमी | Partial | YasmadAdhikamYasyaCesvaravacanamTatraSaptamiSutra.kt |
| 2.3.10 | पञ्चम्यपाङ्परिभिः | Partial | PancamyApangParibhihSutra.kt |
| 2.3.11 | प्रतिनिधिप्रतिदाने च यस्मात् | Partial | PratinidhiPratidaneCaYasmatSutra.kt |
| 2.3.12 | गत्यर्थकर्मणि द्वितीयाचतुर्थ्यौ चेष्टायामनध्वनि | Partial | GatyarthaKarmaniDvitiyaCaturthyauSutra.kt |
| 2.3.13 | चतुर्थी सम्प्रदाने | Partial | ChaturthiSampradaneSutra.kt |
| 2.3.14 | क्रियार्थोपपदस्य च कर्मणि स्थानिनः | Partial | KriyarthopapadasyaCaKarmaniSthaninahSutra.kt |
| 2.3.15 | तुमर्थाच्च भाववचनात् | Partial | TumarthacCaBhavavacanatSutra.kt |
| 2.3.16 | नमःस्वस्तिस्वाहास्वधालंवषड्योगाच्च | Partial | NamahSvastiSvahaSutra.kt |
| 2.3.17 | मन्यकर्मण्यनादरे विभाषाऽप्राणिषु | Partial | ManyaKarmaniAnadareSutra.kt |
| 2.3.18 | कर्तृकरणयोस्तृतीया | Partial | KartrkaranayostrtiyaSutra.kt |
| 2.3.19 | सहयुक्तेऽप्रधाने | Partial | SahaYukteApradhaneSutra.kt |
| 2.3.20 | येनाङ्गविकारः | Partial | YenangavikarahSutra.kt |
| 2.3.21 | इत्थम्भूतलक्षणे | Partial | ItthambhutalaksaneSutra.kt |
| 2.3.22 | संज्ञोऽन्यतरस्यांकर्मणि | Partial | SamjnyoAnyatarasyamKarmaniSutra.kt |
| 2.3.23 | हेतौ | Partial | HetauSutra.kt |
| 2.3.24 | अकर्तर्यृणे पञ्चमी | Partial | AkartaryRnePancamiSutra.kt |
| 2.3.25 | विभाषा गुणेऽस्त्रियाम् | Partial | VibhasaGuneAstriyamSutra.kt |
| 2.3.26 | षष्ठी हेतुप्रयोगे | Partial | SasthiHetuprayogeSutra.kt |
| 2.3.27 | सर्वनाम्नस्तृतीया च | Partial | SarvanamnasTrtiyaCaSutra.kt |
| 2.3.28 | अपादाने पञ्चमी | Partial | ApadanePancamiSutra.kt |
| 2.3.29 | अन्यारादितरर्तेदिक्छब्दाञ्चूत्तरपदाजाहियुक्ते | Partial | AnyaraditarartedikshabdaSutra.kt |
| 2.3.30 | षष्ठ्यतसर्थप्रत्ययेन | Partial | SasthyAtasarthaPratyayenaSutra.kt |
| 2.3.31 | एनपा द्वितीया | Partial | EnapaDvitiyaSutra.kt |
| 2.3.32 | पृथग्विनानानाभिस्तृतीयान्यतरस्याम् | Partial | PrthagVinaNanabhihSutra.kt |
| 2.3.33 | करणे च स्तोकाल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य | Partial | StokanAlpaKrcchraSutra.kt |
| 2.3.34 | दूरान्तिकार्थैः षष्ठ्यन्यतरस्याम् | Partial | DurantikarthaihSasthiAnyatarasyamSutra.kt |
| 2.3.35 | दूरान्तिकार्थेभ्यो द्वितीया च | Partial | DurantikarthebhyoDvitiyaCaSutra.kt |
| 2.3.36 | सप्तम्यधिकरणे च | Partial | SaptamyAdhikaraneCaSutra.kt |
| 2.3.37 | यस्य च भावेन भावलक्षणम् | Partial | YasyaCaBhavenaBhavalaksanamSutra.kt |
| 2.3.38 | षष्ठी चानादरे | Partial | SasthiCanadareSutra.kt |
| 2.3.39 | स्वामीश्वराधिपतिदायादसाक्षिप्रतिभूप्रसूतैश्च | Partial | SwamyIsvaraAdhipatiSutra.kt |
| 2.3.40 | आयुक्तकुशलाभ्यां चासेवायाम् | Partial | AyuktaKusalabhyamCaSevayamSutra.kt |
| 2.3.41 | यतश्च निर्धारणम् | Partial | YatasCaNirdharanamSutra.kt |
| 2.3.42 | पञ्चमी विभक्ते | Partial | PancamiVibhakteSutra.kt |
| 2.3.43 | साधुनिपुणाभ्यामर्चायां सप्तम्यप्रतेः | Partial | SadhuNipunabhyamSutra.kt |
| 2.3.44 | प्रसितोत्सुकाभ्यां तृतीया च | Partial | PrasitotsukabhyamTrtiyaCaSutra.kt |
| 2.3.45 | नक्षत्रे च लुपि | Partial | NaksatreCaLupiSutra.kt |
| 2.3.46 | प्रातिपदिकार्थलिङ्गपरिमाणवचनमात्रे प्रथमा | Partial | PratipadikarthalingaparimanavacanamatrePrathamaSutra.kt |
| 2.3.47 | सम्बोधने च | Partial | SambodhaneCaSutra.kt |
| 2.3.48 | सामन्त्रितम् | Partial | SamantriteSutra.kt |
| 2.3.49 | एकवचनं सम्बुद्धिः | Partial | EkavacanamSambuddhihSutra.kt |
| 2.3.50 | षष्ठी शेषे | Partial | SasthiSeseSutra.kt |
| 2.3.51 | ज्ञोऽविदर्थस्य करणे | Partial | JnyoAvidarthasyaKaraneSutra.kt |
| 2.3.52 | अधीगर्थदयेशां कर्मणि | Partial | AdhigarthaDayesamKarmaniSutra.kt |
| 2.3.53 | कृञः प्रतियत्ने | Partial | KrjahPratiyatneSutra.kt |
| 2.3.54 | रुजार्थानां भाववचनानामज्वरेः | Partial | RujarthanamBhavavacananamAjvarehSutra.kt |
| 2.3.55 | आशिषि नाथः | Partial | AsisiNathahSutra.kt |
| 2.3.56 | जासिनिप्रहणनाटक्राथपिषां हिंसायाम् | Partial | JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.kt |
| 2.3.57 | व्यवहृपणोः समर्थयोः | Partial | VyavahruPanohSamarthayohSutra.kt |
| 2.3.58 | दिवस्तदर्थस्य | Partial | DivasTadarthasyaSutra.kt |
| 2.3.59 | विभाषोपसर्गे | Partial | VibhasopasargeSutra.kt |
| 2.3.60 | द्वितीया ब्राह्मणे | Partial | DvitiyaBrahmaneSutra.kt |
| 2.3.61 | प्रेष्यब्रुवोर्हविषो देवतासम्प्रदाने | Partial | PresyabruvorHavisoDevatasampradaneSutra.kt |
| 2.3.62 | चतुर्थ्यर्थे बहुलं छन्दसि | Partial | CaturthyartheBahulamChandasiSutra.kt |
| 2.3.63 | यजेश्च करणे | Partial | YajesCaKaraneSutra.kt |
| 2.3.64 | कृत्वोऽर्थप्रयोगे कालेऽधिकरणे | Partial | KrtvorthaPrayogeKaleAdhikaraneSutra.kt |
| 2.3.65 | कर्तृकर्मणोः कृति | Partial | KartrKarmanohKrtiSutra.kt |
| 2.3.66 | उभयप्राप्तौ कर्मणि | Partial | UbhayapraptauKarmaniSutra.kt |
| 2.3.67 | क्तस्य च वर्तमाने | Partial | KasyaCaVartamaneSutra.kt |
| 2.3.68 | अधिकरणवाचिनश्च | Partial | AdhikaranavacinacCaSutra.kt |
| 2.3.69 | न लोकाव्ययनिष्ठाखलर्थतृनाम् | Partial | NaLokavyayanisthanisthakhalarthatrnnamSutra.kt |
| 2.3.70 | अकेनोर्भविष्यदाधमर्ण्ययोः | Partial | AkasyaCaBhavisyadadamarnyayohSutra.kt |
| 2.3.71 | कृत्यानां कर्तरि वा | Partial | KrtyanamKartariVaSutra.kt |
| 2.3.72 | तुल्यार्थैरतुलोपमाभ्यां तृतीयाऽन्यतरस्याम् | Partial | TulyarthairAtulopamabhyamTrtiyanatarasyamSutra.kt |
| 2.3.73 | चतुर्थी चाशिष्यायुष्यमद्रभद्रकुशलसुखार्थहितैः | Partial | CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.kt |

