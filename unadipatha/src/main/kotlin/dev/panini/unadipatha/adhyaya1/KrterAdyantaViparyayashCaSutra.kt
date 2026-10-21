package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaViparyaya

// 1.16: कृतेराद्यन्तविपर्ययश्च
object KrterAdyantaViparyayashCaSutra : UnadiSutra(
    number = "1.16",
    text = "कृतेराद्यन्तविपर्ययश्च",
    // The output denotes a winding implement, hence कृतीँ वेष्टने rather than the homonymous छेदने root.
    roots = setOf(RudhadiDhatus.all.single { it.id == "07.0010" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("कृतीँ" to Samjna.Rudhi("तर्कु")),
    rootMeaningMap = mapOf("कृतीँ" to Artha.Rudhi("तर्कु", "spindle for winding thread")),
    varnaViparyayas = listOf(
        UnadiVarnaViparyaya(initial = Vyanjana.KA, final = Vyanjana.TA),
    ),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 0,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("तर्कु", "spindle for winding thread"),
    hindiExplanation = "वेष्टनार्थक कृतीँ धातु के बाद उ प्रत्यय होता है। ‘आद्यन्तविपर्ययः’ के विधान से धातु के आदि क् और अन्त्य त् अपने स्थान परस्पर बदलते हैं; कृ‌त् इस प्रकार तृक् बनता है और गुण तथा उ प्रत्यय के योग से तर्कु रूप सिद्ध होता है। " +
        "१.९ से अनुवृत्त नित् के कारण इसका प्रथम स्वर उदात्त है। तर्कु उस तकुए अथवा उपकरण का नाम है जिस पर सूत्र यानी धागा लपेटा जाता है।",
)
