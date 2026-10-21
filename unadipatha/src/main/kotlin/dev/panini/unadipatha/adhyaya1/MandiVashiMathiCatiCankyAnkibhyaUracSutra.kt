package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.38: मन्दिवाशिमथिचतिचङ्क्यङ्किभ्य उरच्
object MandiVashiMathiCatiCankyAnkibhyaUracSutra : UnadiSutra(
    number = "1.38",
    text = "मन्दिवाशिमथिचतिचङ्क्यङ्किभ्य उरच्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0013" },
        DivadiDhatus.all.single { it.id == "04.0059" },
        BhvadiDhatus.all.single { it.id == "01.0044" },
        BhvadiDhatus.all.single { it.id == "01.1003" },
        BhvadiDhatus.all.single { it.id == "01.0092" },
    ),
    rootReferences = setOf("चङ्कि"),
    pratyaya = "उरच्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.CIT),
    rootMorphology = mapOf(
        "मदिँ" to UnadiRootMorphology(conditionedStem = "मन्द्".toVarnas()),
        "वाशृँ" to UnadiRootMorphology(conditionedStem = "वाश्".toVarnas()),
        "मथिँ" to UnadiRootMorphology(conditionedStem = "मथ्".toVarnas()),
        "चतेँ" to UnadiRootMorphology(conditionedStem = "चत्".toVarnas()),
        "चङ्कि" to UnadiRootMorphology(conditionedStem = "चङ्क्".toVarnas()),
        "अकिँ" to UnadiRootMorphology(
            conditionedStem = "अङ्क्".toVarnas(),
            optionalVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.UU)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "मन्दुरा".toVarnas(),
            meaning = Artha.Rudhi("मन्दुरा", "horse stable"),
            sourceRoot = "मदिँ",
        ),
        UnadiDerivedVariant(
            output = "वाशुरा".toVarnas(),
            meaning = Artha.Rudhi("वाशुरा", "night"),
            sourceRoot = "वाशृँ",
        ),
        UnadiDerivedVariant(output = "मथुरा".toVarnas(), meaning = Artha.Rudhi("मथुरा"), sourceRoot = "मथिँ"),
        UnadiDerivedVariant(output = "चतुर".toVarnas(), meaning = Artha.Rudhi("चतुर"), sourceRoot = "चतेँ"),
        UnadiDerivedVariant(
            output = "चङ्कुर".toVarnas(),
            meaning = Artha.Rudhi("चङ्कुर", "chariot"),
            sourceRoot = "चङ्कि",
        ),
        UnadiDerivedVariant(output = "अङ्कुर".toVarnas(), meaning = Artha.Rudhi("अङ्कुर"), sourceRoot = "अकिँ"),
        UnadiDerivedVariant(
            output = "अङ्कूर".toVarnas(),
            meaning = Artha.Rudhi("अङ्कूर", "variant reading"),
            sourceRoot = "अकिँ",
        ),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मन्दुरा आदि रूप उरच् प्रत्यय के साथ निपातित हैं।"),
    hindiExplanation = "मन्दि, वाशि, मथि, चति, चङ्कि और अङ्कि—इन छह धातु-निर्देशों के बाद उरच् प्रत्यय लगाकर वृत्ति में दिए रूप निपातित होते हैं। उरच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और उर शेष रहता है। " +
        "मदिँ के मन्द् रूप से अश्वशालावाचक मन्दुरा, वाशृँ के वाश् रूप से रात्रिवाचक वाशुरा और मथिँ के सूत्रोक्त मथ् रूप से मथुरा बनता है। चतेँ से चतुर तथा स्रोत में स्वतंत्र रूप से उल्लिखित चङ्कि से रथवाचक चङ्कुर सिद्ध होता है। " +
        "अकिँ के अङ्क् रूप में उर जुड़ने पर अङ्कुर बनता है। खर्जूरादि-गण के आधार पर प्रत्यय के उकार का ऊकार होने से अङ्कूर पाठान्तर भी मिलता है। वृत्ति मथुरा, चतुर और अङ्कुर के अलग अर्थ नहीं बताती, इसलिए उनके लिए कोई अर्थ नहीं गढ़ा गया है; और चङ्कि को अनुपलब्ध धातुपाठ-परिचय के स्थान पर पाठगत धातु-सन्दर्भ ही रखा गया है।",
)
