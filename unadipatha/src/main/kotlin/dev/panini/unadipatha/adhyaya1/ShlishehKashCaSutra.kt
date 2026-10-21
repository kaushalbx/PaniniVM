package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.32: श्लिषेः कश्च
object ShlishehKashCaSutra : UnadiSutra(
    number = "1.32",
    text = "श्लिषेः कश्च",
    // श्लिष्यति in the gloss identifies the embracing/attaching Divādi root, not the homonymous दाह root.
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0083" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("श्लिषँ" to Samjna.Rudhi("श्लिकु")),
    rootMeaningMap = mapOf("श्लिषँ" to Artha.Rudhi("श्लिकु", "servant or raised flame")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.SSA, replacement = Vyanjana.KA),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("श्लिकु", "servant or raised flame"),
    hindiExplanation = "आलिङ्गन अर्थ वाले दिवादिगणीय श्लिषँ धातु के बाद अनुवृत्त कु प्रत्यय होता है। प्रत्यय का क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। सूत्र के ‘कश्च’ विधान से धातु के अन्तिम ष् के स्थान पर क् आता है; इस प्रकार श्लिष् से श्लिक् और उ प्रत्यय के योग से श्लिकु रूप सिद्ध होता है। " +
        "स्रोत श्लिकु के दो अर्थ देता है—भृत्य तथा ऊपर उठी हुई ज्योति—और दोनों को उसी एक रूढ़ार्थ-प्रविष्टि में सुरक्षित रखा गया है।",
)
