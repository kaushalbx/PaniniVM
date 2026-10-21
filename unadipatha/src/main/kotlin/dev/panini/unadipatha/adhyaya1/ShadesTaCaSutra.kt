package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.60: शदेस्त च
object ShadesTaCaSutra : UnadiSutra(
    number = "1.60",
    text = "शदेस्त च",
    roots = setOf(TudadiDhatus.all.single { it.id == "06.0164" }),
    pratyaya = "एरक्",
    pratyayaSurface = "एर",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("शदॢँ" to Samjna.Rudhi("शतेर")),
    rootMeaningMap = mapOf("शदॢँ" to Artha.Explanation("शत्रु")),
    rootMorphology = mapOf(
        "शदॢँ" to UnadiRootMorphology(
            conditionedStem = "शद्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "शतेर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("शत्रु"),
            attributedSutra = "1.60",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शतेर एरक्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और शत्रु का वाचक है।"),
    hindiExplanation = "शातनार्थक तुदादिगणीय शदॢँ धातु के शद् आधार के बाद पिछले सूत्र से अनुवृत्त एरक् प्रत्यय होता है। एरक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है और एर शेष रहता है। सूत्र का ‘त्’ धातु के अन्त्य द् के स्थान पर त् का स्पष्ट वर्णादेश करता है; अतः शद् से शत् बनता है। फिर शत् + एर के संयोग से शतेर प्रातिपदिक सिद्ध होता है। वृत्ति ‘शतेरः शत्रुः’ कहती है, इसलिए इसे पुल्लिङ्ग तथा शत्रु-अर्थ में दर्ज किया गया है; कोई अतिरिक्त अर्थ नहीं जोड़ा गया।",
)
