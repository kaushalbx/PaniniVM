package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.26: रपेरिच्चोपधायाः
object RaperIcCopadhayahSutra : UnadiSutra(
    number = "1.26",
    text = "रपेरिच्चोपधायाः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0467" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("रपँ" to Samjna.Rudhi("रिपु")),
    rootMeaningMap = mapOf("रपँ" to Artha.Rudhi("रिपु", "enemy")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Svara.A, replacement = Svara.I),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("रिपु", "enemy"),
    hindiExplanation = "व्यक्त वाणी के अर्थ वाले रपँ धातु के बाद पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। ‘इच्चोपधायाः’ के अनुसार रप् की उपधा अ के स्थान पर इ आता है; अतः रप् से रिप् और उ प्रत्यय के योग से रिपु रूप सिद्ध होता है। " +
        "स्रोत की ‘अनिष्टं रपतीति रिपुः’ व्युत्पत्ति के अनुसार रिपु अनिष्ट बोलने अथवा करने वाले शत्रु का वाचक है।",
)
