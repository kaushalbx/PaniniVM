package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.79: जीवेरातुः
object JiverAtuhSutra : UnadiSutra(
    number = "1.79",
    text = "जीवेरातुः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0643" }),
    pratyaya = "आतु",
    pratyayaSurface = "आतु",
    rootSamjnaMap = mapOf("जीवँ" to Samjna.Rudhi("जीवातु")),
    rootMeaningMap = mapOf("जीवँ" to Artha.Explanation("भक्त, जीवित अथवा जीवनौषध")),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "जीवातु".toVarnas(),
            setOf(Linga.PUMS, Linga.NAPUMSAKA),
            Artha.Explanation("भक्त, जीवित अथवा जीवनौषध"),
            "1.79",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("जीवातु आतु-प्रत्ययान्त, स्त्रीलिङ्ग से भिन्न प्रातिपदिक है और भक्त, जीवित अथवा जीवनौषध का वाचक है।"),
    hindiExplanation = "प्राणधारणार्थक भ्वादिगणीय जीवँ धातु के जीव् आधार के बाद आतु प्रत्यय होता है। इस प्रत्यय में सूत्र अथवा वृत्ति कोई इत्संज्ञक वर्ण, आगम, लोप या विशेष आदेश नहीं बताती; इसलिए पूरा आतु श्रव्य रखा गया है। जीव् + आतु में ई और व् अपने स्थान पर रहते हैं और जीवातु प्रातिपदिक सीधे सिद्ध होता है। वृत्ति ‘जीवातुरस्त्रियां’ कहकर स्त्रीलिङ्ग का निषेध करती है, इसलिए पुल्लिङ्ग और नपुंसकलिङ्ग सुरक्षित हैं। इसके अर्थ भक्त, जीवित और जीवनौषध दिए गए हैं; इनके बाहर कोई अतिरिक्त अर्थ नहीं जोड़ा गया।",
)
