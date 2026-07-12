package dev.sanskrit.ashtadhyayi.adhyaya1.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
            state = context, // Currently a virtual state in matching
            explanation = "1.4.108: Recognized avasāna (end of word)."
        )
    }
}
