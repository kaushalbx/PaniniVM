package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSequenceLopa

// 1.33: आङ्परयोः खनिशॄभ्यां डिच्च
object AngParayohKhaniShrbhyamDicCaSutra : UnadiSutra(
    number = "1.33",
    text = "आङ्परयोः खनिशॄभ्यां डिच्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1020" },
        KryadiDhatus.all.single { it.id == "09.0015" },
    ),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT, ItMarker.DIT),
    rootSamjnaMap = mapOf(
        "खनुँ" to Samjna.Rudhi("आखु"),
        "शॄञ्" to Samjna.Rudhi("परशु"),
    ),
    rootMeaningMap = mapOf(
        "खनुँ" to Artha.Explanation("जो खोदता है"),
        "शॄञ्" to Artha.Explanation("जो दूसरे को काटता अथवा नष्ट करता है"),
    ),
    rootMorphology = mapOf(
        "खनुँ" to UnadiRootMorphology(
            conditionedStem = "खन्".toVarnas(),
            requiredUpasargas = setOf(listOf(Svara.AA)),
            varnaSequenceLopas = listOf(
                UnadiVarnaSequenceLopa(listOf(Svara.A, Vyanjana.NA)),
            ),
        ),
        "शॄञ्" to UnadiRootMorphology(
            conditionedStem = "शॄ".toVarnas(),
            requiredUpapadas = setOf("पर".toVarnas()),
            varnaSequenceLopas = listOf(
                UnadiVarnaSequenceLopa(listOf(Svara.RR)),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "पर्शु".toVarnas(),
            upapada = "पर".toVarnas(),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.Karaka.KARTA),
    meaning = Artha.Karaka.KARTA,
    hindiExplanation = "आङ् उपसर्ग के साथ खनुँ धातु और पर उपपद के साथ शॄञ् धातु के बाद पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है। कु का क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है; इस सूत्र में प्रत्यय को डित् भी किया गया है। " +
        "डित् के कारण खन् के अन्तिम अकार और नकार का लोप होकर ख् बचता है; आ उपसर्ग के योग से आ + ख् + उ से आखु बनता है—‘जो खोदता है’। शॄ के ॠ का लोप होकर श् बचता है; पर उपपद के साथ पर + श् + उ से परशु बनता है—‘जो दूसरे को काटता अथवा नष्ट करता है’। " +
        "वृत्ति में पृषोदरादि-गण के आधार पर पर के अकार-लोप से पर्शु रूप भी विकल्प से दिया गया है; इसे पृथक् प्रमाणित रूप के रूप में रखा गया है।",
)
