package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.82: मृजेर्गुणश्च
object MrjerGunasCaSutra : UnadiSutra(
    number = "1.82",
    text = "मृजेर्गुणश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0061" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf("मृजूँ" to Samjna.Rudhi("मर्जू")),
    rootMeaningMap = mapOf("मृजूँ" to Artha.Explanation("शुद्धिकृत् अर्थात् शुद्ध करने वाला")),
    rootMorphology = mapOf(
        "मृजूँ" to UnadiRootMorphology(
            conditionedStem = "मृज्".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("मर्जू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("शुद्धिकृत् अर्थात् शुद्ध करने वाला"), "1.82"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मर्जू ऊ-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और शुद्ध करने वाले का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से ऊ प्रत्यय की अनुवृत्ति होती है। मृजूँ धातु के मृज् आधार में सूत्र का ‘गुणश्च’ ऋ-वर्ण के गुण अर् का विधान करता है; इसे ऋ के स्थान पर अ और र् के क्रमबद्ध Varna-विस्तार के रूप में रखा गया है। इस प्रकार मृज् से मर्ज् बनकर मर्ज् + ऊ = मर्जू प्रातिपदिक सिद्ध होता है। वृत्ति के ‘मर्जूः शुद्धिकृत्’ के अनुसार यह पुल्लिङ्ग में शुद्ध अथवा स्वच्छ करने वाले का वाचक है।",
)
