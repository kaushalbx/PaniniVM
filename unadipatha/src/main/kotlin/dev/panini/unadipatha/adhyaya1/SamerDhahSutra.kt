package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.99: शमेर्ढः
object SamerDhahSutra : UnadiSutra(
    number = "1.99",
    text = "शमेर्ढः",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0098" }),
    pratyaya = "ढ",
    pratyayaSurface = "ढ",
    rootSamjnaMap = mapOf("शमुँ" to Samjna.Rudhi("शण्ढ")),
    rootMeaningMap = mapOf("शमुँ" to Artha.Explanation("गोपति अथवा क्लीब")),
    rootMorphology = mapOf(
        "शमुँ" to UnadiRootMorphology(
            conditionedStem = "शम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NNA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शण्ढ".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गोपति"), "1.99"),
        UnadiLexicalAttestation("शण्ढ".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("क्लीब"), "1.99"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शण्ढ ढ-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और गोपति अथवा क्लीब का वाचक है।"),
    hindiExplanation = "शमुँ धातु के शम् आधार के बाद ढ प्रत्यय होता है। मूर्धन्य ढ् के पहले म् का उसी उच्चारणस्थान वाला परसवर्ण ण् होता है; इसे म् से ण् का स्पष्ट Varna-आदेश रखा गया है। फलतः शम् + ढ से शण्ढ प्रातिपदिक सिद्ध होता है। वृत्ति पहले ‘पुंसि गोपतौ’ कहकर पुल्लिङ्ग में गोपति का अर्थ देती है और फिर ‘शण्ढः क्लीबः’ कहकर क्लीब व्यक्ति का दूसरा अर्थ बताती है। ‘क्लीब’ यहाँ अर्थ है, न कि स्वतः नपुंसकलिङ्ग का निर्देश; इसलिए दोनों अर्थ अलग पुल्लिङ्ग attestations में रखे गए हैं।",
)
