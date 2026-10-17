package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.20: dādhā ghvadāp.
 * The six source roots designated as dā/dhā are called ghu,
 * excluding dāp (cutting) and daip (purifying).
 */
object GhuSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.20",
    text = "दाधा घ्वदाप्",
    hindiExplanation = "निर्दिष्ट दा और धा स्वरूप वाली धातुओं की घु संज्ञा होती है; दाप् और दैप् को छोड़कर।",
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
            term.upadesha in ghuRootUpadeshas &&
            SamjnaAssignment(term.id, Samjna.GHU) !in context.samjnas
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms.filter {
            it.kind == TermKind.DHATU && it.upadesha in ghuRootUpadeshas
        }.mapTo(mutableSetOf()) { SamjnaAssignment(it.id, Samjna.GHU) }
        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.20 assigns घु to the six specified roots, excluding दाप् and दैप्."
        )
    }

    // Lexical upadeśa identities, not a predicate over the mutable phonological shape.
    private val ghuRootUpadeshas = setOf("डुदाञ्", "दाण्", "दो", "देङ्", "डुधाञ्", "धेट्")
}
