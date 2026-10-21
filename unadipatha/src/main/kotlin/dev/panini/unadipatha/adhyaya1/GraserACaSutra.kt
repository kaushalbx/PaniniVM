package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra140Gras = BhvadiDhatus.all.single { it.id == "01.0717" }

// 1.140: ग्रसेरा च
object GraserACaSutra : UnadiSutra(
    number = "1.140",
    text = "ग्रसेरा च",
    roots = setOf(sutra140Gras),
    pratyaya = "मन्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(sutra140Gras.upadesha to Samjna.Rudhi("ग्राम")),
    rootMorphology = mapOf(
        sutra140Gras.upadesha to UnadiRootMorphology(
            conditionedStem = "ग्रस्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Vyanjana.SA, Svara.AA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "ग्राम".toVarnas(),
            lingas = setOf(Linga.PUMS),
            attributedSutra = "1.140",
        ),
    ),
    relatedSutraNumbers = setOf("1.137"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "अदन अर्थात् खाने के अर्थ वाले भ्वादिगणीय ग्रसुँ धातु के ग्रस् आधार के बाद १.१३७ से मन् प्रत्यय आता है। मन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है, इसलिए उसका श्रव्य रूप म रहता है। सूत्र का ‘आ’ धातु के अन्तिम स्-वर्ण के स्थान पर आता है; इसे स् → आ के स्पष्ट Varna-आदेश के रूप में रखा गया है। ग्रस् में अन्तिम स् के स्थान पर आ आने पर ग्रअ + आ बनता है और समीपवर्ती सवर्ण अ तथा आ के दीर्घ-सन्धि से ग्रा आधार मिलता है। ग्रा + म से ग्राम प्रातिपदिक सिद्ध होता है। वृत्ति केवल ‘ग्रामः’ रूप देती है, अतः उसका पुल्लिङ्ग सुरक्षित किया गया है; इस स्रोतांश में ग्राम का कोई पृथक् कोशार्थ नहीं दिया गया, इसलिए परिचित अर्थ अनुमान से नहीं जोड़ा गया।",
)
