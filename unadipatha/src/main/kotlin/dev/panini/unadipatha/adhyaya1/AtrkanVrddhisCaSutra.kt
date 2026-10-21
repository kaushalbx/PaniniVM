package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.80: आतृकन् वृद्धिश्च
object AtrkanVrddhisCaSutra : UnadiSutra(
    number = "1.80",
    text = "आतृकन् वृद्धिश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0643" }),
    pratyaya = "आतृकन्",
    pratyayaSurface = "आतृक",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("जीवँ" to Samjna.Rudhi("जैवातृक")),
    rootMeaningMap = mapOf("जीवँ" to Artha.Explanation("इन्दु, भिषक्, आयुष्मान् अथवा कृषीवल")),
    rootMorphology = mapOf(
        "जीवँ" to UnadiRootMorphology(
            conditionedStem = "जीव्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.AI)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "जैवातृक".toVarnas(),
            setOf(Linga.PUMS),
            Artha.Explanation("इन्दु, भिषक्, आयुष्मान् अथवा कृषीवल"),
            "1.80",
        ),
    ),
    relatedSutraNumbers = setOf("1.79"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("जैवातृक आतृकन्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और इन्दु, भिषक्, आयुष्मान् अथवा कृषीवल का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से जीवँ धातु की अनुवृत्ति होती है। उसके जीव् आधार के बाद आतृकन् प्रत्यय होता है; प्रत्यय का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और आतृक शेष रहता है। सूत्र का ‘वृद्धिश्च’ जीव् के ई-वर्ण की वृद्धि निर्धारित करता है; ई के स्थान पर ऐ का स्पष्ट Varna-आदेश होने से जीव् आधार जैव् बनता है। जैव् + आतृक के संयोग से जैवातृक प्रातिपदिक सिद्ध होता है। वृत्ति इसे पुल्लिङ्ग में इन्दु अर्थात् चन्द्रमा, भिषक् अर्थात् चिकित्सक, आयुष्मान् तथा कृषीवल अर्थात् किसान का वाचक बताती है; चारों अर्थ सुरक्षित किए गए हैं।",
)
