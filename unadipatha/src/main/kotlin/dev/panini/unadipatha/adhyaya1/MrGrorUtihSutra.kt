package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.94: मृग्रोरुतिः
object MrGrorUtihSutra : UnadiSutra(
    number = "1.94",
    text = "मृग्रोरुतिः",
    roots = setOf(
        TudadiDhatus.all.single { it.id == "06.0139" },
        GrDhatu(),
    ),
    pratyaya = "उति",
    pratyayaSurface = "उत्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(
        "मृङ्" to Samjna.Rudhi("मरुत्"),
        "गॄ" to Samjna.Rudhi("गरुत्"),
    ),
    rootMeaningMap = mapOf("गॄ" to Artha.Explanation("पक्ष")),
    rootMorphology = mapOf(
        "मृङ्" to UnadiRootMorphology(
            conditionedStem = "मृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        "गॄ" to UnadiRootMorphology(
            conditionedStem = "गॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("मरुत्".toVarnas(), attributedSutra = "1.94"),
        UnadiLexicalAttestation("गरुत्".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पक्ष"), "1.94"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मरुत् और गरुत् इकारानुबन्धक उति-प्रत्ययान्त प्रातिपदिक हैं; गरुत् पक्ष का वाचक है।"),
    hindiExplanation = "मृङ् और गॄ धातुओं के बाद उति प्रत्यय होता है। प्रत्यय का अन्तिम इ अनुबन्ध होकर लुप्त होता है और सुनाई देने वाला अंश उत् रहता है। मृङ् के मृ आधार में ऋ का गुण अर्—अ और र् का क्रमबद्ध Varna-विस्तार—होने से मर् + उत् = मरुत् बनता है। गॄ के ॠ का गुण भी अर् होकर गर् + उत् = गरुत् सिद्ध होता है, जिसे वृत्ति पक्ष अर्थात् पंख कहती है। वृत्ति मरुत् का कोई अर्थ या लिङ्ग यहाँ नहीं बताती, इसलिए प्रसिद्ध कोशार्थ इस नियम में अनुमान से नहीं जोड़े गए।",
)
