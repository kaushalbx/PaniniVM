package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private fun nyamantaVariant(root: String, output: String, morphology: UnadiRootMorphology? = null) =
    UnadiDerivedVariant(sourceRoot = root, output = output.toVarnas(), morphology = morphology)

private fun nyamantaAttestation(form: String, linga: Linga, meaning: Artha? = null) =
    UnadiLexicalAttestation(form.toVarnas(), setOf(linga), meaning, "1.111")

// 1.111: ञमन्ताड्डः
object NyamantadDahSutra : UnadiSutra(
    number = "1.111",
    text = "ञमन्ताड्डः",
    roots = emptySet(),
    rootReferences = setOf("दण्", "रण्", "खण्", "भण्", "वण्", "अण्", "षणु", "गण्", "चण्", "पण्"),
    pratyaya = "ड",
    pratyayaSurface = "ड",
    rootSamjnaMap = mapOf(
        "दण्" to Samjna.Rudhi("दण्ड"), "रण्" to Samjna.Rudhi("रण्डा"),
        "खण्" to Samjna.Rudhi("खण्ड"), "भण्" to Samjna.Rudhi("भण्ड"),
        "वण्" to Samjna.Rudhi("वण्ड"), "अण्" to Samjna.Rudhi("अण्ड"),
        "षणु" to Samjna.Rudhi("षण्ड"), "गण्" to Samjna.Rudhi("गण्ड"),
        "चण्" to Samjna.Rudhi("चण्ड"), "पण्" to Samjna.Rudhi("पण्ड"),
    ),
    rootMeaningMap = mapOf(
        "वण्" to Artha.Explanation("छिन्नहस्त"),
        "षणु" to Artha.Explanation("सङ्घात"),
        "पण्" to Artha.Explanation("क्लीब; स्त्रीलिङ्ग पण्डा में बुद्धि"),
    ),
    rootMorphology = mapOf(
        "दण्" to UnadiRootMorphology(conditionedStem = "दण्".toVarnas()),
        "रण्" to UnadiRootMorphology(conditionedStem = "रण्".toVarnas()),
        "खण्" to UnadiRootMorphology(conditionedStem = "खण्".toVarnas()),
        "भण्" to UnadiRootMorphology(conditionedStem = "भण्".toVarnas()),
        "वण्" to UnadiRootMorphology(conditionedStem = "वण्".toVarnas()),
        "अण्" to UnadiRootMorphology(conditionedStem = "अण्".toVarnas()),
        "षणु" to UnadiRootMorphology(
            conditionedStem = "षण्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Vyanjana.SSA, Vyanjana.SA),
                UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.SSA),
            ),
        ),
        "गण्" to UnadiRootMorphology(conditionedStem = "गण्".toVarnas()),
        "चण्" to UnadiRootMorphology(conditionedStem = "चण्".toVarnas()),
        "पण्" to UnadiRootMorphology(conditionedStem = "पण्".toVarnas()),
    ),
    derivedVariants = listOf(
        nyamantaVariant("दण्", "दण्ड"), nyamantaVariant("रण्", "रण्डा"),
        nyamantaVariant("खण्", "खण्ड"), nyamantaVariant("भण्", "भण्ड"),
        nyamantaVariant("वण्", "वण्ड"), nyamantaVariant("अण्", "अण्ड"),
        nyamantaVariant("षणु", "षण्ड"),
        nyamantaVariant(
            "षणु",
            "शण्ड",
            UnadiRootMorphology(
                conditionedStem = "षण्".toVarnas(),
                varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.SSA, Vyanjana.SHA)),
            ),
        ),
        nyamantaVariant("गण्", "गण्ड"), nyamantaVariant("चण्", "चण्ड"),
        nyamantaVariant("पण्", "पण्ड"), nyamantaVariant("पण्", "पण्डा"),
    ),
    lexicalAttestations = listOf(
        nyamantaAttestation("दण्ड", Linga.PUMS), nyamantaAttestation("रण्डा", Linga.STRI),
        nyamantaAttestation("खण्ड", Linga.PUMS), nyamantaAttestation("भण्ड", Linga.PUMS),
        nyamantaAttestation("वण्ड", Linga.PUMS, Artha.Explanation("छिन्नहस्त")),
        nyamantaAttestation("अण्ड", Linga.PUMS),
        nyamantaAttestation("षण्ड", Linga.PUMS, Artha.Explanation("सङ्घात")),
        nyamantaAttestation("शण्ड", Linga.PUMS, Artha.Explanation("सङ्घात; तालव्यादि पाठ")),
        nyamantaAttestation("गण्ड", Linga.PUMS), nyamantaAttestation("चण्ड", Linga.PUMS),
        nyamantaAttestation("पण्ड", Linga.PUMS, Artha.Explanation("क्लीब")),
        nyamantaAttestation("पण्डा", Linga.STRI, Artha.Explanation("बुद्धि")),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("ञम्-प्रत्याहार के अन्त्य वर्ण वाले आधारों से ड-प्रत्ययान्त रूढ़ प्रातिपदिक बनते हैं।"),
    hindiExplanation = "ञम्-प्रत्याहार के अन्तर्गत आने वाले अन्त्य नासिक्य-वर्ण वाले आधारों के बाद ड प्रत्यय होता है। ण्-अन्त आधार में ड जुड़ने से ण्ड-संयोग मिलता है और स्रोत दण्ड, रण्डा, खण्ड, भण्ड, वण्ड, अण्ड, षण्ड, गण्ड, चण्ड तथा पण्ड रूप देता है। ये आधार सूत्र में अलग-अलग धातुरूप से नहीं गिनाए गए हैं; इसलिए उन्हें किसी अनिश्चित Dhātupāṭha ID से जोड़ने के बजाय रूप से प्रत्यक्ष दण्, रण् आदि textual references के रूप में सुरक्षित किया गया है। ‘बाहुलकात्सभावः’ के अनुसार षणु-शाखा में पहले ष् का स्-भाव होता है; ण् की सन्निधि में पुनः षत्व से षण्ड परिणाम मिलता है। दोनों चरण पृथक् Varna-आदेशों में रखे गए हैं। ‘तालव्यादिरित्यपरे’ के अनुसार कुछ आचार्य उसी रूप को तालव्य श् से शण्ड पढ़ते हैं; यह ष् से श् का पृथक् Varna-विकल्प है। वृत्ति वण्ड का अर्थ छिन्नहस्त, षण्ड/शण्ड का अर्थ सङ्घात, पुल्लिङ्ग पण्ड का अर्थ क्लीब और स्त्रीलिङ्ग पण्डा का अर्थ बुद्धि बताती है। ‘क्लीब’ यहाँ पण्ड शब्द का वाच्यार्थ है, न कि उसके व्याकरणिक नपुंसकलिङ्ग होने का निर्देश; अतः पण्ड को स्रोत के ‘पण्डः’ के अनुसार पुल्लिङ्ग ही रखा गया है। जिन अन्य रूपों का अर्थ स्रोत नहीं बताता, उनके लिए अर्थ नहीं गढ़ा गया।",
)
