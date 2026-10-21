package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.91: नृतिशृध्योः कूः
object NrtiSrdhyohKuhSutra : UnadiSutra(
    number = "1.91",
    text = "नृतिशृध्योः कूः",
    roots = setOf(
        DivadiDhatus.all.single { it.id == "04.0010" },
        BhvadiDhatus.all.single { it.id == "01.0864" },
    ),
    pratyaya = "कू",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "नृतीँ" to Samjna.Rudhi("नृतू"),
        "शृधुँ" to Samjna.Rudhi("शृधू"),
    ),
    rootMeaningMap = mapOf(
        "नृतीँ" to Artha.Explanation("नर्तक"),
        "शृधुँ" to Artha.Explanation("अपान"),
    ),
    rootMorphology = mapOf(
        "नृतीँ" to UnadiRootMorphology(conditionedStem = "नृत्".toVarnas()),
        "शृधुँ" to UnadiRootMorphology(conditionedStem = "शृध्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("नृतू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("नर्तक"), "1.91"),
        UnadiLexicalAttestation("शृधू".toVarnas(), meaning = Artha.Explanation("अपान"), attributedSutra = "1.91"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("नृतू और शृधू कित् कू-प्रत्ययान्त प्रातिपदिक हैं तथा क्रमशः नर्तक और अपान के वाचक हैं।"),
    hindiExplanation = "नृतीँ और शृधुँ धातुओं के बाद कू प्रत्यय होता है। प्रत्यय का आरम्भिक क् इत्संज्ञक होकर लुप्त होता है और ऊ सुनाई देता है। नृतीँ के नृत् आधार में कोई अतिरिक्त वर्णादेश बताए बिना नृत् + ऊ से नृतू बनता है, जिसे वृत्ति नर्तक कहती है और इसलिए पुल्लिङ्ग अभिलेखित है। उन्दन तथा शब्दकुत्सा-अर्थ वाली शृधुँ धातु के शृध् आधार से इसी प्रकार शृध् + ऊ = शृधू बनता है, जिसका अर्थ वृत्ति में अपान दिया है। शृधू का लिङ्ग स्रोत-पाठ में स्पष्ट नहीं है, इसलिए उसके लिए लिङ्ग अनुमान से नहीं जोड़ा गया।",
)
