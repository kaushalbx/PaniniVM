package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.23 बहुगणवतुडति संख्या.
 * Assigns saṅkhyā saṃjñā to bahu, gaṇa, vatu, ḍati.
 */
object TatiSankhyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.23", text = "बहुगणवतुडति संख्या",
    hindiExplanation = "बहु, गण, वतु तथा डति शब्दों की 'संख्या' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110023,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("बहु", "गण", "वतु", "डति") || it.surface in setOf("बहु", "गण", "कति") } &&
        "1.1.23" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.23"),
            explanation = "1.1.23 assigns संख्या saṃjñā to bahu, gaṇa, vatu, ḍati.",
        )
}
