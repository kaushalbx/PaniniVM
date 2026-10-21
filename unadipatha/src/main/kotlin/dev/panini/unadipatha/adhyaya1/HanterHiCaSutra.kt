package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

private val sutra144Han = AdadiDhatus.all.single { it.id == "02.0002" }

// 1.144: हन्तेर्हि च
object HanterHiCaSutra : UnadiSutra(
    number = "1.144",
    text = "हन्तेर्हि च",
    roots = setOf(sutra144Han),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(sutra144Han.upadesha to Samjna.Rudhi("हिम")),
    rootMorphology = mapOf(
        sutra144Han.upadesha to UnadiRootMorphology(
            conditionedStem = "हन्".toVarnas(),
            stemAdesha = "हि".toVarnas(),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("हिम".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.144"),
    ),
    relatedSutraNumbers = setOf("1.142"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "हिंसा और गति के अर्थ वाले अदादिगणीय हनँ धातु के बाद १.१४२ से मक् प्रत्यय आता है। मक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है, अतः श्रव्य प्रत्यय म है और कित्त्व बना रहता है। इस सूत्र का ‘हि च’ हन् आधार के स्थान पर पूरे हि आधार का आदेश देता है। स्रोत इसे अलग-अलग अ-वर्णादेश और न्-लोप के रूप में नहीं खोलता, इसलिए कृत्रिम चरण गढ़ने के बजाय प्रमाणित हन् → हि stem-आदेश ही रखा गया है। हि + म से हिम प्रातिपदिक सिद्ध होता है। वृत्ति ‘हिमम्’ कहकर इसे नपुंसकलिङ्ग में उद्धृत करती है, पर इस स्थान पर इसका कोई कोशार्थ नहीं देती; इसलिए परिचित हिम अथवा शीत-अर्थ को अनुमान से model में नहीं जोड़ा गया।",
)
