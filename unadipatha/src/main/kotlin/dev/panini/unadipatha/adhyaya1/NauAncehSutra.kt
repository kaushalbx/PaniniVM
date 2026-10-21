package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.17: नावञ्चेः
object NauAncehSutra : UnadiSutra(
    number = "1.17",
    text = "नावञ्चेः",
    // The traditional gloss cites अञ्चु गतिपूजनयोः.
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0215" }),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("अन्चुँ" to Samjna.Rudhi("न्यङ्कु")),
    rootMeaningMap = mapOf("अन्चुँ" to Artha.Rudhi("न्यङ्कु", "a kind of deer")),
    upasargas = setOf(listOf(Vyanjana.NA, Svara.I)),
    // न्यङ्क्वादीनां च (A. 7.3.53) supplies the guttural replacement च् -> क्.
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Vyanjana.CA, replacement = Vyanjana.KA),
    ),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 0,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi("न्यङ्कु", "a kind of deer"),
    hindiExplanation = "‘नौ’ पद यहाँ नि उपसर्ग को बताता है। अतः गतिपूजनार्थक अन्चुँ धातु के पहले नि उपसर्ग और धातु के बाद उ प्रत्यय होता है। नि + अञ्च् के मेल से न्यञ्च् बनता है; ‘न्यङ्क्वादीनां च’ ७.३.५३ से च् को क् तथा उसके अनुसार पूर्ववर्ती ञ् को ङ् प्राप्त होकर न्यङ्कु रूप सिद्ध होता है। " +
        "१.९ से अनुवृत्त नित् के कारण प्रथम स्वर उदात्त है। ‘नियतम् अञ्चति’ इस व्युत्पत्ति के साथ न्यङ्कु एक विशेष प्रकार के मृग का नाम है।",
)
