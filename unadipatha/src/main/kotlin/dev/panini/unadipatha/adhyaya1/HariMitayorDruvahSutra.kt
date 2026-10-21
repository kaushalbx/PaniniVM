package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.34: हरिमितयोर्द्रुवः
object HariMitayorDruvahSutra : UnadiSutra(
    number = "1.34",
    text = "हरिमितयोर्द्रुवः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1095" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.DIT),
    rootMorphology = mapOf(
        "द्रु" to UnadiRootMorphology(
            conditionedStem = "द्रु".toVarnas(),
            requiredUpapadas = setOf("हरि".toVarnas(), "मित".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.U)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "हरिद्रु".toVarnas(),
            meaning = Artha.Rudhi("हरिद्रु", "tree"),
            upapada = "हरि".toVarnas(),
        ),
        UnadiDerivedVariant(
            output = "मितद्रु".toVarnas(),
            meaning = Artha.Rudhi("मितद्रु", "ocean"),
            upapada = "मित".toVarnas(),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("हरि उपपद से वृक्षवाचक हरिद्रु और मित उपपद से समुद्रवाचक मितद्रु सिद्ध होते हैं।"),
    hindiExplanation = "गतौ अर्थ वाले द्रु धातु के पहले हरि अथवा मित उपपद होने पर पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है और उसका डित्-धर्म भी बना रहता है। कु का क् इत्संज्ञक होकर लुप्त होता है तथा उ शेष रहता है। " +
        "डित् प्रत्यय के कारण द्रु धातु के अन्तिम उकार का लोप होता है; फिर प्रत्यय का उ जुड़ने पर पुनः द्रु रूप प्राप्त होता है। हरि + द्रु से ‘हरिभिः द्रूयते’ इस विग्रह में वृक्षवाचक हरिद्रु और मित + द्रु से ‘मितं द्रवति’ इस विग्रह में समुद्रवाचक मितद्रु सिद्ध होता है। दोनों उपपद और दोनों फल अलग प्रमाणित रूपों के रूप में रखे गए हैं।",
)
