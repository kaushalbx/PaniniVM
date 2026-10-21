package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.svadi.VrDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.119: सृवृञोर्वृद्धिश्च
object SrVrnyorVrddhisCaSutra : UnadiSutra(
    number = "1.119",
    text = "सृवृञोर्वृद्धिश्च",
    roots = setOf(SrDhatu(), VrDhatu()),
    pratyaya = "अङ्गच्",
    pratyayaSurface = "अङ्ग",
    itMarkers = setOf(ItMarker.C),
    rootSamjnaMap = mapOf("सृ" to Samjna.Rudhi("सारङ्ग"), "वृञ्" to Samjna.Rudhi("वारङ्ग")),
    rootMeaningMap = mapOf("वृञ्" to Artha.Explanation("खड्गादि की मुष्टि")),
    rootMorphology = mapOf(
        "सृ" to UnadiRootMorphology(
            conditionedStem = "सृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA))),
        ),
        "वृञ्" to UnadiRootMorphology(
            conditionedStem = "वृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सारङ्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.119"),
        UnadiLexicalAttestation("वारङ्ग".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("खड्गादि की मुष्टि"), "1.119"),
    ),
    relatedSutraNumbers = setOf("1.116", "1.117", "1.118"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("सारङ्ग और वारङ्ग वृद्धि-सहित अङ्गच्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "सृ और वृञ् धातुओं के बाद पिछले सूत्रों से अनुवृत्त अङ्गच् प्रत्यय होता है तथा इस सूत्र से दोनों धातुओं के ऋ की वृद्धि होती है। अङ्गच् के अन्तिम च् का इत्संज्ञा-लोप होने पर अङ्ग शेष रहता है। वृद्धि को केवल label न रखकर ऋ का आ + र् में स्पष्ट Varna-विस्तार किया गया है: सृ → सार् और वृ → वार्। फलतः सार् + अङ्ग से सारङ्ग तथा वार् + अङ्ग से वारङ्ग बनते हैं। वृत्ति दोनों को पुल्लिङ्ग में उद्धृत करती है और केवल वारङ्ग का अर्थ ‘खड्गादिमुष्टिः’—तलवार आदि के हत्थे की मुष्टि—बताती है। सारङ्ग के लिए इस स्रोतांश में हरिण आदि कोई अर्थ नहीं दिया गया, इसलिए वह बाहरी अर्थ model में नहीं जोड़ा गया।",
)
