package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.SanadiAffix
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.98: ताडेर्णिलुक् च
object TaderNiLukCaSutra : UnadiSutra(
    number = "1.98",
    text = "ताडेर्णिलुक् च",
    roots = setOf(CuradiDhatus.all.single { it.id == "10.0064" }),
    pratyaya = "इति",
    pratyayaSurface = "इत्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf("तडँ" to Samjna.Rudhi("तडित्")),
    rootMorphology = mapOf(
        "तडँ" to UnadiRootMorphology(
            conditionedStem = "तड्".toVarnas(),
            requiredSanadiAffixes = setOf(SanadiAffix.NIC),
            lopaSanadiAffixes = setOf(SanadiAffix.NIC),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("तडित्".toVarnas(), attributedSutra = "1.98"),
    ),
    relatedSutraNumbers = setOf("1.97"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तडित् णिच्-लुक् के बाद प्राप्त इति-प्रत्ययान्त प्रातिपदिक है।"),
    hindiExplanation = "तडँ धातु पहले णिच्-सहित होकर ताडयति रूप में प्रयुक्त होती है; सूत्र का ‘णिलुक्’ इसी णिच् प्रत्यय का सम्पूर्ण लुक् करता है। इसे केवल तैयार तड् आधार मानकर छोड़ने के बजाय requiredSanadiAffixes और lopaSanadiAffixes—दोनों में णिच् दर्ज किया गया है, जिससे आगमन और फिर सूत्रविहित लोप अलग-अलग स्पष्ट रहते हैं। पिछले सूत्र से इकारानुबन्धक इति प्रत्यय आता है; अन्तिम इ लुप्त होने पर इत् शेष रहता है। णिच्-लुक् के बाद तड् + इत् से तडित् सिद्ध होता है। वृत्ति केवल ‘ताडयतीति तडित्’ कहती है; यहाँ कोई विशिष्ट कोशार्थ या लिङ्ग नहीं बताती, इसलिए बिजली आदि प्रसिद्ध अर्थ अनुमान से नहीं जोड़े गए।",
)
