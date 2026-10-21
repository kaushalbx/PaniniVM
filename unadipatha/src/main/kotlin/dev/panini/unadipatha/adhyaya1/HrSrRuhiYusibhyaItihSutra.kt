package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.bhvadi.HrDhatu
import dev.panini.dhatupatha.bhvadi.SrDhatu
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.97: हृसृरुहियुषिभ्य इतिः
object HrSrRuhiYusibhyaItihSutra : UnadiSutra(
    number = "1.97",
    text = "हृसृरुहियुषिभ्य इतिः",
    roots = setOf(
        HrDhatu(),
        SrDhatu(),
        JuhotyadiDhatus.all.single { it.id == "03.0018" },
        BhvadiDhatus.all.single { it.id == "01.0995" },
    ),
    rootReferences = setOf("युष्"),
    pratyaya = "इति",
    pratyayaSurface = "इत्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(
        "हृञ्" to Samjna.Rudhi("हरित्"),
        "सृ" to Samjna.Rudhi("सरित्"),
        "रुहँ" to Samjna.Rudhi("रोहित्"),
        "युष्" to Samjna.Rudhi("योषित्"),
    ),
    rootMeaningMap = mapOf(
        "हृञ्" to Artha.Explanation("ककुभ्, वर्ण तथा तृणवाचिविशेष"),
        "सृ" to Artha.Explanation("नदी"),
        "रुहँ" to Artha.Explanation("मृगविशेष की स्त्री; ऋश्य की मृगी"),
        "युष्" to Artha.Explanation("पुरुष की स्त्री अर्थात् नारी"),
    ),
    rootMorphology = mapOf(
        "हृञ्" to UnadiRootMorphology(
            conditionedStem = "हृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        "सृ" to UnadiRootMorphology(
            conditionedStem = "सृ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
        "रुहँ" to UnadiRootMorphology(
            conditionedStem = "रुह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.O)),
        ),
        "युष्" to UnadiRootMorphology(
            conditionedStem = "युष्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.O)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("हरित्".toVarnas(), meaning = Artha.Explanation("ककुभ्, वर्ण तथा तृणवाचिविशेष"), attributedSutra = "1.97"),
        UnadiLexicalAttestation("सरित्".toVarnas(), setOf(Linga.STRI), Artha.Explanation("नदी"), "1.97"),
        UnadiLexicalAttestation("रोहित्".toVarnas(), setOf(Linga.STRI), Artha.Explanation("मृगविशेष की स्त्री; ऋश्य की मृगी"), "1.97"),
        UnadiLexicalAttestation("योषित्".toVarnas(), setOf(Linga.STRI), Artha.Explanation("पुरुष की स्त्री अर्थात् नारी"), "1.97"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("हरित्, सरित्, रोहित् और योषित् इकारानुबन्धक इति-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "हृञ्, सृ, रुहँ और सौत्र युष् धातु के बाद इति प्रत्यय होता है। प्रत्यय का अन्तिम इ अनुबन्ध होकर लुप्त होता है और इत् शेष रहता है। हृ के ऋ का गुण अर् होकर हर् + इत् से हरित् बनता है; वृत्ति इसे ककुभ्, वर्ण तथा ‘तृणवाचिविशेष’ में रखती है, इसलिए इस पाठांश को बिना मनमाना विस्तार दिए सुरक्षित किया गया है। सृ के ऋ का गुण अर् होकर सरित् ‘नदी’ बनता है। रुह् के उ का गुण ओ होकर रोह् + इत् से रोहित् बनता है, जो मृगविशेष की स्त्री और भाष्य के अनुसार ऋश्य की मृगी है। युष् धातुपाठ में नहीं है; वृत्ति स्वयं उसे सौत्र कहती है। उसके उ का गुण ओ होकर योष् + इत् से स्त्रीलिङ्ग योषित् ‘नारी’ सिद्ध होता है। सृ की समान उपदेश वाली भ्वादि और जुहोत्यादि दोनों प्रविष्टियाँ सूत्र में गणनिर्देश न होने से सुरक्षित हैं।",
)
