package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.74: आप्नोतेर्ह्रस्वश्च
object ApnoterHrasvasCaSutra : UnadiSutra(
    number = "1.74",
    text = "आप्नोतेर्ह्रस्वश्च",
    roots = setOf(SvadiDhatus.all.single { it.id == "05.0016" }),
    pratyaya = "तु",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("आपॢँ" to Samjna.Rudhi("अप्तु")),
    rootMeaningMap = mapOf("आपॢँ" to Artha.Explanation("शरीर")),
    rootMorphology = mapOf(
        "आपॢँ" to UnadiRootMorphology(
            conditionedStem = "आप्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.AA, Svara.A)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अप्तु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("शरीर"), "1.74"),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("अप्तु कित् तु-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और शरीर का वाचक है।"),
    hindiExplanation = "व्याप्त्यर्थक स्वादिगणीय आपॢँ धातु के आप् आधार के बाद पिछले सूत्र से कित् तु प्रत्यय आता है और 7.2.9 के अनुसार इट्-आगम नहीं होता। सूत्र का ‘ह्रस्वश्च’ धातु के दीर्घ आ के स्थान पर ह्रस्व अ का स्पष्ट वर्णादेश करता है; अतः आप् से अप् आधार बनता है। अप् + तु के संयोग से अप्तु प्रातिपदिक सिद्ध होता है। वृत्ति ‘अप्तुः शरीरम्’ कहती है, इसलिए इसे पुल्लिङ्ग तथा शरीर-अर्थ में रखा गया है; बाद की व्याख्याओं में मिलने वाले अतिरिक्त अभिलषित-अर्थ को मूल वृत्ति में न होने के कारण यहाँ नहीं जोड़ा गया।",
)
