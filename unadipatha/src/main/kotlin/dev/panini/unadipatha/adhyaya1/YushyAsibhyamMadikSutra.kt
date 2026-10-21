package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.AsDhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

private val sutra136As = AsDhatu()

// 1.136: युष्यसिभ्यां मदिक्
object YushyAsibhyamMadikSutra : UnadiSutra(
    number = "1.136",
    text = "युष्यसिभ्यां मदिक्",
    roots = setOf(sutra136As),
    rootReferences = setOf("युष्"),
    pratyaya = "मदिक्",
    pratyayaSurface = "मद्",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "युष्" to Samjna.Rudhi("युष्मद्"),
        sutra136As.upadesha to Samjna.Rudhi("अस्मद्"),
    ),
    rootMeaningMap = mapOf(
        "युष्" to Artha.Explanation("मध्यमपुरुष का सर्वनाम; एकवचन में त्वम्"),
        sutra136As.upadesha to Artha.Explanation("उत्तमपुरुष का सर्वनाम; एकवचन में अहम्"),
    ),
    rootMorphology = mapOf(
        "युष्" to UnadiRootMorphology(conditionedStem = "युष्".toVarnas()),
        sutra136As.upadesha to UnadiRootMorphology(conditionedStem = "अस्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "युष्मद्".toVarnas(),
            meaning = Artha.Explanation("मध्यमपुरुष का सर्वनाम; एकवचन में त्वम्"),
            attributedSutra = "1.136",
        ),
        UnadiLexicalAttestation(
            "अस्मद्".toVarnas(),
            meaning = Artha.Explanation("उत्तमपुरुष का सर्वनाम; एकवचन में अहम्"),
            attributedSutra = "1.136",
        ),
    ),
    assignedAccent = Accent.UDATTA,
    accentVowelIndex = 1,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.SARVANAMA),
    meaning = Artha.Explanation("युष्मद् और अस्मद् क्रमशः मध्यमपुरुष तथा उत्तमपुरुष के सर्वनाम हैं।"),
    hindiExplanation = "युष् और अस् धातुओं के बाद मदिक् प्रत्यय होता है। वृत्ति युष् को स्पष्टतः सौत्र धातु कहती है, इसलिए उसके लिए कोई काल्पनिक धातुपाठ-ID नहीं चुनी गई; अस् के लिए अदादिगणीय असँ भुवि धातु ली गई है। मदिक् का क् इत्संज्ञक है और उसके उच्चारण के लिए प्रयुक्त इ श्रव्य प्रत्यय का अंश नहीं है, इसलिए प्रत्यय का surface मद् है। कित्त्व से धातु-स्वर का गुण नहीं होता। युष् + मद् से युष्मद् और अस् + मद् से अस्मद् सिद्ध होते हैं। प्रत्ययस्वर के कारण मद् का अ अन्तोदात्त है, इसलिए दोनों शब्दों में दूसरा स्वर उदात्त दर्ज किया गया है। वृत्ति के ‘त्वम्’ और ‘अहम्’ क्रमशः इन सर्वनामों के एकवचन में होने वाले व्याकरणिक आदेश हैं, अलग उणादि-निष्पन्न प्रातिपदिक नहीं; अतः उन्हें derived variants नहीं बनाया गया। दोनों को सर्वनामसंज्ञा दी गई है और स्रोत में न दिए लिङ्ग अनुमान से नहीं जोड़े गए।",
)
