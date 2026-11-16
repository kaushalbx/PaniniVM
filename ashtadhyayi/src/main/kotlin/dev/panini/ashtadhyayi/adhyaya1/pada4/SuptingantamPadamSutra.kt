package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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

        if (context.stage == DerivationStage.ANGAKARYA) return true

        return context.terms.any { term ->
            val isVibhakti = context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
            isVibhakti && context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PADA }
        }
    }

    /**
     * 1.1.62 प्रत्ययलोपे प्रत्ययलक्षणम्: when the final sup/tiṅ is elided (as 7.1.23 does to अम्),
     * the word still ends in it and is a pada. 1.1.63 withholds only aṅga operations.
     */
    private fun lukPadaCarrier(context: DerivationState): Set<SamjnaAssignment> {
        val last = context.terms.lastOrNull() ?: return emptySet()
        if (context.terms.any { term -> context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA } }) return emptySet()
        val elidedVibhakti = context.droppedTerms.any { dropped ->
            (dropped.sourceSupAffix != null || dropped.kind == dev.panini.derivation.TermKind.PRATYAYA) &&
                context.samjnas.any { it.targetId == dropped.id && it.samjna == Samjna.PRATYAYA }
        }
        return if (elidedVibhakti) setOf(SamjnaAssignment(last.id, Samjna.PADA)) else emptySet()
    }

    override fun apply(context: DerivationState): DerivationChange {
        // In a full implementation, the 'Pada' Samjna often applies to the whole combination.
        // Here we mark the last term (affix) as the trigger for the Pada boundary.
        val assignments = context.terms.filter { term ->
            context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA }
        }.map { SamjnaAssignment(it.id, Samjna.PADA) }.toSet() + lukPadaCarrier(context)

        return DerivationChange(
            state = context.withSamjnas(assignments).copy(stage = DerivationStage.PADA_FORMED),
            explanation = "1.4.14 assigns पद संज्ञा to the term ending with a vibhakti."
        )
    }
}
