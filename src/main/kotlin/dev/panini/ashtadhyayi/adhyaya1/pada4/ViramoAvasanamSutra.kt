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
 * 1.4.108: virāmo'vasānam.
 * The cessation of effort (pause/end of word) is called 'avasāna'.
 */
object ViramoAvasanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.108",
    text = "विरामोऽवसानम्",
    hindiExplanation = "वर्णों के अभाव (विराम) की अवसान संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140108,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Formally, avasāna exists at the end of the term sequence.
        // In our engine, we assign this Samjna to the last term when it's ready for final sandhi.
        return context.stage == DerivationStage.PADA_FORMED &&
               context.samjnas.none { it.samjna == Samjna.AC && it.targetId == "avasana" } // Placeholder
    }

    override fun apply(context: DerivationState): DerivationChange {
        // We use a special ID to mark the 'end' of the derivation as having avasāna status.
        return DerivationChange(
            state = context.withSamjnas(setOf(SamjnaAssignment("avasana", Samjna.AC))),
            explanation = "1.4.108: Recognized avasāna (end of word)."
        )
    }
}
