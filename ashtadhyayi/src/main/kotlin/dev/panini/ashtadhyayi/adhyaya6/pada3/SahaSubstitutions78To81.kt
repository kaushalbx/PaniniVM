package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.sutra.*

private fun isSaha(c: SamasaRuleContext) = c.padas.size >= 2 && c.purvaPada.upadesha == "सह"
private fun saStem(c: SamasaRuleContext, rule: String): SamasaRuleResult {
    val u = c.padas.drop(1).joinToString("") { it.upadesha }
    val stem = when {
        u.startsWith("अ") || u.startsWith("आ") -> "सा${u.drop(1)}"
        u.startsWith("इ") || u.startsWith("ई") -> "से${u.drop(1)}"
        u.startsWith("उ") || u.startsWith("ऊ") -> "सो${u.drop(1)}"
        else -> "स$u"
    }
    return SamasaRuleResult.Formed(stem, "$rule substitutes स for सह.", memberEdits=mapOf(0 to "स"))
}

/** 6.3.78: सहस्य सः संज्ञायाम्. */
object SahasyaSahSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.78", text="सहस्य सः संज्ञायाम्", hindiExplanation="संज्ञा में सह के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630078, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=isSaha(context) && SamasaSemanticRelation.PROPER_NAME in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=saStem(context,"6.3.78")
}

/** 6.3.79: ग्रन्थान्ताधिके च. */
object GranthantadhikeCaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.79", text="ग्रन्थान्ताधिके च", hindiExplanation="ग्रन्थ के अन्त तक अथवा अधिक अर्थ में सह के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630079, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=isSaha(context) && context.semanticRelations.any { it == SamasaSemanticRelation.TEXT_COMPLETION || it == SamasaSemanticRelation.EXCESS }
    override fun apply(context: SamasaRuleContext)=saStem(context,"6.3.79")
}

/** 6.3.80: द्वितीये चानुपाख्ये. */
object DvitiyeCanupakhyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.80", text="द्वितीये चानुपाख्ये", hindiExplanation="अनुपाख्य द्वितीय वस्तु के सम्बन्ध में सह के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630080, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION, samasaPriority=30,
), SamasaSutra, UniversalSamasaTransformation {
    override fun matches(context: SamasaRuleContext)=isSaha(context) && SamasaSemanticRelation.INDIRECT_SECOND in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=saStem(context,"6.3.80")
}

/** 6.3.81: अव्ययीभावे चाकाले. */
object AvyayibhaveCakaleSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.81", text="अव्ययीभावे चाकाले", hindiExplanation="कालवाची उत्तरपद को छोड़कर अव्ययीभाव में सह के स्थान पर स होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630081, role=SutraRole.Vidhi, action=SutraAction.ADESHA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.AVYAYIBHAVA, samasaPriority=10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext)=isSaha(context) && context.samasaType == SamasaType.AVYAYIBHAVA && SamasaSemanticRelation.TIME_REFERENCE !in context.semanticRelations
    override fun apply(context: SamasaRuleContext)=saStem(context,"6.3.81")
}
