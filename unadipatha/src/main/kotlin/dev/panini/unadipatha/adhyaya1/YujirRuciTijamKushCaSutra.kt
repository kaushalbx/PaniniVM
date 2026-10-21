package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra143Yuj = YujirDhatu()
private val sutra143Ruc = BhvadiDhatus.all.single { it.id == "01.0847" }
private val sutra143TijBhvadi = BhvadiDhatus.all.single { it.id == "01.1126" }
private val sutra143TijCuradi = CuradiDhatus.all.single { it.id == "10.0154" }

// 1.143: युजिरुचितिजां कुश्च
object YujirRuciTijamKushCaSutra : UnadiSutra(
    number = "1.143",
    text = "युजिरुचितिजां कुश्च",
    roots = setOf(sutra143Yuj, sutra143Ruc, sutra143TijBhvadi, sutra143TijCuradi),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra143Yuj.upadesha to Samjna.Rudhi("युग्म"),
        sutra143Ruc.upadesha to Samjna.Rudhi("रुक्म"),
        sutra143TijBhvadi.upadesha to Samjna.Rudhi("तिग्म"),
    ),
    rootMorphology = mapOf(
        sutra143Yuj.upadesha to UnadiRootMorphology(
            conditionedStem = "युज्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA)),
        ),
        sutra143Ruc.upadesha to UnadiRootMorphology(
            conditionedStem = "रुच्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.CA, Vyanjana.KA)),
        ),
        sutra143TijBhvadi.upadesha to UnadiRootMorphology(
            conditionedStem = "तिज्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("युग्म".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.143"),
        UnadiLexicalAttestation("रुक्म".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.143"),
        UnadiLexicalAttestation("तिग्म".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.143"),
    ),
    relatedSutraNumbers = setOf("1.142"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "युजिँर्, रुचँ और तिजँ धातुओं के बाद पिछले सूत्र १.१४२ से मक् प्रत्यय आता है। मक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है, इसलिए श्रव्य प्रत्यय म है और उसका कित्त्व बना रहता है। इस सूत्र का ‘कुश्च’ कोई पृथक् कु-आगम नहीं, बल्कि धातु के अन्तिम तालव्य वर्ण का कुत्व-विधान है। युज् में ज् के स्थान पर उसी वर्ग का कण्ठ्य ग् आता है: युज् → युग्; युग् + म से युग्म बनता है। रुच् में च् के स्थान पर क् आता है: रुच् → रुक्; रुक् + म से रुक्म बनता है। तिज् में ज् → ग् होकर तिग् + म से तिग्म सिद्ध होता है। तिजँ की भ्वादि और चुरादि दोनों प्रविष्टियाँ निशान-अर्थ से सम्बद्ध हैं और सूत्र कोई गण-प्रतिबन्ध नहीं देता, इसलिए दोनों सुरक्षित हैं। वृत्ति तीनों रूप नपुंसकलिङ्ग में उद्धृत करती है, पर इस स्थान पर उनका कोई कोशार्थ नहीं बताती; अतः युग्म, सुवर्ण अथवा तीक्ष्ण जैसे परिचित अर्थ अनुमान से नहीं जोड़े गए। सभी ध्वनि-परिवर्तन स्पष्ट Varna-आदेश के रूप में रखे गए हैं।",
)
