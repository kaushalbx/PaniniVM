package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.12: उन्देरिच्चादेः
object UnderIcCadEhSutra : UnadiSutra(
    number = "1.12",
    text = "उन्देरिच्चादेः",
    roots = setOf(RudhadiDhatus.all.single { it.id == "07.0020" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(dev.panini.core.ItMarker.NIT),
    assignedAccent = dev.panini.shiksha.Accent.UDATTA,
    accentVowelIndex = 0,
    rootSamjnaMap = mapOf("उन्दीँ" to Samjna.Rudhi("इन्दु")),
    rootMeaningMap = mapOf("उन्दीँ" to Artha.Rudhi("इन्दु", "moon")),
    varnaSubstitutions = listOf(
        // इच्चादेः: the initial उ of उन्द् is replaced by इ.
        UnadiVarnaSubstitution(source = Svara.U, replacement = Svara.I),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("इन्दु", "moon"),
    hindiExplanation = "उन्दीँ धातु के बाद पूर्वसूत्र से अनुवृत्त उ प्रत्यय होता है। ‘इच्चादेः’ के अनुसार धातु के आरम्भिक उ-वर्ण के स्थान पर इ-वर्ण आता है; इस प्रकार उन्द् से इन्द् और प्रत्यय के योग से इन्दु रूप बनता है। " +
        "‘उनत्ति’ अर्थात् आर्द्र या तृप्त करता है—इस व्युत्पत्तिगत संकेत के साथ इन्दु रूढ़ रूप से चन्द्रमा का वाचक है।",
)
