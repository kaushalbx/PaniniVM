package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.tanadi.TanDhatu
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

// 1.81: कृषिचमितनिधनिसर्जिखर्जिभ्य ऊः
object KrsiCamiTaniDhaniSarjiKharjibhyaUhSutra : UnadiSutra(
    number = "1.81",
    text = "कृषिचमितनिधनिसर्जिखर्जिभ्य ऊः",
    roots = setOf(
        TudadiDhatus.all.single { it.id == "06.0006" },
        BhvadiDhatus.all.single { it.id == "01.0540" },
        TanDhatu(),
        JuhotyadiDhatus.all.single { it.id == "03.0024" },
        BhvadiDhatus.all.single { it.id == "01.0257" },
        BhvadiDhatus.all.single { it.id == "01.0261" },
    ),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf(
        "कृषँ" to Samjna.Rudhi("कर्षू"),
        "चमुँ" to Samjna.Rudhi("चमू"),
        "तनुँ" to Samjna.Rudhi("तनू"),
        "धनँ" to Samjna.Rudhi("धनू"),
        "षर्जँ" to Samjna.Rudhi("सर्जू"),
        "खर्जँ" to Samjna.Rudhi("खर्जू"),
    ),
    rootMeaningMap = mapOf(
        "कृषँ" to Artha.Explanation("पुल्लिङ्ग में करीष अथवा अग्नि और स्त्रीलिङ्ग में नदी"),
        "धनँ" to Artha.Explanation("शस्त्र"),
        "षर्जँ" to Artha.Explanation("वणिक्"),
        "खर्जँ" to Artha.Explanation("पामा"),
    ),
    rootMorphology = mapOf(
        "कृषँ" to UnadiRootMorphology(
            conditionedStem = "कृष्".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.A, Vyanjana.RA))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कर्षू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("करीष अथवा अग्नि"), "1.81"),
        UnadiLexicalAttestation("कर्षू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("नदी"), "1.81"),
        UnadiLexicalAttestation("चमू".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.81"),
        UnadiLexicalAttestation("तनू".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.81"),
        UnadiLexicalAttestation("धनू".toVarnas(), meaning = Artha.Explanation("शस्त्र"), attributedSutra = "1.81"),
        UnadiLexicalAttestation("सर्जू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("वणिक्"), "1.81"),
        UnadiLexicalAttestation("खर्जू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("पामा"), "1.81"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कर्षू, चमू, तनू, धनू, सर्जू और खर्जू—ये ऊ-प्रत्ययान्त प्रातिपदिक वृत्ति में निर्दिष्ट अर्थों में प्रयुक्त होते हैं।"),
    hindiExplanation = "कृषि, चमि, तनि, धनि, सर्जि और खर्जि—इन छह धातुओं के बाद ऊ प्रत्यय होता है। कृषँ के कृष् आधार में ऋ का गुण होकर अर् बनता है; इस Varna-विस्तार से कर्ष् + ऊ = कर्षू सिद्ध होता है। कर्षू पुल्लिङ्ग में करीष अथवा अग्नि और स्त्रीलिङ्ग में नदी का वाचक है, इसलिए दोनों अर्थ-लिङ्ग अलग अभिलेखित हैं। चमुँ + ऊ से चमू और तनुँ + ऊ से तनू बनते हैं; वृत्ति इनके लिए कोई अर्थ नहीं देती, अतः अर्थ अनुमान से नहीं जोड़े गए। धनँ + ऊ से धनू ‘शस्त्र’, षर्जँ के सर्ज् आधार + ऊ से सर्जू ‘वणिक्’ और खर्जँ + ऊ से खर्जू ‘पामा’ बनते हैं।",
)
