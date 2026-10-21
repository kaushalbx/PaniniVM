package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.102: शमेः खः
object SamehKhahSutra : UnadiSutra(
    number = "1.102",
    text = "शमेः खः",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0098" }),
    pratyaya = "ख",
    pratyayaSurface = "ख",
    rootSamjnaMap = mapOf("शमुँ" to Samjna.Rudhi("शङ्ख")),
    rootMorphology = mapOf(
        "शमुँ" to UnadiRootMorphology(
            conditionedStem = "शम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NGA)),
        ),
    ),
    lexicalAttestations = listOf(UnadiLexicalAttestation("शङ्ख".toVarnas(), attributedSutra = "1.102")),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शङ्ख ख-प्रत्ययान्त प्रातिपदिक है।"),
    hindiExplanation = "शमुँ धातु के शम् आधार के बाद ख प्रत्यय होता है। कण्ठ्य ख् के पहले म् का कण्ठ्य परसवर्ण ङ् होता है; इसे म् के स्थान पर ङ् का स्पष्ट Varna-आदेश बनाया गया है। फलतः शम् + ख से शङ्ख प्रातिपदिक सिद्ध होता है। वृत्ति यहाँ केवल ‘शङ्खः’ रूप देती है, उसका विशेष अर्थ या लिङ्ग नहीं बताती; इसलिए प्रसिद्ध वस्तु-अर्थ और लिङ्ग अनुमान से नहीं जोड़े गए।",
)
