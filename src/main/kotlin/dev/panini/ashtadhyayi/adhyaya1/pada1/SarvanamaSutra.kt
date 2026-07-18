package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.27: sarvādīni sarvanāmāni.
 * Stems in the list starting with 'sarva' are called 'sarvanāma' (pronouns).
 */
object SarvanamaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.27",
    text = "सर्वादीनी सर्वनामानि",
    hindiExplanation = "सर्व आदि शब्दों की सर्वनाम संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110027,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
            GanaPatha.isEligibleMember(1, term.surface, term.lexicalUses) &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter {
                it.kind == TermKind.PRATIPADIKA &&
                    GanaPatha.isEligibleMember(1, it.surface, it.lexicalUses)
            }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.27 assigns सर्वनाम संज्ञा to eligible stems."
        )
    }
}
