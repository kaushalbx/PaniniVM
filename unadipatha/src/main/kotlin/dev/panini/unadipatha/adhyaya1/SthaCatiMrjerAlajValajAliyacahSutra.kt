package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiRootPratyaya
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.113: स्थाचतिमृजेरालज्वालजालीयचः
object SthaCatiMrjerAlajValajAliyacahSutra : UnadiSutra(
    number = "1.113",
    text = "स्थाचतिमृजेरालज्वालजालीयचः",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1077" },
        BhvadiDhatus.all.single { it.id == "01.1003" },
        AdadiDhatus.all.single { it.id == "02.0061" },
    ),
    pratyaya = "आलच्, वालच्, आलीयच्",
    pratyayaSurface = "",
    rootPratyayaMap = mapOf(
        "ष्ठा" to UnadiRootPratyaya("आलच्", "आल", setOf(ItMarker.C)),
        "चतेँ" to UnadiRootPratyaya("वालच्", "वाल", setOf(ItMarker.C)),
        "मृजूँ" to UnadiRootPratyaya("आलीयच्", "आलीय", setOf(ItMarker.C)),
    ),
    rootSamjnaMap = mapOf(
        "ष्ठा" to Samjna.Rudhi("स्थाल"),
        "चतेँ" to Samjna.Rudhi("चत्वाल"),
        "मृजूँ" to Samjna.Rudhi("मार्जालीय"),
    ),
    rootMeaningMap = mapOf("मृजूँ" to Artha.Explanation("बिडाल")),
    rootMorphology = mapOf(
        "ष्ठा" to UnadiRootMorphology(conditionedStem = "स्था".toVarnas()),
        "चतेँ" to UnadiRootMorphology(conditionedStem = "चत्".toVarnas()),
        "मृजूँ" to UnadiRootMorphology(
            conditionedStem = "मृज्".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.R, listOf(Svara.AA, Vyanjana.RA))),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(sourceRoot = "ष्ठा", output = "स्थाल".toVarnas()),
        UnadiDerivedVariant(sourceRoot = "ष्ठा", output = "स्थाली".toVarnas()),
        UnadiDerivedVariant(sourceRoot = "चतेँ", output = "चत्वाल".toVarnas()),
        UnadiDerivedVariant(sourceRoot = "मृजूँ", output = "मार्जालीय".toVarnas(), meaning = Artha.Explanation("बिडाल")),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("स्थाल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.113"),
        UnadiLexicalAttestation("स्थाली".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.113"),
        UnadiLexicalAttestation("चत्वाल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.113"),
        UnadiLexicalAttestation("मार्जालीय".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("बिडाल"), "1.113"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("स्था, चति और मृज् से क्रमशः आलच्, वालच् और आलीयच्-प्रत्ययान्त प्रातिपदिक बनते हैं।"),
    hindiExplanation = "यह सूत्र तीन धातुओं के लिए तीन अलग प्रत्यय देता है; इसलिए उन्हें एक साझा प्रत्यय मानकर नहीं छोड़ा गया है। तिष्ठति रूप वाली ष्ठा/स्था धातु से आलच् होता है। अन्तिम च् के इत्संज्ञा-लोप से आल शेष रहता है और स्था + आल के स्वर-संयोग से स्थाल बनता है; वृत्ति स्थालम् तथा स्त्रीलिङ्ग स्थाली दोनों देती है। चतेँ धातु के चत् आधार से वालच् होता है, जिसका श्रव्य रूप वाल है; चत् + वाल से चत्वाल बनता है। मृजूँ से आलीयच् होता है, जिसका श्रव्य रूप आलीय है। मृज् के ऋ का वृद्धि-रूप आर् स्पष्ट Varna-विस्तार से मार्ज् आधार देता है और मार्ज् + आलीय से मार्जालीय बनता है। वृत्ति ‘मार्जालीयो बिडालः’ कहती है, इसलिए इसका अर्थ बिडाल और लिङ्ग पुल्लिङ्ग रखा गया है। स्थाल, स्थाली और चत्वाल के लिए स्रोत कोई कोशार्थ नहीं देता, अतः अर्थ अनुमान से नहीं जोड़े गए।",
)
