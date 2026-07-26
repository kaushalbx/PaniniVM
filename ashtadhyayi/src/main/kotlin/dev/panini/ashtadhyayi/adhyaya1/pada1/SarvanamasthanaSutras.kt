package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.42: śī sarvanāmasthānam.
 * In neuter, the substitute 'śī' (for jas/śas) is called 'sarvanāmasthāna'.
 */
object ShiSarvanamasthanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.42",
    text = "शि सर्वनामस्थानम्",
    hindiExplanation = "नपुंसकलिङ्ग में 'शि' (जस्/शस् के स्थान पर) की सर्वनामस्थान संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110042,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (!HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context)) return false

        return context.terms.any { term ->
            term.upadesha == "शि" &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMASTHANA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.upadesha == "शि" }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMASTHANA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.42 assigns सर्वनामस्थान संज्ञा to 'śi' in neuter."
        )
    }
}

