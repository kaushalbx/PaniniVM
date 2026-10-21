package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.115: तमिविशिबिडिमृणिकुलिकपिपलिपञ्चिभ्यः कालन्
object TamiVisiBidiMrniKuliKapiPaliPancibhyahKalanSutra : UnadiSutra(
    number = "1.115",
    text = "तमिविशिबिडिमृणिकुलिकपिपलिपञ्चिभ्यः कालन्",
    roots = setOf(
        DivadiDhatus.all.single { it.id == "04.0099" },
        TudadiDhatus.all.single { it.id == "06.0160" },
        BhvadiDhatus.all.single { it.id == "01.0949" },
        TudadiDhatus.all.single { it.id == "06.0057" },
        BhvadiDhatus.all.single { it.id == "01.0976" },
        BhvadiDhatus.all.single { it.id == "01.0435" },
        BhvadiDhatus.all.single { it.id == "01.0973" },
        BhvadiDhatus.all.single { it.id == "01.0198" },
    ),
    pratyaya = "कालन्",
    pratyayaSurface = "आल",
    itMarkers = setOf(ItMarker.KIT, ItMarker.N),
    rootSamjnaMap = mapOf(
        "तमुँ" to Samjna.Rudhi("तमाल"), "विशँ" to Samjna.Rudhi("विशाल"),
        "बिडँ" to Samjna.Rudhi("बिडाल"), "मृणँ" to Samjna.Rudhi("मृणाल"),
        "कुलँ" to Samjna.Rudhi("कुलाल"), "कपिँ" to Samjna.Rudhi("कपाल"),
        "पलँ" to Samjna.Rudhi("पलाल"), "पचिँ" to Samjna.Rudhi("पञ्चाल"),
    ),
    rootMorphology = mapOf(
        "तमुँ" to UnadiRootMorphology(conditionedStem = "तम्".toVarnas()),
        "विशँ" to UnadiRootMorphology(conditionedStem = "विश्".toVarnas()),
        "बिडँ" to UnadiRootMorphology(conditionedStem = "बिड्".toVarnas()),
        "मृणँ" to UnadiRootMorphology(conditionedStem = "मृण्".toVarnas()),
        "कुलँ" to UnadiRootMorphology(conditionedStem = "कुल्".toVarnas()),
        "कपिँ" to UnadiRootMorphology(conditionedStem = "कप्".toVarnas()),
        "पलँ" to UnadiRootMorphology(conditionedStem = "पल्".toVarnas()),
        "पचिँ" to UnadiRootMorphology(conditionedStem = "पञ्च्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("तमाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.115"),
        UnadiLexicalAttestation("विशाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.115"),
        UnadiLexicalAttestation("बिडाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.115"),
        UnadiLexicalAttestation("मृणाल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.115"),
        UnadiLexicalAttestation("कुलाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.115"),
        UnadiLexicalAttestation("कपाल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.115"),
        UnadiLexicalAttestation("पलाल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.115"),
        UnadiLexicalAttestation("पञ्चाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.115"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तमाल आदि आठ कालन्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "तमि, विशि, बिडि, मृणि, कुलि, कपि, पलि और पञ्चि—इन आठ स्रोत-निर्दिष्ट धातुओं के बाद कालन् प्रत्यय होता है। कालन् का आरम्भिक क् और अन्तिम न् इत्संज्ञक होकर लुप्त होते हैं; इसलिए प्रत्यय का श्रव्य Varna-रूप आल है। तम् + आल से तमाल, विश् + आल से विशाल, बिड् + आल से बिडाल, मृण् + आल से मृणाल, कुल् + आल से कुलाल, कप् + आल से कपाल, पल् + आल से पलाल और पञ्च् + आल से पञ्चाल बनते हैं। कपिँ की धातुपाठीय सामान्य surface कम्प् है, किन्तु इस सूत्र का पाठ स्वयं ‘कपि’ और वृत्ति कपालम् देती है; इसलिए यहाँ सूत्रानुकूल कप् conditioned stem रखा गया है, पर स्रोत में न कहा गया कोई काल्पनिक म्-लोप operation नहीं जोड़ा गया। वृत्ति तमालः, विशालः, बिडालः, कुलालः को पुल्लिङ्ग; मृणालम्, कपालम्, पलालम् को नपुंसकलिङ्ग; और पञ्चालाः को पुल्लिङ्ग बहुवचन में उद्धृत करती है। इस प्रविष्टि में इनके कोशार्थ नहीं दिए गए, इसलिए कोई अर्थ अनुमान से नहीं जोड़ा गया।",
)
