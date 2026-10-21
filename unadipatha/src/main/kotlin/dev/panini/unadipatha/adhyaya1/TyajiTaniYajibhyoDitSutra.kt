package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.tanadi.TanDhatu
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSequenceLopa

private val sutra129Tyaj = BhvadiDhatus.all.single { it.id == "01.1141" }
private val sutra129Tan = TanDhatu()
private val sutra129Yaj = BhvadiDhatus.all.single { it.id == "01.1157" }
private val sutra129PronounLingas = setOf(Linga.PUMS, Linga.STRI, Linga.NAPUMSAKA)

// 1.129: त्यजितनियजिभ्यो डित्
object TyajiTaniYajibhyoDitSutra : UnadiSutra(
    number = "1.129",
    text = "त्यजितनियजिभ्यो डित्",
    roots = setOf(sutra129Tyaj, sutra129Tan, sutra129Yaj),
    pratyaya = "अदि",
    pratyayaSurface = "अद्",
    itMarkers = setOf(ItMarker.I, ItMarker.DIT),
    rootSamjnaMap = mapOf(
        sutra129Tyaj.upadesha to Samjna.Rudhi("त्यद्"),
        sutra129Tan.upadesha to Samjna.Rudhi("तद्"),
        sutra129Yaj.upadesha to Samjna.Rudhi("यद्"),
    ),
    rootMorphology = mapOf(
        sutra129Tyaj.upadesha to UnadiRootMorphology(
            conditionedStem = "त्यज्".toVarnas(),
            varnaSequenceLopas = listOf(UnadiVarnaSequenceLopa(listOf(Svara.A, Vyanjana.JA))),
        ),
        sutra129Tan.upadesha to UnadiRootMorphology(
            conditionedStem = "तन्".toVarnas(),
            varnaSequenceLopas = listOf(UnadiVarnaSequenceLopa(listOf(Svara.A, Vyanjana.NA))),
        ),
        sutra129Yaj.upadesha to UnadiRootMorphology(
            conditionedStem = "यज्".toVarnas(),
            varnaSequenceLopas = listOf(UnadiVarnaSequenceLopa(listOf(Svara.A, Vyanjana.JA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("त्यद्".toVarnas(), sutra129PronounLingas, attributedSutra = "1.129"),
        UnadiLexicalAttestation("तद्".toVarnas(), sutra129PronounLingas, attributedSutra = "1.129"),
        UnadiLexicalAttestation("यद्".toVarnas(), sutra129PronounLingas, attributedSutra = "1.129"),
    ),
    relatedSutraNumbers = setOf("1.127"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.SARVANAMA),
    hindiExplanation = "त्यजि, तनि और यजि के रूप में निर्दिष्ट त्यजँ, तनुँ और यजँ धातुओं के बाद पिछले सूत्र से अदि प्रत्यय आता है और यह सूत्र उसे डित् करता है। अदि का इ उच्चारणार्थक इत् है; डित्त्व के कारण धातु के टि-अंश का लोप होता है। त्यज् में अन्तिम अ + ज् के लोप से त्य् बचता है और त्य् + अद् से त्यद् बनता है। तन् में अ + न् के लोप से त् बचता है और त् + अद् से तद् बनता है। यज् में अ + ज् के लोप से य् बचता है और य् + अद् से यद् बनता है। इन टिलोपों को अनुमानित stemादेश न मानकर वास्तविक Varna-श्रृंखला-लोप के रूप में रखा गया है। वृत्ति का ‘सर्वादयः’ कोई चौथा निष्पन्न शब्द नहीं, बल्कि इन शब्दों के सर्वादिगण में पठित होकर सर्वनामसंज्ञक होने का संकेत है; इसलिए सर्व को काल्पनिक output नहीं बनाया गया। तीनों सर्वनाम तीनों लिङ्गों में प्रयुक्त होते हैं, पर इस वृत्ति में उनके पृथक् अर्थ नहीं दिए गए, इसलिए lexical meanings नहीं गढ़े गए।",
)
