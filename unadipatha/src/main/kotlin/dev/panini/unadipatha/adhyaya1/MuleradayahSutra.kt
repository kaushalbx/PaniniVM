package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.61: मूलेरादयः
object MuleradayahSutra : UnadiSutra(
    number = "1.61",
    text = "मूलेरादयः",
    roots = setOf(
        CuradiDhatus.all.single { it.id == "10.0092" },
        DivadiDhatus.all.single { it.id == "04.0014" },
        BhvadiDhatus.all.single { it.id == "01.1043" },
        DivadiDhatus.all.single { it.id == "04.0095" },
    ),
    pratyaya = "एरक्",
    pratyayaSurface = "एर",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "मूलँ" to Samjna.Rudhi("मूलेर"),
        "गुधँ" to Samjna.Rudhi("गुधेर"),
        "गुहूँ" to Samjna.Rudhi("गुहेर"),
        "मुहँ" to Samjna.Rudhi("मुहेर"),
    ),
    rootMeaningMap = mapOf(
        "मूलँ" to Artha.Explanation("जटा"),
        "गुधँ" to Artha.Explanation("गोप्ता"),
        "गुहूँ" to Artha.Explanation("लोहघातक"),
        "मुहँ" to Artha.Explanation("मूर्ख"),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("मूलेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जटा"), "1.61"),
        UnadiLexicalAttestation("गुधेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गोप्ता"), "1.61"),
        UnadiLexicalAttestation("गुहेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("लोहघातक"), "1.61"),
        UnadiLexicalAttestation("मुहेर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मूर्ख"), "1.61"),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मूलेर, गुधेर, गुहेर और मुहेर निपातित एरक्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक हैं।"),
    hindiExplanation = "वृत्ति ‘एरगन्ता निपात्यन्ते’ कहकर इन एर-अन्त शब्दों को निपातित रूप में पढ़ती है। अतः इन्हें केवल किसी सामान्य ध्वनि-नियम से उत्पन्न मानने के बजाय स्रोत में दिए चार रूपों के रूप में सुरक्षित किया गया है। पिछले सूत्र से एरक् प्रत्यय की अनुवृत्ति है; उसका अन्तिम क् इत्संज्ञक होकर लुप्त होता है और एर शेष रहता है। रोहणार्थक चुरादिगणीय मूलँ से मूलेर, परिवेष्टनार्थक दिवादिगणीय गुधँ से गुधेर, संवरणार्थक भ्वादिगणीय गुहूँ से गुहेर और वैचित्त्यार्थक दिवादिगणीय मुहँ से मुहेर रूप जोड़े गए हैं। वृत्ति के अनुसार मूलेर जटा, गुधेर गोप्ता अर्थात् रक्षक, गुहेर लोहघातक अर्थात् लोहे पर प्रहार करके काम करने वाला कारीगर, और मुहेर मूर्ख का वाचक है। चारों रूप ‘मूलेरः’, ‘गुधेरः’, ‘गुहेरः’, ‘मुहेरः’ के रूप में दिए गए हैं, इसलिए पुल्लिङ्ग रखा गया है; स्रोत से बाहर के अर्थ अथवा अतिरिक्त रूप नहीं गढ़े गए।",
)
