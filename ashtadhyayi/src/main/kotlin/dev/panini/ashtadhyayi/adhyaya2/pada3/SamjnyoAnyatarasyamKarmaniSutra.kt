package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.core.Upasarga
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.OptionalVibhaktiRuleOutcome

/**
 * Sūtra 2.3.22 संज्ञोऽन्यतरस्यांकर्मणि.
 * Option for Tṛtīyā (or Dvitīyā) for the karman of sam + jñā.
 */
object SamjnyoAnyatarasyamKarmaniSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.22", text = "संज्ञोऽन्यतरस्यांकर्मणि",
    hindiExplanation = "सम्-पूर्वक ज्ञा धातु के अनभिहित कर्म में तृतीया अथवा द्वितीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230022,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.18"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA)
    /** Later 2.3.65 wins in the overlapping kṛt construction (vipratiṣedha). */
    fun hasPreferredKrtGenitive(context: VibhaktiRuleContext): Boolean {
        if (context.governingKrt == null) return false
        val participant = context.participant ?: return false
        if (context.karaka != Karaka.KARMAN ||
            SemanticRelation.SAM_JNA_OBJECT !in participant.semanticRelations ||
            !hasCompatibleGovernor(context) || participant.isKrtProhibitedForSasthi ||
            participant.krtGenitiveProhibition != null) return false
        return KartrKarmanohKrtiSutra.matches(context.copy(morphologicalCandidates = setOf(Vibhakti.SASTHI)))
    }
    private fun hasCompatibleGovernor(context: VibhaktiRuleContext): Boolean {
        fun matches(root: String, prefixes: List<Upasarga?>) =
            root in setOf("ज्ञा", "ज्ञा॒") && prefixes.none { it == null } && Upasarga.SAM in prefixes
        // A linked nominal governor owns its argument; the enclosing finite
        // verb cannot supply the missing prefix or override that governor.
        context.governingKrt?.let { return matches(it.dhatu.mulaDhatu, it.upasargaIdentities) }
        (context.verbNode as? TingantaPada)?.let { return matches(it.dhatu.mulaDhatu, it.upasargaIdentities) }
        // The low-level API can supply the complete construction premise when
        // no governing AST is available. It cannot contradict an actual AST.
        return true
    }
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARMAN &&
            hasCompatibleGovernor(context) &&
            !hasPreferredKrtGenitive(context) &&
            SemanticRelation.SAM_JNA_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes karman for sam-jñā dhātu (2.3.22)."),
        )
    }
}
