package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra124Kr = KruDhatu()
private val sutra124Gr = KryadiDhatus.all.single { it.id == "09.0033" }

// 1.24: कृग्रोरुच्च
object KrGrorUcCaSutra : UnadiSutra(
    number = "1.24",
    text = "कृग्रोरुच्च",
    roots = setOf(sutra124Kr, sutra124Gr),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra124Kr.upadesha to Samjna.Rudhi("कुरु"),
        sutra124Gr.upadesha to Samjna.Rudhi("गुरु"),
    ),
    rootMeaningMap = mapOf(
        sutra124Kr.upadesha to Artha.Rudhi("कुरु"),
        sutra124Gr.upadesha to Artha.Rudhi("गुरु"),
    ),
    // उकारादेश is followed by र् (ऋ/ॠ -> उर्), so this is a one-to-many varṇa replacement.
    varnaExpansions = listOf(
        UnadiVarnaExpansion(Svara.R, listOf(Svara.U, Vyanjana.RA)),
        UnadiVarnaExpansion(Svara.RR, listOf(Svara.U, Vyanjana.RA)),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कृ और शब्दार्थक गॄ से क्रमशः कुरु और गुरु शब्द बनते हैं।"),
    hindiExplanation = "कृ और शब्दार्थक गॄ धातुओं के बाद पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। सूत्र का ‘उच्च’ दोनों धातुओं के ऋ अथवा ॠ को उर् में बदलता है; इसे एक वर्ण के बदले उ और र् की क्रमबद्ध वर्ण-माला के रूप में model किया गया है। " +
        "इस प्रकार कृ से कुरु—‘करोति’—और गॄ से गुरु—‘गृणाति’—रूप सिद्ध होते हैं। स्रोत इन दो व्युत्पत्तियों से आगे कोई विशेष रूढ़ार्थ नहीं बताता, इसलिए अतिरिक्त अर्थ नहीं जोड़ा गया है।",
)
