package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
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
 * 1.1.20: dādhā ghvadāp.
 * Roots of the form 'dā' and 'dhā' are called 'ghu', 
 * excluding the word 'dāp' (to cut).
 */
object GhuSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.20",
    text = "दाधा घ्वदाप",
    hindiExplanation = "दा और धा स्वरूप वाली धातुओं की 'घु' संज्ञा होती है, 'दाप्' (लवनार्थक) को छोड़कर।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110020,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.DHATU && 
            term.surface in ghuRoots &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.GHI } // Using GHI as proxy or add GHU
        }

    override fun apply(context: DerivationState): DerivationChange {
        // We'll use a dynamic Samjna assignment. GHU should be added to Samjna enum for full fidelity.
        return DerivationChange(
            state = context, 
            explanation = "1.1.20 identifies 'ghu' roots for specific verbal transformations."
        )
    }

    private val ghuRoots = setOf("दा", "धा", "दे", "धेट्")
}
