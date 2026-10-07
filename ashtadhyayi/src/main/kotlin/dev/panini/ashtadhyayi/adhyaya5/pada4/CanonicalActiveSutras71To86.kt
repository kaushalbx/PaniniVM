package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.SamasaType
import dev.panini.core.SamasantaAffix
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

abstract class ActiveSamasantaSutra(
    number: Int,
    text: String,
    optional: Boolean = false,
    samasaType: SamasaType,
    priority: Int = 10,
) : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.$number",
    text = text,
    hindiExplanation = "प्रामाणिक अर्थ-सन्दर्भ और उत्तरपद की शर्तों के अनुसार समासान्त-कार्य होता है।",
    type = if (optional) SutraType.VIBHASHA else SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = optional,
    kramaValue = 540000 + number,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = samasaType,
    samasaPriority = priority,
), SamasaSutra {
    protected fun formedWithAffix(context: SamasaRuleContext, affix: SamasantaAffix): SamasaRuleResult.Formed =
        formedWithSuffix(context, affix.varnas).copy(samasantaAffix = affix)

    /** Explicit suffix selection; no written-prefix inference of grammatical operations. */
    protected fun formedWithSuffix(context: SamasaRuleContext, suffix: List<Varna>): SamasaRuleResult.Formed =
        SamasaRuleResult.Formed(
            (context.padas.flatMap { it.varnas } + suffix).toDevanagari(),
            "$number selects the prescribed samāsānta suffix.",
            samasantaSuffix = suffix.toDevanagari(),
        )

    /** Exact final-member substitution, retaining the earlier members as separately editable terms. */
    protected fun formedWithFinalMember(context: SamasaRuleContext, replacement: List<Varna>): SamasaRuleResult.Formed =
        formedWithMembers(context, mapOf(context.padas.lastIndex to replacement))

    /** Prescribed multi-member substitutions, without collapsing their grammatical identities. */
    protected fun formedWithMembers(context: SamasaRuleContext, replacements: Map<Int, List<Varna>>): SamasaRuleResult.Formed {
        require(replacements.keys.all { it in context.padas.indices })
        return SamasaRuleResult.Formed(
            context.padas.flatMapIndexed { index, pada -> replacements[index] ?: pada.varnas }.toDevanagari(),
            "$number substitutes the prescribed compound members.",
            memberEdits = replacements.mapValues { (_, varnas) -> varnas.toDevanagari() },
        )
    }

}

/** 5.4.71 blocks the following samāsānta affixes in a nañ-tatpuruṣa. */
object NanjastatpurusatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.71", text = "नञस्तत्पुरुषात्",
    hindiExplanation = "नञ्-तत्पुरुष से उत्तरवर्ती समासान्त प्रत्ययों का निषेध होता है।",
    type = SutraType.NITYA, chapter = 5, pada = 4, optional = false, kramaValue = 540071,
    role = SutraRole.Niyama, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.NAN_TATPURUSA, samasaPriority = 30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext) = context.samasaType == SamasaType.NAN_TATPURUSA
    override fun apply(context: SamasaRuleContext) = SamasaRuleResult.NotApplicable
}

/** 5.4.72 optionally restores the a-affix for nañ + pathin. */
object PathoVibhasaSutra : ActiveSamasantaSutra(72, "पथो विभाषा", true, SamasaType.NAN_TATPURUSA, 40) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "पथिन्"
    override fun apply(context: SamasaRuleContext) =
        formedWithFinalMember(context, context.uttaraPada.varnas.dropLast(2) + Svara.A)
}

/** 5.4.73 requires a genuinely numerical external referent; lexical shape alone is insufficient. */
object BahuvrihauSankhyeyeDajabahuganatSutra : ActiveSamasantaSutra(73, "बहुव्रीहौ संख्येये डजबहुगणात्", samasaType = SamasaType.BAHUVRIHI) {
    override fun matches(context: SamasaRuleContext) =
        context.padas.size >= 2 && SamasaSemanticRelation.NUMERICAL_REFERENT in context.semanticRelations &&
            context.purvaPada.upadesha !in setOf("बहु", "गण")
    override fun apply(context: SamasaRuleContext) = formedWithSuffix(context, listOf(Svara.A))
}

object AcPratyanvavapurvatSamalomnahSutra : ActiveSamasantaSutra(75, "अच् प्रत्यन्ववपूर्वात् सामलोम्नः", samasaType = SamasaType.TATPURUSA) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 &&
        context.purvaPada.upadesha in setOf("प्रति", "अनु", "अव") && context.uttaraPada.upadesha in setOf("सामन्", "लोमन्")
    override fun apply(context: SamasaRuleContext) = formedWithFinalMember(context, context.uttaraPada.varnas.dropLast(1))
}

object UpasargadAdhvanahSutra : ActiveSamasantaSutra(85, "उपसर्गादध्वनः", samasaType = SamasaType.TATPURUSA) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "अध्वन्" &&
        Samjna.UPASARGA in context.purvaPada.samjnas
    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return formedWithFinalMember(context, context.uttaraPada.varnas.dropLast(1))
    }
}

object TatpurusasyangulehSankhyavyayadehSutra : ActiveSamasantaSutra(86, "तत्पुरुषस्याङ्गुलेः संख्याऽव्ययादेः", samasaType = SamasaType.TATPURUSA) {
    private val numerals = setOf("द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश")
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "अङ्गुलि" &&
        (context.purvaPada.upadesha in numerals || Samjna.AVYAYA in context.purvaPada.samjnas)
    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return formedWithFinalMember(context, context.uttaraPada.varnas.dropLast(1) + Svara.A)
    }
}
