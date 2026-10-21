package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.76: अगारे णिच्च
object AgareNicCaSutra : UnadiSutra(
    number = "1.76",
    text = "अगारे णिच्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0013" }),
    pratyaya = "तुन्",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.NIT),
    blocksItAgama = true,
    applicationMeaning = Artha.Explanation("अगार अर्थात् गृह अथवा गृहभूमि"),
    rootSamjnaMap = mapOf("वसँ" to Samjna.Rudhi("वास्तु")),
    rootMeaningMap = mapOf("वसँ" to Artha.Explanation("वेश्म अथवा गृहभूमि")),
    rootMorphology = mapOf(
        "वसँ" to UnadiRootMorphology(
            conditionedStem = "वस्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("वास्तु".toVarnas(), setOf(Linga.PUMS, Linga.NAPUMSAKA), Artha.Explanation("वेश्म अथवा गृहभूमि"), "1.76"),
    ),
    relatedSutraNumbers = setOf("1.75", "7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("वास्तु अगारार्थ में णित् तुन्-प्रत्ययान्त प्रातिपदिक है और स्त्रीलिङ्ग से भिन्न है।"),
    hindiExplanation = "अगार अर्थात् वेश्म या गृहभूमि के अर्थ में अदादिगणीय वसँ धातु के वस् आधार के बाद पिछले सूत्र से तुन् प्रत्यय आता है। तुन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और तु शेष रहता है; 7.2.9 के अनुसार इट्-आगम नहीं होता। सूत्र का ‘णिच्च’ इस प्रयोग में णित्त्व बताता है, जिससे वस् के अ-वर्ण की वृद्धि होकर आ आता है। इस स्पष्ट Varna-आदेश से वस् का वास् आधार बनता है और वास् + तु से वास्तु सिद्ध होता है। वृत्ति ‘वेश्मभूर्वास्तुरस्त्रियाम्’ कहती है, इसलिए वास्तु को वेश्म अथवा गृहभूमि के अर्थ में तथा स्त्रीलिङ्ग से भिन्न—पुल्लिङ्ग और नपुंसकलिङ्ग—रखा गया है।",
)
