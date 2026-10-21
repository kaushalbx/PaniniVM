package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.104: कलस्तृपश्च
object KalasTrpasCaSutra : UnadiSutra(
    number = "1.104",
    text = "कलस्तृपश्च",
    roots = setOf(
        TudadiDhatus.all.single { it.id == "06.0028" },
        TudadiDhatus.all.single { it.id == "06.0030" },
    ),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "तृपँ" to Samjna.Rudhi("तृपला"),
        "तृफँ" to Samjna.Rudhi("त्रिफला"),
    ),
    rootMeaningMap = mapOf(
        "तृपँ" to Artha.Explanation("लता"),
        "तृफँ" to Artha.Explanation("फलत्रिक"),
    ),
    rootMorphology = mapOf(
        "तृपँ" to UnadiRootMorphology(conditionedStem = "तृप्".toVarnas()),
        "तृफँ" to UnadiRootMorphology(conditionedStem = "तृफ्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("तृपला".toVarnas(), setOf(Linga.STRI), Artha.Explanation("लता"), "1.104"),
        UnadiLexicalAttestation("त्रिफला".toVarnas(), setOf(Linga.STRI), Artha.Explanation("फलत्रिक"), "1.104"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तृपला और त्रिफला कित् कल-प्रत्ययान्त स्त्रीलिङ्ग प्रातिपदिक हैं।"),
    hindiExplanation = "तृपँ धातु तथा चकार से तृफँ धातु के बाद कल प्रत्यय होता है। प्रत्यय का प्रारम्भिक क् लशक्वतद्धिते से इत्संज्ञक होकर लुप्त होता है और अल शेष रहता है। तृप् + अल से तृपल आधार और स्त्रीरूप तृपला बनता है, जिसे वृत्ति लता बताती है। तृफँ से वृत्ति सीधे त्रिफला रूप देती है और उसका अर्थ फलत्रिक बताती है। तृफ् से त्रिफ् बनने की आन्तरिक प्रक्रिया सूत्र या वृत्ति में नहीं खोली गई है, इसलिए प्रमाणित त्रिफला परिणाम रखा गया है पर कोई काल्पनिक संप्रसारण अथवा वर्णादेश नहीं गढ़ा गया।",
)
