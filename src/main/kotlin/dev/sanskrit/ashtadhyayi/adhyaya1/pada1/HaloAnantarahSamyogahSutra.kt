package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraInput
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraStage
import dev.sanskrit.sutra.SutraType

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
