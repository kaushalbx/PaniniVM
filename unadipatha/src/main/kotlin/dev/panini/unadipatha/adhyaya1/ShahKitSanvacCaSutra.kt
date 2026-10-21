package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSanvatTreatment
import dev.panini.unadipatha.UnadiSutra

// 1.20: शः कित्सन्वच्च
object ShahKitSanvacCaSutra : UnadiSutra(
    number = "1.20",
    text = "शः कित्सन्वच्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1118" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("श्यैङ्" to Samjna.Rudhi("शिशु")),
    rootMeaningMap = mapOf("श्यैङ्" to Artha.Rudhi("शिशु", "child")),
    sanvatTreatment = UnadiSanvatTreatment(),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("शिशु", "child"),
    hindiExplanation = "गतिअर्थक श्यैङ् धातु के बाद उ प्रत्यय होता है और ‘कित्’ विधान से यह प्रत्यय कित् माना जाता है। ‘सन्वत्’ का अर्थ है कि प्रकृति पर सन्-प्रत्यय के समान अभ्यासादि प्रक्रियाएँ हों, यद्यपि यहाँ वास्तविक सन् प्रत्यय नहीं लगाया जाता। " +
        "इन प्रक्रियाओं और उ प्रत्यय के योग से शिशु रूप सिद्ध होता है। स्रोत-टीका शिशु का अर्थ बालक देती है; अतः उसी प्रमाणित रूढ़ार्थ को रखा गया है।",
)
