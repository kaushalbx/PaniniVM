package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.EdhDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.78: एधिवह्योश्चतुः
object EdhiVahyosCatuhSutra : UnadiSutra(
    number = "1.78",
    text = "एधिवह्योश्चतुः",
    roots = setOf(
        EdhDhatu(),
        BhvadiDhatus.all.single { it.id == "01.1159" },
    ),
    pratyaya = "चतु",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.CIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf(
        "एधँ" to Samjna.Rudhi("एधतु"),
        "वहँ" to Samjna.Rudhi("वहतु"),
    ),
    rootMeaningMap = mapOf(
        "एधँ" to Artha.Explanation("पुरुष"),
        "वहँ" to Artha.Explanation("अनड्वान् अर्थात् बैल"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("एधतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पुरुष"), "1.78"),
        UnadiLexicalAttestation("वहतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अनड्वान् अर्थात् बैल"), "1.78"),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("एधतु और वहतु चतु-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक हैं।"),
    hindiExplanation = "वृद्ध्यर्थक भ्वादिगणीय एधँ और प्रापणार्थक भ्वादिगणीय वहँ धातुओं के बाद चतु प्रत्यय होता है। चतु का आरम्भिक च् इत्संज्ञक होकर लुप्त होता है और तु शेष रहता है; 7.2.9 के अनुसार इस तु के पहले इट्-आगम नहीं होता। एध् + तु से एधतु और वह् + तु से वहतु रूप बिना किसी अतिरिक्त अप्रमाणित वर्ण-परिवर्तन के सिद्ध होते हैं। वृत्ति एधतु को पुरुष और वहतु को अनड्वान् अर्थात् भारवाही बैल का वाचक बताती है; दोनों रूप पुल्लिङ्ग में सुरक्षित किए गए हैं।",
)
