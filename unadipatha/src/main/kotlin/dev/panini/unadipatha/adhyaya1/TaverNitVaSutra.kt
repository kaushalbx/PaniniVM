package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.48: तवेर्णिद्वा
object TaverNitVaSutra : UnadiSutra(
    number = "1.48",
    text = "तवेर्णिद्वा",
    roots = emptySet(),
    rootReferences = setOf("तव"),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    optionalItMarkers = setOf(ItMarker.NIT),
    rootMorphology = mapOf(
        "तव" to UnadiRootMorphology(
            conditionedStem = "तव".toVarnas(),
            optionalVarnaSubstitutions = listOf(
                UnadiVarnaSubstitution(source = Svara.A, replacement = Svara.AA),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            sourceRoot = "तव",
            output = "तविष".toVarnas(),
            meaning = Artha.Explanation("समुद्र अथवा स्वर्ग"),
        ),
        UnadiDerivedVariant(
            sourceRoot = "तव",
            output = "ताविष".toVarnas(),
            meaning = Artha.Explanation("समुद्र अथवा स्वर्ग"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "तविष".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("समुद्र अथवा स्वर्ग"),
            attributedSutra = "1.48",
        ),
        UnadiLexicalAttestation(
            form = "ताविष".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("समुद्र अथवा स्वर्ग"),
            attributedSutra = "1.48",
        ),
        UnadiLexicalAttestation(
            form = "तविषी".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("नदी, देवकन्या, भूमि अथवा बल"),
            attributedSutra = "1.48",
        ),
        UnadiLexicalAttestation(
            form = "ताविषी".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("नदी, देवकन्या अथवा भूमि"),
            attributedSutra = "1.48",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तविष और ताविष समुद्र अथवा स्वर्ग के वाचक हैं; स्त्रीलिङ्ग के तविषी और ताविषी नदी, देवकन्या अथवा भूमि के वाचक हैं, तथा वैदिक व्याख्या में तविषी बल का भी वाचक है।"),
    hindiExplanation = "यहाँ ‘तव’ धातुपाठ में प्राप्त सामान्य धातु नहीं, बल्कि उणादिवृत्ति द्वारा स्पष्ट किया गया सौत्र धातु है। पूर्वसूत्र से टिषच् प्रत्यय की अनुवृत्ति होती है। टिषच् में ट् और च् इत्संज्ञक होकर लुप्त होते हैं, इसलिए प्रत्यय का श्रव्य अंश इष बचता है। णित्त्व विकल्प से है: जिस पक्ष में णित् नहीं माना जाता, वहाँ तव् + इष से तविष बनता है; जिस पक्ष में प्रत्यय णित् माना जाता है, वहाँ धातु के अकार की वृद्धि आ होकर ताविष बनता है। अतः optionalItMarkers में णित् और उसी वैकल्पिक शाखा में अ से आ का Varna-परिवर्तन रखा गया है। वृत्ति के अनुसार तविष और ताविष पुल्लिङ्ग में समुद्र तथा स्वर्ग के वाचक हैं। टित्त्व के कारण स्त्रीप्रत्यय लगने पर तविषी और ताविषी रूप होते हैं; ये नदी, देवकन्या और भूमि के वाचक हैं। वेदभाष्य में विशेषतः तविषी का अर्थ बल भी दिया गया है।",
)
