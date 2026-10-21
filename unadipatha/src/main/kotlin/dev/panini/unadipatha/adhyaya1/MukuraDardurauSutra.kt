package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiReduplication
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.40: मुकुरदर्दुरौ
object MukuraDardurauSutra : UnadiSutra(
    number = "1.40",
    text = "मुकुरदर्दुरौ",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0026" }),
    pratyaya = "उरच्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf("दॄ" to Samjna.Rudhi("दर्दुर")),
    rootMeaningMap = mapOf(
        "दॄ" to Artha.Explanation("दर्दुर शब्द बादल, मेढक, वाद्य-भाण्ड, पर्वत-विशेष, चण्डिका अथवा ग्रामों के जाल का वाचक है।"),
    ),
    rootMorphology = mapOf(
        "दॄ" to UnadiRootMorphology(
            conditionedStem = "दॄ".toVarnas(),
            reduplication = UnadiReduplication(
                explicitDve = true,
                abhyasaAgamaUpadesha = "रुक्",
                abhyasaAgamaVarnas = listOf(Vyanjana.RA),
                tiLopa = true,
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "मुकुर".toVarnas(),
            meaning = Artha.Rudhi("मुकुर", "mirror"),
        ),
        UnadiDerivedVariant(
            output = "मकुर".toVarnas(),
            meaning = Artha.Rudhi("मकुर", "variant of मुकुर: mirror"),
        ),
        UnadiDerivedVariant(
            output = "दर्दुर".toVarnas(),
            meaning = Artha.Explanation("बादल, मेढक, वाद्य-भाण्ड, पर्वत-विशेष, चण्डिका अथवा ग्रामों का जाल"),
            sourceRoot = "दॄ",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मुकुर और दर्दुर उरच्-प्रत्ययान्त रूप हैं; मुकुर का मकुर पाठ भी बाहुलक से मिलता है।"),
    hindiExplanation = "इस सूत्र से मुकुर और दर्दुर रूप उरच् प्रत्यय के साथ सिद्ध होते हैं। उरच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और उर शेष रहता है। मुकुर का अर्थ दर्पण है; वृत्ति उसके लिए कोई धातु नहीं बताती, इसलिए उसे अनुमानित धातु से नहीं जोड़ा गया है। बाहुलक से प्राप्त मकुर रूप भी दर्पणार्थक पाठान्तर के रूप में अलग सुरक्षित है। " +
        "विदारण अर्थ वाले दॄ धातु के लिए वृत्ति पहले धातु का द्विर्वचन, फिर अभ्यास से सम्बद्ध रुक्—जिसका प्रभावी वर्ण र् है—और टि-लोप बताती है। इन स्पष्ट क्रियाओं तथा उर के योग से दर्दुर प्रातिपदिक सिद्ध होता है। " +
        "वृत्ति के अनुसार पुल्लिङ्ग दर्दुर बादल, मेढक, वाद्य-भाण्ड और पर्वत-विशेष का वाचक है; स्त्रीलिङ्ग दर्दुरा चण्डिका तथा नपुंसकलिङ्ग दर्दुर ग्रामों के जाल का वाचक है। ये सभी अर्थ सुरक्षित हैं, पर उनके अतिरिक्त कोई कोशगत अर्थ नहीं जोड़ा गया है।",
)
