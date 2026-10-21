package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra139Av = BhvadiDhatus.all.single { it.id == "01.0684" }

// 1.139: अवतेष्टिलोपश्च
object AvatesTiLopashCaSutra : UnadiSutra(
    number = "1.139",
    text = "अवतेष्टिलोपश्च",
    roots = setOf(sutra139Av),
    pratyaya = "मन्",
    // मन् की टि 'अन्' लुप्त होने पर केवल म् बचता है।
    pratyayaSurface = "म्",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(sutra139Av.upadesha to Samjna.Rudhi("ओम्")),
    rootMeaningMap = mapOf(
        sutra139Av.upadesha to Artha.Explanation("अवतीति—जो रक्षा करता है"),
    ),
    rootMorphology = mapOf(
        sutra139Av.upadesha to UnadiRootMorphology(
            conditionedStem = "अव्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "ओम्".toVarnas(),
            meaning = Artha.Explanation("अवतीति—जो रक्षा करता है"),
            attributedSutra = "1.139",
        ),
    ),
    relatedSutraNumbers = setOf("1.137", "6.4.20"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.AVYAYA),
    meaning = Artha.Explanation("ओम् स्वरादिपाठ से अव्यय है; वृत्ति इसका निर्वचन ‘अवतीति’ करती है।"),
    hindiExplanation = "अवँ धातु के बाद १.१३७ से मन् प्रत्यय आता है। इस सूत्र में कहा गया टि-लोप धातु का नहीं, स्वयं मन् प्रत्यय का है—वृत्ति इसे स्पष्ट करती है और कहती है कि प्रकृति का टि-लोप अभिप्रेत होता तो डित् ही कहा जाता। मन् की टि अर्थात् अन्तिम स्वर से आरम्भ होने वाला अन्-भाग लुप्त होने पर केवल म्-वर्ण बचता है; इसलिए यहाँ प्रत्यय का श्रव्य रूप म् है, १.१३७ के सामान्य म से भिन्न। ६.४.२० के विधान से अव् के अन्तिम व् के स्थान पर ऊठ् का प्रभावी ऊ-वर्ण आता है। अ + ऊ का गुण होकर ओ बनता है और ओ + म् से ओम् सिद्ध होता है। इन क्रियाओं में व् → ऊ को स्पष्ट Varna-आदेश के रूप में रखा गया है। वृत्ति ‘अवतीति ओम्’—जो रक्षा करता है—निर्वचन देती है। वह उज्ज्वलदत्त का चादिपाठ से अव्ययत्व वाला मत अस्वीकार करके अन्ततः स्वरादिपाठ से अव्ययत्व स्वीकार करती है; अतः ओम् को अव्ययसंज्ञा दी गई है और किसी लिङ्ग का अनुमान नहीं किया गया।",
)
