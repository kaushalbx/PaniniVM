package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.unadipatha.UnadiSutra

private val sutra110OutputsBySurface = linkedMapOf(
    "शृ" to "शरु",
    "स्वृ" to "स्वरु",
    "स्निह्" to "स्नेहु",
    "त्रप्" to "त्रपु",
    "अस्" to "असु",
    "वस्" to "वसु",
    "हन्" to "हनु",
    "क्लिद्" to "क्लेदु",
    "बन्ध्" to "बन्धु",
    "मन्" to "मनु",
    "बिन्द्" to "बिन्दु",
)

private val sutra110Roots: Set<Dhatu> = DhatuPathaRegistration.allDhatus.filter { dhatu ->
    dhatu.sourceSurface in sutra110OutputsBySurface &&
        (dhatu.sourceSurface != "बिन्द्" || (dhatu.upadesha == "बिदिँ" && "अवयवे" in dhatu.artha))
}.toSet()

private fun sutra110OutputMap(): Map<String, Samjna.Rudhi> = sutra110Roots.associate { dhatu ->
    dhatu.upadesha to Samjna.Rudhi(sutra110OutputsBySurface.getValue(dhatu.sourceSurface))
}

private fun sutra110MeaningMap(): Map<String, Artha> = sutra110Roots.associate { dhatu ->
    val output = sutra110OutputsBySurface.getValue(dhatu.sourceSurface)
    dhatu.upadesha to Artha.Rudhi(output)
}

// 1.10: शॄस्वृस्निहित्रप्यसिवसिहनिक्लिदिबन्धिमनिभ्यश्च
object ShrSvrSnihiTrapyAsiVasiHaniKlidiBandhiManibhyashCaSutra : UnadiSutra(
    number = "1.10",
    text = "शॄस्वृस्निहित्रप्यसिवसिहनिक्लिदिबन्धिमनिभ्यश्च",
    roots = sutra110Roots,
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(dev.panini.core.ItMarker.NIT),
    assignedAccent = dev.panini.shiksha.Accent.UDATTA,
    accentVowelIndex = 0,
    rootSamjnaMap = sutra110OutputMap(),
    rootMeaningMap = sutra110MeaningMap(),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("इस सूत्र में प्रत्येक धातु से प्राप्त शब्द का रूढ़ अर्थ पृथक् है।"),
    hindiExplanation = "शॄ, स्वृ, स्निह्, त्रप्, अस्, वस्, हन्, क्लिद्, बन्ध् और मन् धातुओं के बाद उ प्रत्यय होता है। इससे क्रमशः शरु, स्वरु, स्नेहु, त्रपु, असु, वसु, हनु, क्लेदु, बन्धु और मनु शब्द सिद्ध होते हैं। " +
        "सूत्र के ‘च’ से अवयव-अर्थ वाले बिद् धातु का ग्रहण होता है और उससे बिन्दु बनता है। जहाँ मूल पाठ किसी समानरूप धातु का गण नहीं बताता, वहाँ उपलब्ध सभी समानरूप Dhātupāṭha प्रविष्टियाँ रखी गई हैं; बिद् को भाष्य में दिए ‘अवयवे’ अर्थ से निश्चित किया गया है।",
)
