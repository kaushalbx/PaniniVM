package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika

/**
 * Sūtra 2.3.33 करणे च स्तोकाल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य.
 * Assigns Pañcamī or Tṛtīyā for non-substance words stoka, alpa, kṛcchra, katipaya.
 */
object StokanAlpaKrcchraSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.33", text = "करणे च स्तोकाल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य",
    hindiExplanation = "द्रव्य-भिन्न अर्थ वाले स्तोक, अल्प, कृच्छ्र तथा कतिपय शब्दों में पञ्चमी अथवा तृतीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230033,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.28", "2.3.32"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA)

    fun hasNonSubstanceInstrument(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        val stem = ((participant.expression as? SubantaPada)?.pratipadika as? MulaPratipadika)
            ?.takeIf { it.vikaras.isEmpty() }?.text
        return context.karaka == Karaka.KARANA && stem in setOf("स्तोक", "अल्प", "कृच्छ्र", "कतिपय") &&
            SemanticRelation.NON_SUBSTANCE_MEANING in participant.semanticRelations
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            hasNonSubstanceInstrument(context) &&
            (Vibhakti.PANCHAMI in context.morphologicalCandidates || Vibhakti.TRTIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.PANCHAMI in context.morphologicalCandidates) Vibhakti.PANCHAMI else Vibhakti.TRTIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes non-substance quantity (2.3.33)."),
        )
    }
}
