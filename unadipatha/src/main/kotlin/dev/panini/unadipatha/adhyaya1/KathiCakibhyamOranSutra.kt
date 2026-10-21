package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.64: कठिचकिभ्यामोरन्
object KathiCakibhyamOranSutra : UnadiSutra(
    number = "1.64",
    text = "कठिचकिभ्यामोरन्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0385" },
        BhvadiDhatus.all.single { it.id == "01.0098" },
    ),
    pratyaya = "ओरन्",
    pratyayaSurface = "ओर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "कठँ" to Samjna.Rudhi("कठोर"),
        "चकँ" to Samjna.Rudhi("चकोर"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कठोर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.64"),
        UnadiLexicalAttestation("चकोर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.64"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कठोर और चकोर ओरन्-प्रत्ययान्त प्रातिपदिक हैं; वृत्ति इस स्थान पर उनके पृथक् अर्थ नहीं बताती।"),
    hindiExplanation = "सूत्र कठि और चकि के बाद ओरन् प्रत्यय विधान करता है। ओरन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और श्रव्य अंश ओर रहता है। कृच्छ्रजीवनार्थक भ्वादिगणीय कठँ के कठ् आधार में अन्तिम हल् का प्रत्यय के आरम्भिक स्वर से संयोग होने पर कठ् + ओर से कठोर बनता है। तृप्ति और प्रतिघात अर्थ वाले भ्वादिगणीय चकँ के चक् आधार से इसी प्रकार चक् + ओर से चकोर बनता है। वृत्ति केवल ‘कठोरः। चकोरः।’ रूप देती है; इसलिए दोनों का पुल्लिङ्ग सुरक्षित है, पर यहाँ न दिए गए प्रचलित गुण अथवा पक्षी-अर्थ अनुमान से नहीं जोड़े गए।",
)
