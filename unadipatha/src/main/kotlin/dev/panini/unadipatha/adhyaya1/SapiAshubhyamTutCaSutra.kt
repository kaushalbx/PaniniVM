package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.svadi.AshDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra155Ash = AshDhatu()

// 1.155: सप्यशूभ्यां तुट् च
object SapiAshubhyamTutCaSutra : UnadiSutra(
    number = "1.155",
    text = "सप्यशूभ्यां तुट् च",
    roots = setOf(sutra155Ash),
    rootReferences = setOf("सप्"),
    pratyaya = "कनिन्",
    pratyayaSurface = "अन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.I),
    rootSamjnaMap = mapOf(
        "सप्" to Samjna.Rudhi("सप्तन्"),
        sutra155Ash.upadesha to Samjna.Rudhi("अष्टन्"),
    ),
    rootMeaningMap = mapOf(
        "सप्" to Artha.Explanation("सात"),
        sutra155Ash.upadesha to Artha.Explanation("आठ"),
    ),
    rootMorphology = mapOf(
        "सप्" to UnadiRootMorphology(
            conditionedStem = "सप्".toVarnas(),
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.TA), UnadiAgamaPlacement.BEFORE_SUFFIX, "तुट्")),
        ),
        sutra155Ash.upadesha to UnadiRootMorphology(
            conditionedStem = "अश्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.SHA, Vyanjana.SSA)),
            varnaAgamas = listOf(UnadiVarnaAgama(listOf(Vyanjana.TTA), UnadiAgamaPlacement.BEFORE_SUFFIX, "तुट्")),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सप्त".toVarnas(), meaning = Artha.Explanation("सात"), attributedSutra = "1.155"),
        UnadiLexicalAttestation("अष्ट".toVarnas(), meaning = Artha.Explanation("आठ"), attributedSutra = "1.155"),
    ),
    relatedSutraNumbers = setOf("1.154"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.SANKHYA),
    hindiExplanation = "सप् और अशूँ धातु-सन्दर्भों के बाद पिछले सूत्र से कनिन् प्रत्यय आता है तथा तुट् आगम भी होता है। कनिन् के क् और इ इत्संज्ञक होकर लुप्त होते हैं, अतः अन् शेष रहता है। सप् के बाद तुट् का प्रभावी त् प्रत्यय से पहले जुड़कर सप् + त् + अन् से सप्तन् संख्या-प्रातिपदिक बनता है, जिसका उद्धृत रूप सप्त है। अशूँ का धातु-आधार अश् है; श् का ष् आदेश और उसके बाद तुट् के त् का ष्टुत्व होकर प्रभावी ट् आने से अष् + ट् + अन् = अष्टन् बनता है, जिसका उद्धृत रूप अष्ट है। आगम की मूल उपदेश-संज्ञा दोनों शाखाओं में तुट् ही सुरक्षित है; अष्टन् शाखा में ट् उसका नियमित ध्वन्यात्मक परिणाम है। वृत्ति केवल सप्त और अष्ट देती है, अतः इन्हें सात और आठ की संख्यासंज्ञा दी गई है और कोई लिङ्ग अनुमानित नहीं किया गया।",
)
