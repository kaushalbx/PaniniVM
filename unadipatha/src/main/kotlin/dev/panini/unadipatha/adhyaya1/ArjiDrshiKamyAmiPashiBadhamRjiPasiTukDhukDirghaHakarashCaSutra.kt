package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra127Arj = BhvadiDhatus.all.single { it.id == "01.0256" }
private val sutra127Drsh = DrshDhatu()
private val sutra127Kam = BhvadiDhatus.all.single { it.id == "01.0511" }
private val sutra127AmiRoots = setOf(
    BhvadiDhatus.all.single { it.id == "01.0536" },
    CuradiDhatus.all.single { it.id == "10.0245" },
)
private val sutra127Badh = BhvadiDhatus.all.single { it.id == "01.0005" }

// 1.27: अर्जिदृशिकम्यमिपशिबाधामृजिपसितुक्धुक्दीर्घहकाराश्च
object ArjiDrshiKamyAmiPashiBadhamRjiPasiTukDhukDirghaHakarashCaSutra : UnadiSutra(
    number = "1.27",
    text = "अर्जिदृशिकम्यमिपशिबाधामृजिपसितुक्धुक्दीर्घहकाराश्च",
    roots = setOf(sutra127Arj, sutra127Drsh, sutra127Kam, sutra127Badh) + sutra127AmiRoots,
    // पशि नाशने is explicitly identified as a sūtra-root in the commentary and is absent from this Dhātupāṭha.
    rootReferences = setOf("पशि"),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra127Arj.upadesha to Samjna.Rudhi("ऋजु"),
        sutra127Drsh.upadesha to Samjna.Rudhi("पशु"),
        sutra127Kam.upadesha to Samjna.Rudhi("कन्तु"),
        "अमँ" to Samjna.Rudhi("अन्धु"),
        "पशि" to Samjna.Rudhi("पांशु"),
        sutra127Badh.upadesha to Samjna.Rudhi("बाहु"),
    ),
    rootMeaningMap = mapOf(
        sutra127Arj.upadesha to Artha.Rudhi("ऋजु", "straight or upright"),
        sutra127Drsh.upadesha to Artha.Rudhi("पशु", "animal"),
        sutra127Kam.upadesha to Artha.Rudhi("कन्तु", "Kandarpa"),
        "अमँ" to Artha.Rudhi("अन्धु", "well"),
        "पशि" to Artha.Rudhi("पांशु", "dust"),
        sutra127Badh.upadesha to Artha.Rudhi("बाहु", "arm"),
    ),
    rootMorphology = mapOf(
        sutra127Arj.upadesha to UnadiRootMorphology(stemAdesha = "ऋज्".toVarnas()),
        sutra127Drsh.upadesha to UnadiRootMorphology(stemAdesha = "पश्".toVarnas()),
        sutra127Kam.upadesha to UnadiRootMorphology(
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.TA), UnadiAgamaPlacement.BEFORE_SUFFIX)),
        ),
        "अमँ" to UnadiRootMorphology(
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.DHA), UnadiAgamaPlacement.BEFORE_SUFFIX)),
        ),
        "पशि" to UnadiRootMorphology(
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.A, listOf(Svara.AA))),
        ),
        sutra127Badh.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.DHA, Vyanjana.HA)),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("छः सूत्रनिर्दिष्ट धातुओं से छः पृथक् रूढ़ शब्द बनते हैं।"),
    hindiExplanation = "इस सूत्र में अर्ज्, दृश्, कम्, अम्, सौत्र पशि और बाध्—इन छः धातुओं के बाद कु प्रत्यय होता है; क् इत्संज्ञक होकर उ शेष रहता है। अर्ज् के स्थान पर ऋज् आदेश से ऋजु और दृश् के स्थान पर पश् आदेश से पशु बनता है। कम् में त्-आगम से कन्तु तथा अम् में ध्-आगम से अन्धु सिद्ध होता है। " +
        "सौत्र पशि के स्वर की दीर्घता से पांशु और बाध् के ध् को ह् आदेश करने से बाहु बनता है। टीका अम् के रोग तथा गति—दोनों पाठ स्वीकारती है, इसलिए दोनों धातुपाठ-प्रविष्टियाँ रखी गई हैं। अर्थ क्रमशः सीधा, पशु, कन्दर्प, कूप, धूलि और भुजा हैं।",
)
