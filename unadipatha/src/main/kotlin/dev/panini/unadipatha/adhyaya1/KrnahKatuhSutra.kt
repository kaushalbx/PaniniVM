package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.77: कृञः कतुः
object KrnahKatuhSutra : UnadiSutra(
    number = "1.77",
    text = "कृञः कतुः",
    roots = setOf(SvadiDhatus.all.single { it.id == "05.0007" }),
    pratyaya = "कतु",
    pratyayaSurface = "अतु",
    itMarkers = setOf(ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("कृञ्" to Samjna.Rudhi("क्रतु")),
    rootMeaningMap = mapOf("कृञ्" to Artha.Explanation("यज्ञ")),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("क्रतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("यज्ञ"), "1.77"),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("क्रतु कतु-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और यज्ञ का वाचक है।"),
    hindiExplanation = "करणार्थक स्वादिगणीय कृञ् धातु के कृ आधार के बाद कतु प्रत्यय होता है। कतु का आरम्भिक क् इत्संज्ञक होकर लुप्त होता है और श्रव्य प्रत्यय अतु बचता है; कित्त्व के कारण ऋ का गुण नहीं होता और 7.2.9 के अनुसार इट्-आगम भी नहीं होता। कृ के ऋ के स्थान पर स्वरादि अतु के सामने नियमित यण् र् आता है; इसलिए कृ + अतु से क्रतु प्रातिपदिक सिद्ध होता है। वृत्ति ‘क्रतुर्यज्ञः’ कहती है, अतः इसे पुल्लिङ्ग और यज्ञ-अर्थ में रखा गया है; अन्य कोशगत अर्थ नहीं जोड़े गए।",
)
