package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.14: स्कन्देः सलोपश्च
object SkandehSaLopashCaSutra : UnadiSutra(
    number = "1.14",
    text = "स्कन्देः सलोपश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1134" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(dev.panini.core.ItMarker.NIT),
    assignedAccent = dev.panini.shiksha.Accent.UDATTA,
    accentVowelIndex = 0,
    rootSamjnaMap = mapOf("स्कन्दिँर्" to Samjna.Rudhi("कन्दु")),
    rootMeaningMap = mapOf("स्कन्दिँर्" to Artha.Rudhi("कन्दु", "a conventional lexical form")),
    varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.SA)),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("कन्दु", "a conventional lexical form"),
    hindiExplanation = "स्कन्दिँर् धातु के बाद पूर्वसूत्र से अनुवृत्त उ प्रत्यय होता है। ‘सलोपश्च’ के अनुसार धातु के आरम्भ में स्थित स्-वर्ण का लोप किया जाता है; इसलिए स्कन्द् से कन्द् शेष रहता है। " +
        "उसके बाद उ प्रत्यय जुड़ने पर कन्दु रूप सिद्ध होता है। स्रोत-टीका केवल ‘कन्दुः’ रूप देती है, इसलिए इसके लिए कोई अनुल्लिखित विशेष अर्थ नहीं जोड़ा गया है।",
)
