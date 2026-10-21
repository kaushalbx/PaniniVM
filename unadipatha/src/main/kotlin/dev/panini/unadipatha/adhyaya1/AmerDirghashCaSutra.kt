package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.46: अमेर्दीर्घश्च
object AmerDirghashCaSutra : UnadiSutra(
    number = "1.46",
    text = "अमेर्दीर्घश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0536" },
        CuradiDhatus.all.single { it.id == "10.0245" },
    ),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    rootSamjnaMap = mapOf("अमँ" to Samjna.Rudhi("आमिष")),
    rootMeaningMap = mapOf("अमँ" to Artha.Explanation("मांस अथवा भोग्य वस्तु")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Svara.A, replacement = Svara.AA),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "आमिष".toVarnas(),
            lingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
            meaning = Artha.Explanation("मांस अथवा भोग्य वस्तु"),
            attributedSutra = "1.46",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("आमिष शब्द मांस अथवा भोग्य वस्तु का वाचक है और स्त्रीलिङ्ग में प्रयुक्त नहीं होता।"),
    hindiExplanation = "अमँ धातु के बाद पूर्वसूत्र से अनुवृत्त टिषच् प्रत्यय होता है। ट् और च् इत्संज्ञक होकर लुप्त होते हैं तथा इष शेष रहता है। सूत्र के ‘दीर्घश्च’ विधान से धातु के अकार के स्थान पर आकार होता है; इस प्रकार अम् से आम् और इष के योग से आमिष प्रातिपदिक सिद्ध होता है। " +
        "धातुपाठ में अमँ की भ्वादि और चुरादि दो समनाम प्रविष्टियाँ हैं, जबकि सूत्र गण अथवा धात्वर्थ का भेद नहीं करता; इसलिए दोनों identities सुरक्षित रखी गई हैं। वृत्ति ‘अस्त्रियां’ कहकर स्त्रीलिङ्ग का निषेध करती है, अतः पुल्लिङ्ग और नपुंसकलिङ्ग दोनों दर्ज हैं। आमिष के प्रमाणित अर्थ मांस तथा भोग्य वस्तु हैं; इनके अतिरिक्त कोई अर्थ नहीं जोड़ा गया है।",
)
