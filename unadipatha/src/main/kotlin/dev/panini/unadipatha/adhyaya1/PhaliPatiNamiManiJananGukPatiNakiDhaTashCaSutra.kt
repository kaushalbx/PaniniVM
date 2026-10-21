package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.SanadiAffix
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra118Phali = BhvadiDhatus.all.single { it.id == "01.0608" }
private val sutra118Pati = BhvadiDhatus.all.single { it.id == "01.0333" }
private val sutra118Nami = BhvadiDhatus.all.single { it.id == "01.1136" }
private val sutra118Mani = DivadiDhatus.all.single { it.id == "04.0073" }
private val sutra118Jani = DivadiDhatus.all.single { it.id == "04.0044" }

// 1.18: फलिपाटिनमिमनिजनां गुक्पटिनाकिधतश्च
object PhaliPatiNamiManiJananGukPatiNakiDhaTashCaSutra : UnadiSutra(
    number = "1.18",
    text = "फलिपाटिनमिमनिजनां गुक्पटिनाकिधतश्च",
    roots = setOf(sutra118Phali, sutra118Pati, sutra118Nami, sutra118Mani, sutra118Jani),
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        sutra118Phali.upadesha to Samjna.Rudhi("फल्गु"),
        sutra118Pati.upadesha to Samjna.Rudhi("पटु"),
        sutra118Nami.upadesha to Samjna.Rudhi("नाकु"),
        sutra118Mani.upadesha to Samjna.Rudhi("मधु"),
        sutra118Jani.upadesha to Samjna.Rudhi("जतु"),
    ),
    rootMeaningMap = mapOf(
        sutra118Phali.upadesha to Artha.Rudhi("फल्गु"),
        sutra118Pati.upadesha to Artha.Rudhi("पटु"),
        sutra118Nami.upadesha to Artha.Rudhi("नाकु", "anthill"),
        sutra118Mani.upadesha to Artha.Rudhi("मधु"),
        sutra118Jani.upadesha to Artha.Rudhi("जतु"),
    ),
    rootMorphology = mapOf(
        sutra118Phali.upadesha to UnadiRootMorphology(
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.GA), UnadiAgamaPlacement.BEFORE_SUFFIX)),
        ),
        sutra118Pati.upadesha to UnadiRootMorphology(
            conditionedStem = "पाटि".toVarnas(),
            requiredSanadiAffixes = setOf(SanadiAffix.NIC),
            stemAdesha = "पट्".toVarnas(),
        ),
        sutra118Nami.upadesha to UnadiRootMorphology(stemAdesha = "नाक्".toVarnas()),
        sutra118Mani.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.DHA)),
        ),
        sutra118Jani.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.TA)),
        ),
    ),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 0,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("इस सूत्र में प्रत्येक धातु से अपना पृथक रूढ़ शब्द बनता है।"),
    hindiExplanation = "फलँ, णिच्-युक्त पटँ, णमँ, मनँ और जनीँ—इन पाँच धातुओं के बाद उ प्रत्यय होता है। फल् में गुक् का प्रभावी ग् आगम होकर फल्गु बनता है। पट् के णिच्-युक्त पाटि रूप के स्थान पर पट् आदेश होकर पटु बनता है। " +
        "णमँ के स्थान पर नाक् आदेश से नाकु अर्थात् वल्मीक बनता है। मनँ के अन्तिम न् को ध् करने से मधु और जनीँ के अन्तिम न् को त् करने से जतु सिद्ध होता है। १.९ से अनुवृत्त नित् के कारण इन सभी शब्दों का प्रथम स्वर उदात्त है।",
)
