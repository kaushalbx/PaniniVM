package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.75: वसेस्तुन्
object VasesTunSutra : UnadiSutra(
    number = "1.75",
    text = "वसेस्तुन्",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0013" }),
    pratyaya = "तुन्",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.NIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("वसँ" to Samjna.Rudhi("वस्तु")),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("वस्तु".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.75"),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("वस्तु तुन्-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है; वृत्ति यहाँ उसका पृथक् अर्थ नहीं बताती।"),
    hindiExplanation = "निवासार्थक अदादिगणीय वसँ धातु के वस् आधार के बाद तुन् प्रत्यय होता है। तुन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और तु शेष रहता है; 7.2.9 के अनुसार इसके पहले इट्-आगम नहीं होता। वस् + तु में धातु का अन्तिम स् और प्रत्यय का आरम्भिक त् दोनों अपने स्थान पर रहते हैं, जिससे वस्तु प्रातिपदिक सीधे सिद्ध होता है। वृत्ति केवल ‘वस्तु’ रूप देती है; उससे नपुंसकलिङ्ग रूप सुरक्षित किया गया है, लेकिन इस स्थान पर कोई पृथक् अर्थ न दिए जाने से परिचित पदार्थ अथवा सत्ता-अर्थ अनुमान से नहीं जोड़े गए।",
)
