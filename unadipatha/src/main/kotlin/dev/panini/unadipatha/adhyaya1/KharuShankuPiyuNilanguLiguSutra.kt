package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.36: खरु शङ्कु पीयु नीलङ्गु लिगु
object KharuShankuPiyuNilanguLiguSutra : UnadiSutra(
    number = "1.36",
    text = "खरु शङ्कु पीयु नीलङ्गु लिगु",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1020" },
        AdadiDhatus.all.single { it.id == "02.0051" },
        BhvadiDhatus.all.single { it.id == "01.0154" },
        BhvadiDhatus.all.single { it.id == "01.0895" },
    ),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.DIT),
    rootMorphology = mapOf(
        "खनुँ" to UnadiRootMorphology(
            conditionedStem = "खन्".toVarnas(),
            stemAdesha = "खर्".toVarnas(),
        ),
        "पा" to UnadiRootMorphology(
            conditionedStem = "पिब्".toVarnas(),
            stemAdesha = "पी".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.YA), UnadiAgamaPlacement.BEFORE_SUFFIX),
            ),
        ),
        "लगिँ" to UnadiRootMorphology(
            conditionedStem = "लङ्ग्".toVarnas(),
            requiredUpasargas = setOf("नि".toVarnas()),
        ),
        "लगेँ" to UnadiRootMorphology(
            conditionedStem = "लग्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.I)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "खरु".toVarnas(),
            meaning = Artha.Rudhi("खरु", "desire, cruel person, fool, or horse"),
            sourceRoot = "खनुँ",
        ),
        UnadiDerivedVariant(
            output = "शङ्कु".toVarnas(),
            meaning = Artha.Rudhi("शङ्कु", "peg or dart"),
        ),
        UnadiDerivedVariant(
            output = "पीयु".toVarnas(),
            meaning = Artha.Rudhi("पीयु", "crow, time, or gold"),
            sourceRoot = "पा",
        ),
        UnadiDerivedVariant(
            upasarga = "नि".toVarnas(),
            output = "नीलङ्गु".toVarnas(),
            meaning = Artha.Rudhi("नीलङ्गु", "a particular worm or a jackal"),
            sourceRoot = "लगिँ",
        ),
        UnadiDerivedVariant(
            upasarga = "नि".toVarnas(),
            output = "नीलाङ्गु".toVarnas(),
            meaning = Artha.Rudhi("नीलाङ्गु", "variant reading: a particular worm or a jackal"),
            sourceRoot = "लगिँ",
        ),
        UnadiDerivedVariant(
            output = "लिगु".toVarnas(),
            meaning = Artha.Rudhi("लिगु", "mind or fool"),
            sourceRoot = "लगेँ",
        ),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("खरु, शङ्कु, पीयु, नीलङ्गु और लिगु—ये पाँच कु-प्रत्ययान्त रूप निपातित हैं।"),
    hindiExplanation = "इस सूत्र में खरु, शङ्कु, पीयु, नीलङ्गु और लिगु—इन पाँच कु-प्रत्ययान्त शब्दों को निपातित किया गया है। कु का क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है; निपातन के कारण वृत्ति में कही गई विशेष रूप-व्यवस्था ही ग्रहण की जाती है। " +
        "खनुँ के अन्त्य नकार के स्थान पर रेफ रखकर खर् + उ से खरु बनता है; इसके अर्थ काम, क्रूर, मूर्ख और अश्व हैं। शङ्कु का कोई धातु वृत्ति में नहीं बताया गया, अतः उसे अनुमानित धातु से न जोड़कर कील और शल्य अर्थ वाला स्वतंत्र निपातित रूप रखा गया है। " +
        "पिब् रूप वाले पा धातु में इत्व तथा युगागम सहित निपातित पी + य् + उ से पीयु बनता है; इसके अर्थ वायस, काल और सुवर्ण हैं। गतिवाचक लगिँ के पहले नि उपसर्ग में दीर्घ होकर नी तथा लङ्ग् + उ के योग से नीलङ्गु बनता है; इसका अर्थ विशेष कृमि अथवा शृगाल है। वृत्ति का नीलाङ्गु पाठान्तर भी अलग सुरक्षित है, जिसमें धातु का अकार दीर्घ है। " +
        "सङ्गार्थक लगेँ के अकार को इकार करके लिग् + उ से लिगु बनता है; वृत्ति इसे चित्त और मूर्ख अर्थों में बताती है।",
)
