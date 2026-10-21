package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.101: रमेर्वृद्धिश्च
object RamerVrddhisCaSutra : UnadiSutra(
    number = "1.101",
    text = "रमेर्वृद्धिश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0989" }),
    pratyaya = "अठ",
    pratyayaSurface = "अठ",
    rootSamjnaMap = mapOf("रमुँ" to Samjna.Rudhi("रामठ")),
    rootMeaningMap = mapOf("रमुँ" to Artha.Explanation("हिङ्गु")),
    rootMorphology = mapOf(
        "रमुँ" to UnadiRootMorphology(
            conditionedStem = "रम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("रामठ".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("हिङ्गु"), "1.101"),
    ),
    relatedSutraNumbers = setOf("1.100"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("रामठ अठ-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है और हिङ्गु का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से अठ प्रत्यय की अनुवृत्ति होती है। रमुँ धातु के रम् आधार में सूत्र का ‘वृद्धिश्च’ अ-वर्ण की वृद्धि आ का विधान करता है; इसे अ से आ का स्पष्ट Varna-आदेश रखा गया है। इस प्रकार रम् आधार राम् बनता है और राम् + अठ के संयोग से रामठ प्रातिपदिक सिद्ध होता है। वृत्ति ‘रामठं हिङ्गु’ कहती है, अतः इसे नपुंसकलिङ्ग में हिङ्गु का वाचक अभिलेखित किया गया है।",
)
