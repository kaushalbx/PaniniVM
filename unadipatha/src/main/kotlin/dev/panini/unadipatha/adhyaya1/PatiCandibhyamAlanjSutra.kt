package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.114: पतिचण्डिभ्यामालञ्
object PatiCandibhyamAlanjSutra : UnadiSutra(
    number = "1.114",
    text = "पतिचण्डिभ्यामालञ्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0979" },
        BhvadiDhatus.all.single { it.id == "01.0312" },
    ),
    pratyaya = "आलञ्",
    pratyayaSurface = "आल",
    itMarkers = setOf(ItMarker.NYIT),
    rootSamjnaMap = mapOf("पतॢँ" to Samjna.Rudhi("पाताल"), "चडिँ" to Samjna.Rudhi("चण्डाल")),
    rootMorphology = mapOf(
        "पतॢँ" to UnadiRootMorphology(
            conditionedStem = "पत्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
        ),
        "चडिँ" to UnadiRootMorphology(conditionedStem = "चण्ड्".toVarnas()),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative("चण्डाल".toVarnas(), "प्रज्ञादि-अण्", "चाण्डाल".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पाताल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.114"),
        UnadiLexicalAttestation("चण्डाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.114"),
        UnadiLexicalAttestation("चाण्डाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.114"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पाताल और चण्डाल आलञ्-प्रत्ययान्त प्रातिपदिक हैं; चाण्डाल परवर्ती प्रज्ञादि-व्युत्पत्ति है।"),
    hindiExplanation = "पतॢँ और चडिँ धातुओं के बाद आलञ् प्रत्यय होता है। अन्तिम ञ् इत्संज्ञक होकर लुप्त होता है, इसलिए प्रत्यय का श्रव्य रूप आल है। पतॢँ के पत् आधार में अ का आ Varna-आदेश रखकर पात् आधार मिलता है और पात् + आल से पाताल बनता है। चडिँ का स्रोत-आधार चण्ड् है; चण्ड् + आल से चण्डाल बनता है। वृत्ति पातालम् को नपुंसकलिङ्ग और चण्डालः को पुल्लिङ्ग में देती है। ‘प्रज्ञादित्वादणि चाण्डालोऽपीत्येके’ मूल आलञ्-निर्माण से अलग, चण्डाल पर प्रज्ञादि-अण् से चाण्डाल बताता है; इसलिए चाण्डाल को secondary derivative रखा गया है, मूल धातु-प्रत्यय परिणाम नहीं। स्रोत यहाँ इन शब्दों का कोशार्थ नहीं बताता, अतः कोई अर्थ अनुमान से नहीं जोड़ा गया।",
)
