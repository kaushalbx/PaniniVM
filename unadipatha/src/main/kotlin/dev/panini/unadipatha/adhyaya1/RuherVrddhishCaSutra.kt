package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.47: रुहेर्वृद्धिश्च
object RuherVrddhishCaSutra : UnadiSutra(
    number = "1.47",
    text = "रुहेर्वृद्धिश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0995" }),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    rootSamjnaMap = mapOf("रुहँ" to Samjna.Rudhi("रौहिष")),
    rootMeaningMap = mapOf("रुहँ" to Artha.Explanation("मृग-विशेष अथवा तृण")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Svara.U, replacement = Svara.AU),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "रौहिष".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("मृग-विशेष"),
            attributedSutra = "1.47",
        ),
        UnadiLexicalAttestation(
            form = "रौहिष".toVarnas(),
            lingas = setOf(Linga.NAPUMSAKA),
            meaning = Artha.Explanation("तृण"),
            attributedSutra = "1.47",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("रौहिष शब्द पुल्लिङ्ग में मृग-विशेष और नपुंसकलिङ्ग में तृण का वाचक है।"),
    hindiExplanation = "बीज से उत्पत्ति अथवा प्रादुर्भाव अर्थ वाले भ्वादिगणीय रुहँ धातु के बाद पूर्वसूत्र से अनुवृत्त टिषच् प्रत्यय होता है। ट् और च् इत्संज्ञक होकर लुप्त होते हैं और इष शेष रहता है। सूत्र के ‘वृद्धिश्च’ विधान से रुह् के उकार की वृद्धि होकर औकार आता है; अतः रुह् से रौह् और इष के योग से रौहिष प्रातिपदिक सिद्ध होता है। " +
        "संसारावर्त के उद्धरण में रौहिष पुल्लिङ्ग में मृग-विशेष और नपुंसकलिङ्ग में तृण का वाचक बताया गया है, इसलिए दोनों प्रयोग अलग लिङ्गयुक्त attestations में रखे गए हैं। ‘रङ्कुशम्बररौहिषाः’ में रङ्कु और शम्बर केवल उद्धृत शब्दसमूह के अन्य सदस्य हैं; वे इस सूत्र से सिद्ध रूप नहीं हैं और model में outputs के रूप में नहीं जोड़े गए हैं।",
)
