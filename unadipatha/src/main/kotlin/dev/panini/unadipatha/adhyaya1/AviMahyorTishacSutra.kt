package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

// 1.45: अविमह्योष्टिषच्
object AviMahyorTishacSutra : UnadiSutra(
    number = "1.45",
    text = "अविमह्योष्टिषच्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0684" },
        BhvadiDhatus.all.single { it.id == "01.0831" },
    ),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "अवँ" to Samjna.Rudhi("अविष"),
        "महँ" to Samjna.Rudhi("महिष"),
    ),
    rootMeaningMap = mapOf(
        "अवँ" to Artha.Rudhi("अविष"),
        "महँ" to Artha.Rudhi("महिष"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अवँ से अविष और महँ से महिष शब्द सिद्ध होते हैं।"),
    hindiExplanation = "भ्वादिगणीय अवँ और महँ धातुओं के बाद टिषच् प्रत्यय होता है। प्रत्यय का आरम्भिक ट् ‘चुटू’ से और अन्तिम च् ‘हलन्त्यम्’ से इत्संज्ञक होकर लुप्त होते हैं; इनके बाद इष शेष रहता है। " +
        "अवँ धातु का इत्-वर्ण हटने पर अव् आधार मिलता है और इष के योग से अविष प्रातिपदिक सिद्ध होता है। इसी प्रकार पूजार्थक महँ का मह् आधार इष से मिलकर महिष बनाता है। वृत्ति केवल ‘अविषः’ और ‘महिषः’ रूप देती है, उनके पृथक् अर्थ नहीं बताती; इसलिए model में इन रूढ़ शब्दों के लिए कोई अतिरिक्त कोशगत अर्थ नहीं गढ़ा गया है।",
)
