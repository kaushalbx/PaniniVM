package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.63: भातेर्डवतुः
object BhaterDavatuSutra : UnadiSutra(
    number = "1.63",
    text = "भातेर्डवतुः",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0046" }),
    pratyaya = "डवतु",
    pratyayaSurface = "वत्",
    itMarkers = setOf(ItMarker.DIT, ItMarker.U),
    rootSamjnaMap = mapOf("भा" to Samjna.Rudhi("भवत्")),
    rootMeaningMap = mapOf("भा" to Artha.Explanation("जो प्रकाशित होता है")),
    rootMorphology = mapOf(
        "भा" to UnadiRootMorphology(
            conditionedStem = "भा".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("भवत्".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जो प्रकाशित होता है"), "1.63"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("भवत् डवतु-प्रत्ययान्त प्रातिपदिक है और प्रकाशित होने वाले का वाचक है।"),
    hindiExplanation = "दीप्त्यर्थक अदादिगणीय भा धातु के बाद डवतु प्रत्यय होता है। आरम्भिक ड् इत्संज्ञक है और उसके डित्त्व-सामर्थ्य से भा धातु की टि—यहाँ अन्तिम आ—का लोप होता है; इसे आ-वर्ण के स्पष्ट Varna-लोप के रूप में रखा गया है। प्रत्यय का उ उगित्-सूचक इत् है, इसलिए श्रव्य प्रत्यय वत् रहता है। भ् + वत् से भवत् प्रातिपदिक बनता है और उसके पुल्लिङ्ग प्रथमा-एकवचन का रूप भवान् होता है। वृत्ति का ‘भातीति भवान्’ यहाँ ‘जो प्रकाशित होता है’ अर्थ बताता है; बाद में विकसित आदरसूचक अर्थ इस प्रविष्टि में नहीं मिलाया गया है।",
)
