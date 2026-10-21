package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.RiDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.71: अर्तेश्च तुः
object ArtesCaTuhSutra : UnadiSutra(
    number = "1.71",
    text = "अर्तेश्च तुः",
    roots = setOf(RiDhatu()),
    pratyaya = "तु",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("ऋ" to Samjna.Rudhi("ऋतु")),
    rootMeaningMap = mapOf("ऋ" to Artha.Explanation("स्त्रीपुष्प अथवा काल")),
    rootMorphology = mapOf(
        "ऋ" to UnadiRootMorphology(conditionedStem = "ऋ".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "ऋतु".toVarnas(),
            setOf(Linga.PUMS),
            Artha.Explanation("स्त्रीपुष्प अथवा काल"),
            "1.71",
        ),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("ऋतु कित् तु-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और स्त्रीपुष्प अथवा काल का वाचक है।"),
    hindiExplanation = "गति और प्राप्ति अर्थ वाले भ्वादिगणीय ऋ धातु के बाद तु प्रत्यय होता है। वृत्ति स्पष्ट करती है कि यह तु कित् है; कित्त्व के कारण ऋ का गुण नहीं होता और मूल ऋ-वर्ण सुरक्षित रहता है। 7.2.9 के तु-ग्रहण से इसके पहले इट्-आगम भी निषिद्ध है। इस प्रकार ऋ + तु से ऋतु प्रातिपदिक सीधे सिद्ध होता है। वृत्ति ‘ऋतुः स्त्रीपुष्पकालयोः’ कहती है, इसलिए इसे पुल्लिङ्ग में स्त्रीपुष्प अर्थात् स्त्री के रजःप्रवर्तन तथा काल—इन दो अर्थों में रखा गया है; इनके बाहर कोई अन्य ऋतु-अर्थ नहीं जोड़ा गया।",
)
