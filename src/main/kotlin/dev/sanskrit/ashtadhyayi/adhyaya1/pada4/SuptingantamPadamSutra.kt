package dev.sanskrit.ashtadhyayi.adhyaya1.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.4.14: suptiṅantaṃ padam.
 * A term ending in a sup (nominal) or tiṅ (verbal) affix is called a 'pada'.
 */
object SuptingantamPadamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.14",
    text = "सुप्तिङन्तं पदम्",
    hindiExplanation = "सुप् और तिङ् प्रत्यय जिसके अन्त में हों, उसकी पद संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140014,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.IT_PROCESSED && context.stage != DerivationStage.ANGAKARYA) return false
        // Find if any term sequence is ready to be a Pada. 
        // For simplicity, we check if a term is a PRATYAYA that is a vibhakti.
        return context.terms.any { term ->
            val isVibhakti = context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
            isVibhakti && context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PADA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // In a full implementation, the 'Pada' Samjna often applies to the whole combination.
        // Here we mark the last term (affix) as the trigger for the Pada boundary.
        val assignments = context.terms.filter { term ->
            context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
        }.map { SamjnaAssignment(it.id, Samjna.PADA) }.toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "1.4.14 assigns पद संज्ञा to the term ending with a vibhakti."
        )
    }
}
