package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.25: अपदुःसुषु स्थः
object ApaDusSusuSthahSutra : UnadiSutra(
    number = "1.25",
    text = "अपदुःसुषु स्थः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1077" }),
    pratyaya = "कु",
    pratyayaSurface = "उ",
    itMarkers = setOf(ItMarker.KIT),
    upasargas = setOf(
        "अप".toVarnas(),
        listOf(Vyanjana.DA, Svara.U, Vyanjana.SA),
        listOf(Vyanjana.SA, Svara.U),
    ),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(Vyanjana.SA, Vyanjana.SSA),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant("अप".toVarnas(), "अपष्ठु".toVarnas(), Artha.Rudhi("अपष्ठु", "adverse")),
        UnadiDerivedVariant("दुस्".toVarnas(), "दुष्ठु".toVarnas(), Artha.Rudhi("दुष्ठु")),
        UnadiDerivedVariant("सु".toVarnas(), "सुष्ठु".toVarnas(), Artha.Rudhi("सुष्ठु")),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("स्था धातु से अप्, दुस् और सु उपसर्गों के अनुसार तीन पृथक् अव्ययीभूत रूप बनते हैं।"),
    hindiExplanation = "स्था धातु के पहले क्रमशः अप्, दुस् अथवा सु उपसर्ग और बाद में पूर्वसूत्र से अनुवृत्त कु प्रत्यय होता है। क् इत्संज्ञक होकर लुप्त होता है और उ शेष रहता है। ‘सुषामादिषु च’ ८.३.९८ से स् को ष् तथा नियमित ध्वनि-प्रक्रियाओं से स्थ् को ष्ठ् रूप मिलता है। " +
        "इससे अपष्ठु, दुष्ठु और सुष्ठु—तीन अलग उपसर्गयुक्त रूप सिद्ध होते हैं। स्रोत केवल अपष्ठु का अर्थ प्रतिकूल बताता है; इसलिए दुष्ठु और सुष्ठु के लिए कोई अनुल्लिखित अर्थ नहीं गढ़ा गया है।",
)
