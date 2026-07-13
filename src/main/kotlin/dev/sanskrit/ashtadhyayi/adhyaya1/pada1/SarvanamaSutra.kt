package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaIds
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
            GanaPatha.contains(GanaIds.SARVADI, term.surface) &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMA }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.kind == TermKind.PRATIPADIKA && GanaPatha.contains(GanaIds.SARVADI, it.surface) }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.27 assigns सर्वनाम संज्ञा to eligible stems."
        )
    }
}
