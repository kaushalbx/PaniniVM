package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.37: मृगय्वादयश्च
object MrgayvadayashCaSutra : UnadiSutra(
    number = "1.37",
    text = "मृगय्वादयश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0044" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.DIT),
    rootMorphology = mapOf(
        "या" to UnadiRootMorphology(
            conditionedStem = "या".toVarnas(),
            requiredUpapadas = setOf(
                "मृग".toVarnas(),
                "देव".toVarnas(),
                "मित्र".toVarnas(),
            ),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "मृगयु".toVarnas(),
            meaning = Artha.Rudhi("मृगयु", "hunter"),
            upapada = "मृग".toVarnas(),
            sourceRoot = "या",
        ),
        UnadiDerivedVariant(
            output = "देवयु".toVarnas(),
            meaning = Artha.Rudhi("देवयु", "religious person"),
            upapada = "देव".toVarnas(),
            sourceRoot = "या",
        ),
        UnadiDerivedVariant(
            output = "मित्रयु".toVarnas(),
            meaning = Artha.Rudhi("मित्रयु", "one versed in worldly conduct"),
            upapada = "मित्र".toVarnas(),
            sourceRoot = "या",
        ),
    ),
    isNipatita = true,
    isAkritiGana = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मृगयु आदि कु-प्रत्ययान्त शब्द निपातित आकृतिगण के सदस्य हैं।"),
    hindiExplanation = "इस सूत्र में मृगयु आदि शब्द कु-प्रत्ययान्त रूप में निपातित किए गए हैं। कु का क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। अनुवृत्त डित्-धर्म से या धातु का आकार लुप्त होता है; तब उपपद के बाद बचे य् में उ जुड़कर यु बनता है। " +
        "मृग + या + कु से ‘मृगं याति’—मृग के पीछे जाने वाला—इस विग्रह में व्याधवाचक मृगयु बनता है। वृत्ति देवयु का अर्थ धार्मिक और मित्रयु का अर्थ लोक-व्यवहार का ज्ञाता बताती है; अतः देव और मित्र भी स्पष्ट उपपदों के रूप में सुरक्षित हैं। " +
        "वृत्ति इसे आकृतिगण कहती है, इसलिए model सूची के खुला होने का तथ्य रखता है; किन्तु स्रोत में नाम न दिए गए अन्य सदस्यों या उनके अर्थों की कल्पना नहीं की गई है।",
)
