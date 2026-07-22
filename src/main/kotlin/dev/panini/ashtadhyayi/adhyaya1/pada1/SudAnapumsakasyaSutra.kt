package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.SamjnaAssignment
import dev.panini.core.Linga
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.43: sud-anapuṃsakasya.
 * The first five case endings (su, au, jas, am, auṭ) are called 'sarvanāmasthāna'
 * except in the neuter gender.
 */
object SudAnapumsakasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.43",
    text = "सुडनपुंसकस्य",
    hindiExplanation = "नपुंसकलिङ्ग को छोड़कर प्रथमा के तीन और द्वितीया के दो प्रत्ययों की सर्वनामस्थान संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110043,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context)) return false

        return context.terms.any { term ->
            term.upadesha in firstFive &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMASTHANA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.upadesha in firstFive }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMASTHANA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.43 assigns सर्वनामस्थान संज्ञा to the first five case endings."
        )
    }

    private val firstFive = setOf("सुँ", "औ", "जस्", "अम्", "औट्")
}
