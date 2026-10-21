package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.55: कमेः पश्च
object KamehPashCaSutra : UnadiSutra(
    number = "1.55",
    text = "कमेः पश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0511" }),
    pratyaya = "इलच्",
    pratyayaSurface = "इल",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf("कमुँ" to Samjna.Rudhi("कपिल")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.MA, replacement = Vyanjana.PA),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "कपिल".toVarnas(),
            lingas = setOf(Linga.PUMS),
            attributedSutra = "1.55",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कपिल इस सूत्र से सिद्ध इलच्-प्रत्ययान्त प्रातिपदिक है; इस स्रोत-प्रविष्टि में उसका पृथक् अर्थ नहीं दिया गया है।"),
    hindiExplanation = "कान्ति अथवा इच्छा अर्थ वाले भ्वादिगणीय कमुँ धातु के बाद पिछले सूत्र से अनुवृत्त इलच् प्रत्यय होता है। इलच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और इल शेष रहता है। सूत्र का ‘पश्च’ धातु के अन्तिम म् के स्थान पर प् का आदेश करता है; यह प्-आगम नहीं है, क्योंकि आगम मानने पर अनिष्ट कम्पिल रूप बनता। अतः कम् में म् → प् Varna-प्रतिस्थापन से कप् आधार बनता है और कप् + इल से कपिल प्रातिपदिक सिद्ध होता है। वृत्ति केवल ‘कपिलः’ रूप देती है, इसलिए पुल्लिङ्ग सुरक्षित रखा गया है पर कोई अप्रदत्त कोशगत अर्थ अनुमान से नहीं जोड़ा गया।",
)
