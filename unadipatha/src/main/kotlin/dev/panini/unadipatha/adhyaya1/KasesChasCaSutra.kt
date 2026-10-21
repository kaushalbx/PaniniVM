package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.84: कषेश्छश्च
object KasesChasCaSutra : UnadiSutra(
    number = "1.84",
    text = "कषेश्छश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0781" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf("कषँ" to Samjna.Rudhi("कच्छू")),
    rootMeaningMap = mapOf("कषँ" to Artha.Explanation("पामा अर्थात् खुजली का रोग")),
    rootMorphology = mapOf(
        "कषँ" to UnadiRootMorphology(
            conditionedStem = "कष्".toVarnas(),
            varnaExpansions = listOf(
                UnadiVarnaExpansion(Vyanjana.SSA, listOf(Vyanjana.CA, Vyanjana.CHA)),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कच्छू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("पामा अर्थात् खुजली का रोग"), "1.84"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कच्छू ऊ-प्रत्ययान्त स्त्रीलिङ्ग प्रातिपदिक है और पामा अर्थात् खुजली के रोग का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से ऊ प्रत्यय की अनुवृत्ति होती है। कषँ धातु के कष् आधार में सूत्र का ‘छश्च’ अन्तिम ष् के स्थान पर च्छ् का आदेश करता है। च्छ् संयुक्त ध्वनि को च् और छ्—दो क्रमबद्ध Varna—के विस्तार के रूप में सुरक्षित किया गया है; फलतः कष् आधार कच्छ् बनता है। इसके बाद ऊ प्रत्यय जुड़ने पर कच्छ् + ऊ = कच्छू प्रातिपदिक सिद्ध होता है। वृत्ति ‘कच्छूः पामा’ के अनुसार यह स्त्रीलिङ्ग में पामा अर्थात् खुजली के रोग का वाचक है।",
)
