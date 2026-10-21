package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra128PrathiRoots = setOf(
    BhvadiDhatus.all.single { it.id == "01.0869" },
    CuradiDhatus.all.single { it.id == "10.0027" },
)
private val sutra128Mradi = BhvadiDhatus.all.single { it.id == "01.0871" }
private val sutra128Bhrasj = TudadiDhatus.all.single { it.id == "06.0004" }

// 1.28: प्रथिम्रदिभ्रस्जां संप्रसारणं सलोपश्च
object PrathiMradiBhrasjamSamprasaranamSaLopashCaSutra : UnadiSutra(
    number = "1.28",
    text = "प्रथिम्रदिभ्रस्जां संप्रसारणं सलोपश्च",
    roots = sutra128PrathiRoots + setOf(sutra128Mradi, sutra128Bhrasj),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "प्रथँ" to Samjna.Rudhi("पृथु"),
        sutra128Mradi.upadesha to Samjna.Rudhi("मृदु"),
        sutra128Bhrasj.upadesha to Samjna.Rudhi("भृगु"),
    ),
    rootMeaningMap = mapOf(
        "प्रथँ" to Artha.Rudhi("पृथु"),
        sutra128Mradi.upadesha to Artha.Rudhi("मृदु"),
        sutra128Bhrasj.upadesha to Artha.Rudhi("भृगु"),
    ),
    rootMorphology = mapOf(
        "प्रथँ" to UnadiRootMorphology(
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R)),
        ),
        sutra128Mradi.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R)),
        ),
        sutra128Bhrasj.upadesha to UnadiRootMorphology(
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Vyanjana.RA, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.GA),
            ),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.SA)),
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("प्रथ्, म्रद् और भ्रस्ज् से क्रमशः पृथु, मृदु और भृगु शब्द बनते हैं।"),
    hindiExplanation = "प्रथ्, म्रद् और भ्रस्ज् धातुओं के बाद अनुवृत्त कु प्रत्यय होता है; क् इत्संज्ञक होकर उ शेष रहता है। तीनों में र् का संप्रसारण ऋ होता है, जिससे पृथु, मृदु और भृ-आधार प्राप्त होते हैं। भ्रस्ज् में सूत्र से केवल स् का लोप होता है और ‘न्यङ्क्वादीनां च’ ७.३.५३ से ज् को ग् होकर भृगु सिद्ध होता है। " +
        "प्रथ् के गण का सूत्र में भेद नहीं है, इसलिए समान प्रख्यानार्थ वाली भ्वादि तथा चुरादि दोनों प्रविष्टियाँ सुरक्षित हैं। स्रोत इन रूपों की प्रक्रिया देता है, पर यहाँ अतिरिक्त कोशगत अर्थ नहीं गढ़े गए हैं।",
)
