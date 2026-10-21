package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.96: हृषेरुलच्
object HrserUlacSutra : UnadiSutra(
    number = "1.96",
    text = "हृषेरुलच्",
    roots = setOf(
        DivadiDhatus.all.single { it.id == "04.0142" },
        BhvadiDhatus.all.single { it.id == "01.0330" },
    ),
    pratyaya = "उलच्",
    pratyayaSurface = "उल",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "हृषँ" to Samjna.Rudhi("हर्षुल"),
        "चटेँ" to Samjna.Rudhi("चटुल"),
    ),
    rootMeaningMap = mapOf(
        "हृषँ" to Artha.Explanation("मृग अथवा कामी"),
        "चटेँ" to Artha.Explanation("शोभन"),
    ),
    rootMorphology = mapOf(
        "हृषँ" to UnadiRootMorphology(
            conditionedStem = "हृष्".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        "चटेँ" to UnadiRootMorphology(conditionedStem = "चट्".toVarnas()),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "चटुल".toVarnas(),
            sourceRoot = "चटेँ",
            meaning = Artha.Explanation("बाहुलक से शोभन"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("हर्षुल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मृग अथवा कामी"), "1.96"),
        UnadiLexicalAttestation("चटुल".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("शोभन"), "1.96"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("हर्षुल उलच्-प्रत्ययान्त रूप है; बाहुलक से चटुल भी इसी प्रत्यय का प्रमाणित रूप है।"),
    hindiExplanation = "हृषँ धातु के बाद उलच् प्रत्यय होता है; अन्तिम च् इत्संज्ञक होकर लुप्त होता है और उल शेष रहता है। हृष् के ऋ-वर्ण का गुण अर्—अ और र् का Varna-विस्तार—होने पर हर्ष् + उल से हर्षुल बनता है। वृत्ति ‘हर्षुलो मृगकामिनोः’ कहकर इसे मृग और कामी पुरुष के अर्थ में रखती है। वृत्ति बाहुलक से चटतेः भी यही प्रत्यय बताती है; भ्वादि चटेँ का चट् आधार + उल से चटुल बनता है और ‘शोभनम्’ अर्थ मिलता है। इस बाहुलक रूप को मुख्य धातु-विधान में छिपाने के बजाय पृथक् derived variant और lexical attestation के रूप में सुरक्षित किया गया है।",
)
