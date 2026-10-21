package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.117: तरत्यादिभ्यश्च
object TaratyadibhyasCaSutra : UnadiSutra(
    number = "1.117",
    text = "तरत्यादिभ्यश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1124" },
        KryadiDhatus.all.single { it.id == "09.0016" },
    ),
    pratyaya = "अङ्गच्",
    pratyayaSurface = "अङ्ग",
    itMarkers = setOf(ItMarker.C),
    rootSamjnaMap = mapOf("तॄ" to Samjna.Rudhi("तरङ्ग"), "लूञ्" to Samjna.Rudhi("लवङ्ग")),
    rootMorphology = mapOf(
        "तॄ" to UnadiRootMorphology(
            conditionedStem = "तॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
        ),
        "लूञ्" to UnadiRootMorphology(
            conditionedStem = "लू".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.UU, listOf(Svara.A, Vyanjana.VA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("तरङ्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.117"),
        UnadiLexicalAttestation("लवङ्ग".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.117"),
    ),
    relatedSutraNumbers = setOf("1.116"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तरङ्ग और लवङ्ग अङ्गच्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "पिछले सूत्र से अङ्गच् प्रत्यय की अनुवृत्ति होती है और ‘च’ से तॄ आदि धातुओं का ग्रहण होता है। अङ्गच् के अन्तिम च् का इत्संज्ञा-लोप होने पर अङ्ग शेष रहता है। तॄ के ॠ का गुण अर्—अर्थात् ॠ का अ + र् Varna-विस्तार—करने पर तर् आधार मिलता है; तर् + अङ्ग से तरङ्ग बनता है। ‘आदि’ में स्रोत से प्रमाणित लूञ् धातु के लू आधार का गुण अव्—ऊ का अ + व् विस्तार—करने पर लव् मिलता है; लव् + अङ्ग से लवङ्ग बनता है। वृत्ति तरङ्गः को पुल्लिङ्ग और लवङ्गम् को नपुंसकलिङ्ग में देती है। वह यहाँ इनके कोशार्थ नहीं बताती, इसलिए लौंग आदि बाहरी कोशार्थ model में नहीं जोड़े गए।",
)
