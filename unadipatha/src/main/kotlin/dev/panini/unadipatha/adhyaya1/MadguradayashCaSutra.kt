package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.41: मद्गुरादयश्च
object MadguradayashCaSutra : UnadiSutra(
    number = "1.41",
    text = "मद्गुरादयश्च",
    roots = setOf(
        DivadiDhatus.all.single { it.id == "04.0105" },
        BhvadiDhatus.all.single { it.id == "01.0440" },
        KryadiDhatus.all.single { it.id == "09.0044" },
        BhvadiDhatus.all.single { it.id == "01.0096" },
    ),
    pratyaya = "उरच्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.CIT),
    rootMorphology = mapOf(
        "मदीँ" to UnadiRootMorphology(
            conditionedStem = "मद्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX, "गुक्"),
            ),
        ),
        "कबृँ" to UnadiRootMorphology(
            conditionedStem = "कब्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.RA), UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, "रुम्"),
            ),
        ),
        "बन्धँ" to UnadiRootMorphology(
            conditionedStem = "बन्ध्".toVarnas(),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.UU)),
        ),
        "कुकँ" to UnadiRootMorphology(
            conditionedStem = "कुक्".toVarnas(),
            optionalVarnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.KA), UnadiAgamaPlacement.BEFORE_SUFFIX, "कुक्"),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "मद्गुर".toVarnas(), meaning = Artha.Rudhi("मद्गुर", "a kind of fish"), sourceRoot = "मदीँ",
        ),
        UnadiDerivedVariant(
            output = "कर्बुर".toVarnas(), meaning = Artha.Explanation("श्वेत अथवा राक्षस"), sourceRoot = "कबृँ",
        ),
        UnadiDerivedVariant(
            output = "बन्धुर".toVarnas(),
            meaning = Artha.Explanation("नम्र अथवा सुन्दर; तीनों लिङ्गों में प्रयुक्त"),
            sourceRoot = "बन्धँ",
        ),
        UnadiDerivedVariant(
            output = "बन्धूर".toVarnas(),
            meaning = Artha.Explanation("नम्र अथवा सुन्दर; तीनों लिङ्गों में प्रयुक्त"),
            sourceRoot = "बन्धँ",
        ),
        UnadiDerivedVariant(output = "कुकुर".toVarnas(), meaning = Artha.Rudhi("कुकुर"), sourceRoot = "कुकँ"),
        UnadiDerivedVariant(output = "कुक्कुर".toVarnas(), meaning = Artha.Rudhi("कुक्कुर"), sourceRoot = "कुकँ"),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मद्गुर आदि उरच्-प्रत्ययान्त शब्द निपातित हैं।"),
    hindiExplanation = "इस सूत्र में मद्गुर आदि उरच्-प्रत्ययान्त शब्द निपातित होते हैं। उरच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है तथा उर शेष रहता है। दिवादिगणीय मदीँ के मद् आधार और प्रत्यय के बीच गुक् का प्रभावी ग् जुड़ने से मत्स्य-विशेषवाचक मद्गुर बनता है। " +
        "वर्ण अर्थ वाले कबृँ के कब् रूप में रुम् का प्रभावी र् अन्तिम मूल स्वर के बाद आता है; कब् से कर्ब् और उर के योग से कर्बुर बनता है, जिसे वृत्ति श्वेत और राक्षस अर्थों में रखती है। बध्नाति रूप देने वाले क्र्यादि बन्धँ से सामान्य उर द्वारा बन्धुर तथा खर्जूरादि-गण के आधार पर उ को ऊ करके बन्धूर मिलता है; दोनों नम्र और सुन्दर अर्थों में तीनों लिङ्गों में प्रयुक्त होते हैं। " +
        "गणसूत्र ‘कोकतेर्वा कुक् च’ के अनुसार कुकँ के कुक् आधार में प्रत्यय से पहले वैकल्पिक कुक्-आगम का प्रभावी क् आने पर कुक्कुर और उसके अभाव में कुकुर बनता है। वृत्ति इन दोनों के लिए अलग अर्थ नहीं बताती, इसलिए कोई अर्थ नहीं गढ़ा गया है।",
)
