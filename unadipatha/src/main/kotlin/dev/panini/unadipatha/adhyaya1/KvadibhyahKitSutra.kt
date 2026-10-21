package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.112: क्वादिभ्यः कित्
object KvadibhyahKitSutra : UnadiSutra(
    number = "1.112",
    text = "क्वादिभ्यः कित्",
    roots = setOf(
        TudadiDhatus.all.single { it.id == "06.0061" }, // कुणँ शब्दोपकरणयोः
        BhvadiDhatus.all.single { it.id == "01.0511" }, // कमुँ कान्तौ
        BhvadiDhatus.all.single { it.id == "01.1100" }, // गुङ् अव्यक्ते शब्दे
        TudadiDhatus.all.single { it.id == "06.0064" }, // घुणँ भ्रमणे
    ),
    pratyaya = "ड",
    pratyayaSurface = "ड",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "कुणँ" to Samjna.Rudhi("कुण्ड"),
        "कमुँ" to Samjna.Rudhi("काण्ड"),
        "गुङ्" to Samjna.Rudhi("गुड"),
        "घुणँ" to Samjna.Rudhi("घुण्ड"),
    ),
    rootMeaningMap = mapOf("घुणँ" to Artha.Explanation("भ्रमर")),
    rootMorphology = mapOf(
        "कुणँ" to UnadiRootMorphology(conditionedStem = "कुण्".toVarnas()),
        "कमुँ" to UnadiRootMorphology(
            conditionedStem = "कम्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.A, Svara.AA),
                UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NNA),
            ),
        ),
        "गुङ्" to UnadiRootMorphology(conditionedStem = "गु".toVarnas()),
        "घुणँ" to UnadiRootMorphology(conditionedStem = "घुण्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कुण्ड".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.112"),
        UnadiLexicalAttestation("काण्ड".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.112"),
        UnadiLexicalAttestation("गुड".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.112"),
        UnadiLexicalAttestation("घुण्ड".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("भ्रमर"), "1.112"),
    ),
    relatedSutraNumbers = setOf("1.111"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कवर्गादि धातुओं से कित् ड-प्रत्ययान्त प्रातिपदिक बनते हैं।"),
    hindiExplanation = "‘क्वादिभ्यः’ की वृत्ति ‘कवर्गादिभ्यः’ कहती है: क्, ख्, ग्, घ् अथवा ङ् से आरम्भ होने वाले धातुओं के बाद पिछले सूत्र का ड प्रत्यय होता है और वह कित् होता है। कुणँ शब्दोपकरणयोः के कुण् आधार में ड जुड़कर कुण्ड बनता है। कमुँ कान्तौ में अनुनासिकान्त आधार का अ दीर्घ आ होता है और म् ण् में बदलकर काण् आधार देता है; काण् + ड = काण्ड। गुङ् के इत् ङ् के लोप से गु आधार रहता है और गु + ड = गुड होता है। घुणँ भ्रमणे के घुण् आधार से घुण्ड बनता है, जिसे वृत्ति भ्रमर बताती है। स्रोत कुण्डम् और काण्डम् को नपुंसकलिङ्ग तथा गुडः और घुण्डः को पुल्लिङ्ग में उद्धृत करता है। घुण्ड के भ्रमर-अर्थ के अतिरिक्त इस प्रविष्टि में कोई अर्थ नहीं दिया गया, इसलिए बाकी अर्थ अनुमान से नहीं जोड़े गए।",
)
