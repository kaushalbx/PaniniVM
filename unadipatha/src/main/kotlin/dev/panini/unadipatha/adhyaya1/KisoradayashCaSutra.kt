package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiUpapadaFormation
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.65: किशोरादयश्च
object KisoradayashCaSutra : UnadiSutra(
    number = "1.65",
    text = "किशोरादयश्च",
    roots = setOf(ShrDhatu()),
    pratyaya = "ओरन्",
    pratyayaSurface = "ओर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("शॄ" to Samjna.Rudhi("किशोर")),
    rootMeaningMap = mapOf("शॄ" to Artha.Explanation("अश्वशाव अर्थात् घोड़े का बच्चा")),
    upapadas = setOf("किम्".toVarnas()),
    upapadaFormations = listOf(
        UnadiUpapadaFormation(
            source = "किम्".toVarnas(),
            output = "कि".toVarnas(),
            finalVarnaLopas = listOf(Vyanjana.MA),
        ),
    ),
    rootMorphology = mapOf(
        "शॄ" to UnadiRootMorphology(
            conditionedStem = "शृ".toVarnas(),
            requiredUpapadas = setOf("कि".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.R)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "सहोर".toVarnas(),
            meaning = Artha.Explanation("साधु"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("किशोर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अश्वशाव अर्थात् घोड़े का बच्चा"), "1.65"),
        UnadiLexicalAttestation("सहोर".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("साधु"), "1.65"),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("किशोर और सहोर सूत्र तथा वृत्ति में निर्दिष्ट ओरन्-अन्त निपातित प्रातिपदिक हैं।"),
    hindiExplanation = "पूर्वसूत्र से ओरन् प्रत्यय की अनुवृत्ति है; उसका अन्तिम न् इत्संज्ञक होकर लुप्त होता है और ओर शेष रहता है। वृत्ति किशोर की विशेष प्रक्रिया बताती है: हिंसार्थक क्र्यादिगणीय शॄ धातु से पहले किम् उपपद आता है। ‘किमोऽन्त्यलोपः’ से किम् का अन्तिम म्-वर्ण लुप्त होकर कि बचता है और ‘शृणातेः टिलोपः’ से शृ आधार का अन्तिम ऋ-वर्ण लुप्त होकर श् बचता है। अतः कि + श् + ओर से किशोर सिद्ध होता है, जिसका वृत्तिदत्त अर्थ अश्वशाव अर्थात् घोड़े का बच्चा है। ‘आदयः’ से उद्धृत सहोर रूप को वृत्ति साधु-अर्थ में देती है, पर उसकी कोई धातु अथवा पृथक् ध्वनि-प्रक्रिया नहीं बताती; इसलिए उसे अनुमानित धातु से न जोड़कर निपातित derived variant रखा गया है। दोनों रूप पुल्लिङ्ग में उद्धृत हैं और इनके बाहर कोई अतिरिक्त अर्थ नहीं जोड़ा गया।",
)
