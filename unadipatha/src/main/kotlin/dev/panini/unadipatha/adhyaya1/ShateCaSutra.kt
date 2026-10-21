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

// 1.35: शते च
object ShateCaSutra : UnadiSutra(
    number = "1.35",
    text = "शते च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1095" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.DIT),
    rootMorphology = mapOf(
        "द्रु" to UnadiRootMorphology(
            conditionedStem = "द्रु".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.U)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "शतद्रु".toVarnas(),
            meaning = Artha.Explanation("जो सौ प्रकार से बहता है"),
            upapada = "शत".toVarnas(),
        ),
        UnadiDerivedVariant(
            output = "द्रु".toVarnas(),
            meaning = Artha.Rudhi("द्रु", "tree or branch"),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शत उपपद के साथ शतद्रु तथा बाहुलक से केवल द्रु धातु से वृक्ष अथवा शाखावाचक द्रु सिद्ध होता है।"),
    hindiExplanation = "गतौ अर्थ वाले द्रु धातु के पहले शत उपपद होने पर पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है और पूर्वप्राप्त डित्-धर्म भी बना रहता है। कु का क् इत्संज्ञक होकर लुप्त होता है तथा उ शेष रहता है। " +
        "डित् प्रत्यय के कारण द्रु के अन्तिम उकार का लोप होता है और प्रत्यय का उ जुड़ने पर द्रु पुनः प्राप्त होता है। अतः शत + द्रु से ‘शतधा द्रवति’—जो सौ प्रकार से बहता है—इस विग्रह में शतद्रु सिद्ध होता है। " +
        "वृत्ति बाहुलक से बिना शत उपपद के द्रु रूप भी देती है और उसका अर्थ वृक्ष अथवा शाखा बताती है। वृत्ति का ‘तद्वान् द्रुमः’ वाक्य द्रुम शब्द की अलग सूचना है; उसे इस कु-प्रत्यय का अप्रमाणित प्रत्यक्ष फल नहीं बनाया गया है।",
)
