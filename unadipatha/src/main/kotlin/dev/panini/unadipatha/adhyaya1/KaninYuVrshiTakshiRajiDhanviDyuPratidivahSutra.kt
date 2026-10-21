package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.adadi.YuDhatu
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

private val sutra154Yu = YuDhatu()
private val sutra154Vrsh = BhvadiDhatus.all.single { it.id == "01.0803" }
private val sutra154Taksh = BhvadiDhatus.all.single { it.id == "01.0743" }
private val sutra154Raj = BhvadiDhatus.all.single { it.id == "01.0956" }
private val sutra154Dhanv = BhvadiDhatus.all.single { it.id == "01.0681" }
private val sutra154Dyu = AdadiDhatus.all.single { it.id == "02.0035" }
private val sutra154Div = DivDhatu()

// 1.154: कनिन् युवृषितक्षिराजिधन्विद्युप्रतिदिवः
object KaninYuVrshiTakshiRajiDhanviDyuPratidivahSutra : UnadiSutra(
    number = "1.154",
    text = "कनिन् युवृषितक्षिराजिधन्विद्युप्रतिदिवः",
    roots = setOf(sutra154Yu, sutra154Vrsh, sutra154Taksh, sutra154Raj, sutra154Dhanv, sutra154Dyu, sutra154Div),
    pratyaya = "कनिन्",
    pratyayaSurface = "अन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.I),
    rootSamjnaMap = mapOf(
        sutra154Yu.upadesha to Samjna.Rudhi("युवन्"),
        sutra154Vrsh.upadesha to Samjna.Rudhi("वृषन्"),
        sutra154Taksh.upadesha to Samjna.Rudhi("तक्षन्"),
        sutra154Raj.upadesha to Samjna.Rudhi("राजन्"),
        sutra154Dhanv.upadesha to Samjna.Rudhi("धन्वन्"),
        sutra154Dyu.upadesha to Samjna.Rudhi("द्युवन्"),
        sutra154Div.upadesha to Samjna.Rudhi("प्रतिदिवन्"),
    ),
    rootMeaningMap = mapOf(
        sutra154Vrsh.upadesha to Artha.Explanation("इन्द्र"),
        sutra154Dhanv.upadesha to Artha.Explanation("मरु अथवा शरासन"),
        sutra154Dyu.upadesha to Artha.Explanation("सूर्य"),
        sutra154Div.upadesha to Artha.Explanation("दिवस"),
    ),
    rootMorphology = mapOf(
        sutra154Yu.upadesha to UnadiRootMorphology(conditionedStem = "युव्".toVarnas()),
        sutra154Vrsh.upadesha to UnadiRootMorphology(conditionedStem = "वृष्".toVarnas()),
        sutra154Taksh.upadesha to UnadiRootMorphology(conditionedStem = "तक्ष्".toVarnas()),
        sutra154Raj.upadesha to UnadiRootMorphology(conditionedStem = "राज्".toVarnas()),
        sutra154Dhanv.upadesha to UnadiRootMorphology(conditionedStem = "धन्व्".toVarnas()),
        sutra154Dyu.upadesha to UnadiRootMorphology(conditionedStem = "द्युव्".toVarnas()),
        sutra154Div.upadesha to UnadiRootMorphology(
            conditionedStem = "दिव्".toVarnas(),
            requiredUpasargas = setOf("प्रति".toVarnas()),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant("प्रति".toVarnas(), "प्रतिदिवन्".toVarnas(), Artha.Explanation("दिवस"), sourceRoot = sutra154Div.upadesha),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("युवा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.154"),
        UnadiLexicalAttestation("वृषा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("इन्द्र"), "1.154"),
        UnadiLexicalAttestation("तक्षा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.154"),
        UnadiLexicalAttestation("राजा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.154"),
        UnadiLexicalAttestation("धन्वा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मरु"), "1.154"),
        UnadiLexicalAttestation("धन्व".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("शरासन"), "1.154"),
        UnadiLexicalAttestation("द्युवा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सूर्य"), "1.154"),
        UnadiLexicalAttestation("प्रतिदिवा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("दिवस"), "1.154"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "यु, वृष्, तक्ष्, राज्, धन्व्, द्यु और प्रति-उपसर्गपूर्वक दिव् धातुओं के बाद कनिन् प्रत्यय होता है। कनिन् में आरम्भिक क् तथा मध्य का इ इत्संज्ञक होकर लुप्त होते हैं और अन् श्रव्य रहता है; अन्तिम न् शब्दाधार का अंग है, इसलिए मूल प्रातिपदिक युवन्, वृषन्, तक्षन्, राजन्, धन्वन्, द्युवन् और प्रतिदिवन् हैं। यु तथा द्यु के उ का स्वरादि प्रत्यय से पहले यण्-वकार होने पर युव् और द्युव् आधार मिलते हैं; शेष आधार स्रोत-पाठ के अनुरूप हैं। इनके उद्धृत प्रथमा-एकवचन रूप युवा, वृषा, तक्षा, राजा, धन्वा, द्युवा और प्रतिदिवा हैं। वृत्ति वृषा को इन्द्र, धन्वा को मरु, नपुंसकलिङ्ग धन्व को शरासन, द्युवा को सूर्य और प्रतिदिवा को दिवस बताती है। प्रतिदिवन् शाखा में प्रति को पृथक् आवश्यक उपसर्ग के रूप में रखा गया है। यु के लिए ‘यौतीति’, तक्ष् के लिए तक्षा और राज् के लिए राजा से अधिक कोई अर्थ इस वृत्ति में नहीं दिया गया, इसलिए परिचित कोशार्थ नहीं जोड़े गए।",
)
