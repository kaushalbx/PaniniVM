package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.13: ईषेः किच्च
object IshehKicCaSutra : UnadiSutra(
    number = "1.13",
    text = "ईषेः किच्च",
    // The commentary gloss ईषते as हिनस्ति, identifying the गतिहिंसादर्शनेषु entry.
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0695" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    assignedAccent = dev.panini.shiksha.Accent.UDATTA,
    accentVowelIndex = 0,
    rootSamjnaMap = mapOf("ईषँ" to Samjna.Rudhi("इषु")),
    rootMeaningMap = mapOf("ईषँ" to Artha.Rudhi("इषु", "arrow")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Svara.II, replacement = Svara.I),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("इषु", "arrow"),
    hindiExplanation = "गतिहिंसादर्शनार्थक ईषँ धातु के बाद उ प्रत्यय होता है और सूत्र के ‘किच्च’ विधान से यह प्रत्यय कित् माना जाता है। साथ ही धातु के आदि ई-वर्ण के स्थान पर ह्रस्व इ-वर्ण आता है। " +
        "इस प्रकार ईष् से इष् और उ प्रत्यय के योग से इषु शब्द सिद्ध होता है। टीका में ‘ईषते—हिनस्ति’ कहा गया है; इषु रूढ़ रूप से बाण का वाचक है।",
)
