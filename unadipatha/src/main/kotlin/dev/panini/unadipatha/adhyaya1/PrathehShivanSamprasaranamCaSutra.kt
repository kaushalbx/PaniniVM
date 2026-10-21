package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra148PrathBhvadi = BhvadiDhatus.all.single { it.id == "01.0869" }
private val sutra148PrathCuradi = CuradiDhatus.all.single { it.id == "10.0027" }

// 1.148: प्रथेः षिवन् संप्रसारणं च
object PrathehShivanSamprasaranamCaSutra : UnadiSutra(
    number = "1.148",
    text = "प्रथेः षिवन् संप्रसारणं च",
    roots = setOf(sutra148PrathBhvadi, sutra148PrathCuradi),
    pratyaya = "षिवन्",
    pratyayaSurface = "इव",
    itMarkers = setOf(ItMarker.SH, ItMarker.N),
    rootSamjnaMap = mapOf("प्रथँ" to Samjna.Rudhi("पृथिवी")),
    rootMorphology = mapOf(
        "प्रथँ" to UnadiRootMorphology(
            conditionedStem = "प्रथ्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(output = "पृथवी".toVarnas(), sourceRoot = "प्रथँ"),
        UnadiDerivedVariant(output = "पृथिवी".toVarnas(), sourceRoot = "प्रथँ"),
        UnadiDerivedVariant(output = "पृथ्वी".toVarnas(), sourceRoot = "प्रथँ"),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative(
            base = "पृथिव".toVarnas(),
            affix = "ङीष्",
            output = "पृथिवी".toVarnas(),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पृथवी".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.148"),
        UnadiLexicalAttestation("पृथिवी".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.148"),
        UnadiLexicalAttestation("पृथ्वी".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.148"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "प्रख्यानार्थक प्रथँ धातु के बाद षिवन् प्रत्यय होता है और धातु में सम्प्रसारण होता है। षिवन् का आरम्भिक ष् इत्संज्ञक होकर प्रत्यय को षित् बनाता है तथा अन्तिम न् भी इत्संज्ञक होकर लुप्त होता है; इसलिए उणादि प्रत्यय का श्रव्य अंश इव है। सम्प्रसारण से प्रथ् के र् के स्थान पर ऋ आता है—इसे र् → ऋ के स्पष्ट Varna-आदेश से रखा गया है—और पृथ् आधार बनता है। पृथ् + इव से पृथिव प्रातिपदिक बनता है। षित्त्व के कारण स्त्रीवाचक ङीष् लगने पर पृथिव + ई से पृथिवी सिद्ध होती है; इस परवर्ती चरण को secondary derivative के रूप में अलग दर्ज किया गया है। सूत्र गणभेद नहीं करता, इसलिए प्रथँ की भ्वादि और चुरादि दोनों प्रविष्टियाँ सुरक्षित हैं। वेबसाइट-वृत्ति का ‘पवन्नित्येके’ पारम्परिक मुद्रित पाठ में ‘षवन्नित्येके’—अर्थात् प्रत्यय का पाठान्तर—मिलता है; उसे पवन् नामक काल्पनिक output नहीं बनाया गया। शब्दार्णव से उद्धृत पृथवी, पृथिवी और पृथ्वी तीनों स्त्रीलिङ्ग पाठ सुरक्षित हैं। इस स्रोतांश में पृथ्वी का कोशार्थ नहीं दिया गया, इसलिए परिचित अर्थ अनुमान से नहीं जोड़ा गया।",
)
