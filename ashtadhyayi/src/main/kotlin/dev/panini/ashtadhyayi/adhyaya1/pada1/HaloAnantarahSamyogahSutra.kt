package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 1.1.7: halo'nantarāḥ saṃyogaḥ.
 * Consonants uninterrupted by vowels are called 'saṃyoga'.
 */
object HaloAnantarahSamyogahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.7",
    text = "हलोऽनन्तराः संयोगः",
    hindiExplanation = "अव्यवहित हल् वर्णों को संयोग संज्ञा दी जाती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110007,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA, SutraInput.PRATYAHARA),
    stage = SutraStage.SAMJNA,
    traceTemplateValue = "{sutra} assigns संयोग to an uninterrupted hal cluster.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any { term ->
        hasConsonantCluster(term.surface) && context.samjnas.none {
            it.targetId == term.id && it.samjna == Samjna.SAMYOGA
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms.filter { hasConsonantCluster(it.surface) }
            .map { SamjnaAssignment(it.id, Samjna.SAMYOGA) }.toSet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "$sutra assigns संयोग to eligible consonant clusters."
        )
    }

    private fun hasConsonantCluster(surface: String): Boolean {
        var count = 0
        surface.forEach { char ->
            when {
                Varnamala.isConsonant(char) -> {
                    count++
                    if (count >= 2) return true
                }
                char == Vyanjana.VIRAMA -> Unit
                else -> count = 0
            }
        }
        return false
    }
}
