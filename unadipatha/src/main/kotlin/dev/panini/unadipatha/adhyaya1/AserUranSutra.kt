package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra

// 1.42: असेरुरन्
object AserUranSutra : UnadiSutra(
    number = "1.42",
    text = "असेरुरन्",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0106" }),
    pratyaya = "उरन्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("असुँ" to Samjna.Rudhi("असुर")),
    rootMeaningMap = mapOf("असुँ" to Artha.Rudhi("असुर")),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative(
            base = "असुर".toVarnas(),
            affix = "अण्",
            output = "आसुर".toVarnas(),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("असुर"),
    hindiExplanation = "क्षेपण अर्थ वाले दिवादिगणीय असुँ धातु के बाद उरन् प्रत्यय होता है। प्रत्यय का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और उर शेष रहता है। अस् + उर के संयोग से असुर प्रातिपदिक सिद्ध होता है। वृत्ति असुर का कोई पृथक् अर्थ नहीं बताती, इसलिए model में केवल प्रमाणित रूढ़ शब्द रखा गया है। " +
        "इसके बाद वृत्ति ‘प्रज्ञाद्यण्’ कहती है: प्रज्ञादिगण में पठित असुर शब्द से अण् तद्धित प्रत्यय लगने और आद्य अकार की वृद्धि होने पर आसुर बनता है। आसुर मूल उरन्-प्रत्यय का समानान्तर फल नहीं, बल्कि पहले सिद्ध असुर पर होने वाली गौण तद्धित-व्युत्पत्ति है; इसलिए उसे अलग secondary derivative के रूप में रखा गया है।",
)
