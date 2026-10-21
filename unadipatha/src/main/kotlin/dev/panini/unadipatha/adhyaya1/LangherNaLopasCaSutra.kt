package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

private val sutra132Langh = BhvadiDhatus.all.single { it.id == "01.0113" }

// 1.132: लङ्घेर्नलोपश्च
object LangherNaLopasCaSutra : UnadiSutra(
    number = "1.132",
    text = "लङ्घेर्नलोपश्च",
    roots = setOf(sutra132Langh),
    pratyaya = "अटि",
    pratyayaSurface = "अट्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra132Langh.upadesha to Samjna.Rudhi("लघट्")),
    rootMeaningMap = mapOf(sutra132Langh.upadesha to Artha.Explanation("वायु")),
    rootMorphology = mapOf(
        sutra132Langh.upadesha to UnadiRootMorphology(
            conditionedStem = "लङ्घ्".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.NGA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("लघट्".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("वायु"), "1.132"),
    ),
    relatedSutraNumbers = setOf("1.131"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("लघट् वायु का वाचक अटि-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "गतिऔर भोजननिवृत्ति अर्थ वाले लघिँ धातु का धातुपाठीय आधार लङ्घ् है। इसके बाद पिछले सूत्र १.१३१ से अटि प्रत्यय आता है; अटि का अन्तिम इ इत्संज्ञक होकर लुप्त होता है और अट् शेष रहता है। सूत्र का ‘नलोप’ यहाँ लङ्घ् के नासिक्य ङ्-वर्ण का लोप कराता है। इसे पूरे stem का अनुमानित आदेश बनाने के बजाय ङ् के स्पष्ट Varna-lopa के रूप में रखा गया है। ङ् हटने पर लघ् आधार बचता है और लघ् + अट् से लघट् बनता है। वृत्ति ‘लघट् वायुः’ कहती है, इसलिए इसका प्रमाणित अर्थ वायु और लिङ्ग पुल्लिङ्ग रखा गया है; कोई अतिरिक्त अर्थ नहीं जोड़ा गया।",
)
