package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiUpapadaFormation
import dev.panini.unadipatha.UnadiVarnaLopa

private val sutra156Ha = JuhotyadiDhatus.all.single { it.id == "03.0009" }

// 1.156: नञि जहातेः
object NanjiJahatehSutra : UnadiSutra(
    number = "1.156",
    text = "नञि जहातेः",
    roots = setOf(sutra156Ha),
    pratyaya = "कनिन्",
    pratyayaSurface = "अन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.I),
    upapadas = setOf("नञ्".toVarnas()),
    rootSamjnaMap = mapOf(sutra156Ha.upadesha to Samjna.Rudhi("अहन्")),
    rootMorphology = mapOf(
        sutra156Ha.upadesha to UnadiRootMorphology(
            conditionedStem = "हा".toVarnas(),
            requiredUpapadas = setOf("नञ्".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
    ),
    upapadaFormations = listOf(
        UnadiUpapadaFormation(
            source = "नञ्".toVarnas(),
            output = "अ".toVarnas(),
            initialVarnaLopas = listOf(Vyanjana.NA),
            finalVarnaLopas = listOf(Vyanjana.NYA),
        ),
    ),
    lexicalAttestations = listOf(
        dev.panini.unadipatha.UnadiLexicalAttestation("अहः".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.156"),
    ),
    relatedSutraNumbers = setOf("1.154"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "नञ् उपपद के साथ ओँहाक् (हा) धातु के बाद पिछले सूत्र से कनिन् प्रत्यय आता है। कनिन् में क् और इ इत्संज्ञक होकर लुप्त होते हैं तथा अन् शेष रहता है। नञ् के आरम्भिक न् और अन्तिम ञ् का लोप होने पर अ उपपद बचता है। हा धातु के आकार का लोप होने पर ह् आधार मिलता है। इस प्रकार अ + ह् + अन् से अहन् प्रातिपदिक सिद्ध होता है और उसका वृत्ति में उद्धृत रूप अहः है। प्रत्येक लोप को सम्बन्धित Varna के रूप में रखा गया है; वृत्ति इस स्थान पर अहः का पृथक् कोशार्थ नहीं बताती, इसलिए कोई अर्थ अनुमान से नहीं जोड़ा गया। अहन् का प्रचलित रूपविधान नपुंसकलिङ्ग है, अतः उद्धृत अहः को उसी लिङ्ग में अभिलिखित किया गया है।",
)
