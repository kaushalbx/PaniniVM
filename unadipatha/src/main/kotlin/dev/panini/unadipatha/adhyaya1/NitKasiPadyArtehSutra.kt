package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.85: णित्कसिपद्यर्तेः
object NitKasiPadyArtehSutra : UnadiSutra(
    number = "1.85",
    text = "णित्कसिपद्यर्तेः",
    roots = setOf(
        AdadiDhatus.all.single { it.id == "02.0014" },
        DivadiDhatus.all.single { it.id == "04.0065" },
        BhvadiDhatus.all.single { it.id == "01.1086" },
        JuhotyadiDhatus.all.single { it.id == "03.0017" },
    ),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf(
        "कसिँ" to Samjna.Rudhi("कासू"),
        "पदँ" to Samjna.Rudhi("पादू"),
        "ऋ" to Samjna.Rudhi("आरू"),
    ),
    rootMeaningMap = mapOf(
        "कसिँ" to Artha.Explanation("शक्ति"),
        "पदँ" to Artha.Explanation("चरणधारिणी"),
        "ऋ" to Artha.Explanation("पिङ्गल"),
    ),
    rootMorphology = mapOf(
        "कसिँ" to UnadiRootMorphology(
            conditionedStem = "कंस्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
            varnaLopas = listOf(UnadiVarnaLopa(Ayogavaha.ANUSVARA)),
        ),
        "पदँ" to UnadiRootMorphology(
            conditionedStem = "पद्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
        ),
        "ऋ" to UnadiRootMorphology(
            conditionedStem = "ऋ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कासू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("शक्ति"), "1.85"),
        UnadiLexicalAttestation("पादू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("चरणधारिणी"), "1.85"),
        UnadiLexicalAttestation("आरू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("पिङ्गल"), "1.85"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कासू, पादू और आरू—ये णित् ऊ-प्रत्ययान्त प्रातिपदिक वृत्ति में निर्दिष्ट अर्थों में प्रयुक्त होते हैं।"),
    hindiExplanation = "कसि, पदि और ऋ धातु के बाद ऊ प्रत्यय होता है और यह सूत्र उसे णित् करता है। णित्त्व के कारण धातु-स्वर की वृद्धि होती है। कसिँ के कंस् आधार में अ का आ और अनुस्वार का लोप होकर कास् + ऊ से कासू ‘शक्ति’ बनता है। पदँ में अ का आ होकर पाद् + ऊ से पादू ‘चरणधारिणी’ बनता है। ऋ की वृद्धि आर् को क्रमबद्ध आ और र् Varna-विस्तार के रूप में रखा गया है; आर् + ऊ से आरू ‘पिङ्गल’ सिद्ध होता है। धातुपाठ में ऋ की भ्वादि और जुहोत्यादि—दो समान उपदेश वाली प्रविष्टियाँ हैं और सूत्र गण नहीं बताता, इसलिए दोनों सुरक्षित हैं।",
)
