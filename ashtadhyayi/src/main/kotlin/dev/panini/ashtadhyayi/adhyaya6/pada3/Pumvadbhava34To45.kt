package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.sutra.*

private fun pumvadEligible(c: SamasaRuleContext): Boolean {
    val p = c.purvaPada
    val f = p.morphologicalFeatures
    return c.padas.size >= 2 && p.masculineCounterpart != null &&
        SamasaMorphologicalFeature.FEMININE_UUNG !in f && SamasaMorphologicalFeature.ORDINAL !in f &&
        SamasaMorphologicalFeature.PRIYADI !in f && SamasaMorphologicalFeature.K_UPADHA !in f &&
        SamasaMorphologicalFeature.BODY_PART_I_ENDING !in f && SamasaMorphologicalFeature.JATI !in f &&
        !(SamasaMorphologicalFeature.VRDDHI_CAUSING_TADDHITA in f && SamasaMorphologicalFeature.COLOR_OR_MODIFICATION_TADDHITA !in f) &&
        SamasaSemanticRelation.PROPER_NAME !in c.semanticRelations
}
private fun masculineStem(c: SamasaRuleContext, rule: String) = SamasaRuleResult.Formed(
    requireNotNull(c.purvaPada.masculineCounterpart) + c.padas.drop(1).joinToString("") { it.upadesha },
    "$rule applies puṃvadbhāva using the explicitly supplied masculine counterpart.",
    memberEdits=mapOf(0 to requireNotNull(c.purvaPada.masculineCounterpart)),
)
private fun shortenFeminine(c: SamasaRuleContext, rule: String): SamasaRuleResult {
    val p = c.purvaPada.upadesha
    val short = when { p.endsWith("ी") -> p.dropLast(1) + "ि"; p.endsWith("ई") -> p.dropLast(1) + "इ"; else -> p }
    return SamasaRuleResult.Formed(short + c.padas.drop(1).joinToString("") { it.upadesha }, "$rule shortens the feminine final vowel.", memberEdits=mapOf(0 to short))
}
private val ghaClass = setOf("तर", "तम", "रूप", "कल्प", "चेल", "ब्रुव", "गोत्र", "मत", "हत")

/** 6.3.34: स्त्रियाः पुंवद्भाषितपुंस्कादनूङ् समानाधिकरणे स्त्रियामपूरणीप्रियादिषु. */
object StriyahPumvadBhasitapumskatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.34", text="स्त्रियाः पुंवद्भाषितपुंस्कादनूङ् समानाधिकरणे स्त्रियामपूरणीप्रियादिषु",
    hindiExplanation="समानाधिकरण स्त्री उत्तरपद में भाषितपुंस्क स्त्री पूर्वपद का पुंवद्भाव होता है, निर्दिष्ट अपवादों को छोड़कर।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630034, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.KARMADHARAYA, samasaPriority=20,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext)=context.samasaType==SamasaType.KARMADHARAYA && context.outputLinga==Linga.STRI && pumvadEligible(context)
    override fun apply(context: SamasaRuleContext)=masculineStem(context,"6.3.34")
}

/** 6.3.42: पुंवत्कर्मधारयजातीयदेशीयेषु (compound portion). */
object PumvatKarmadharayaJatiyaDesiyesuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.42", text="पुंवत्कर्मधारयजातीयदेशीयेषु", hindiExplanation="कर्मधारय में भाषितपुंस्क स्त्री पूर्वपद का पुंवद्भाव होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630042, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.KARMADHARAYA, samasaPriority=10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext)=context.samasaType==SamasaType.KARMADHARAYA && context.outputLinga!=Linga.STRI && pumvadEligible(context)
    override fun apply(context: SamasaRuleContext)=masculineStem(context,"6.3.42")
}

/** 6.3.43: घरूपकल्पचेलडब्रुवगोत्रमतहतेषु ङ्योऽनेकाचो ह्रस्वः. */
object GharupaKalpaCeladBruvaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.43", text="घरूपकल्पचेलडब्रुवगोत्रमतहतेषु ङ्योऽनेकाचो ह्रस्वः", hindiExplanation="निर्दिष्ट उत्तरपदों से पहले अनेकाच् ङी-अन्त स्त्री का अन्त्य स्वर ह्रस्व होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630043, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=20,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.FEMININE_NGI in context.purvaPada.morphologicalFeatures && context.uttaraPada.upadesha in ghaClass
    override fun apply(context: SamasaRuleContext)=shortenFeminine(context,"6.3.43")
}

/** 6.3.44: नद्याः शेषस्यान्यतरस्याम्. */
object NadyahSesasyanyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.44", text="नद्याः शेषस्यान्यतरस्याम्", hindiExplanation="शेष नदी-संज्ञक स्त्री का निर्दिष्ट उत्तरपदों से पहले विकल्प से ह्रस्व होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630044, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=10,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.NADI in context.purvaPada.morphologicalFeatures && context.uttaraPada.upadesha in ghaClass
    override fun apply(context: SamasaRuleContext)=shortenFeminine(context,"6.3.44")
}

/** 6.3.45: उगितश्च. */
object UgitashCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.45", text="उगितश्च", hindiExplanation="उगित्-व्युत्पन्न नदी का निर्दिष्ट उत्तरपदों से पहले विकल्प से ह्रस्व होता है।",
    type=SutraType.VIBHASHA, chapter=6, pada=3, optional=true, kramaValue=630045, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=context.padas.size>=2 && SamasaMorphologicalFeature.UGIT_DERIVED in context.purvaPada.morphologicalFeatures && context.uttaraPada.upadesha in ghaClass
    override fun apply(context: SamasaRuleContext)=shortenFeminine(context,"6.3.45")
}
