package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiUpapadaFormation

// 1.49: नञि व्यथेः
object NanjiVyathehSutra : UnadiSutra(
    number = "1.49",
    text = "नञि व्यथेः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0868" }),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    upapadas = setOf("नञ्".toVarnas()),
    upapadaFormations = listOf(
        UnadiUpapadaFormation(
            source = "नञ्".toVarnas(),
            output = "अ".toVarnas(),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            sourceRoot = "व्यथँ",
            upapada = "अ".toVarnas(),
            output = "अव्यथिष".toVarnas(),
            meaning = Artha.Explanation("समुद्र अथवा सूर्य"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "अव्यथिष".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("समुद्र अथवा सूर्य"),
            attributedSutra = "1.49",
        ),
        UnadiLexicalAttestation(
            form = "अव्यथिषी".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("पृथ्वी अथवा रात्रि"),
            attributedSutra = "1.49",
        ),
    ),
    relatedSutraNumbers = setOf("6.3.73"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अव्यथिष पुल्लिङ्ग में समुद्र अथवा सूर्य का और अव्यथिषी स्त्रीलिङ्ग में पृथ्वी अथवा रात्रि का वाचक है।"),
    hindiExplanation = "भयसञ्चलन अर्थ वाले भ्वादिगणीय व्यथँ धातु के साथ नञ् उपस्थित होने पर यह सूत्र लागू होता है। ‘नञि’ सप्तमी से नञ् इस प्रत्यय-विधान का अनिवार्य पूर्वपद है, इसलिए उसे व्यथ् धातु का अंश नहीं माना गया है। 6.3.73 ‘नलोपो नञः’ के अनुसार नञ् का नकार लुप्त होकर यहाँ अ रूप रहता है। पूर्वसूत्र से टिषच् प्रत्यय की अनुवृत्ति होती है; उसके ट् और च् इत्संज्ञक होकर लुप्त होते हैं और इष शेष रहता है। इस प्रकार अ + व्यथ् + इष से अव्यथिष प्रातिपदिक बनता है। टित्त्व के कारण स्त्रीप्रत्यय लगने पर अव्यथिषी रूप सिद्ध होता है। वृत्ति अव्यथिष को पुल्लिङ्ग में समुद्र और सूर्य का तथा अव्यथिषी को स्त्रीलिङ्ग में पृथ्वी और रात्रि का वाचक बताती है; अतः दोनों रूप और उनके लिङ्गार्थ अलग-अलग lexical attestations में रखे गए हैं।",
)
