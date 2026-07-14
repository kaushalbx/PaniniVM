package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.Vacana
import dev.sanskrit.derivation.Vibhakti
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 8.4.1: ra-ṣābhyāṃ no ṇaḥ samānapade.
 * Changes dental 'n' to retroflex 'ṇ' if immediately preceded by 'r' or 'ṣ' 
 * in the same word (pada).
 */
object RasabhyamNoNahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.1",
    text = "रषाभ्यां नो णः समानपदे",
    hindiExplanation = "एक ही पद में र् या ष् के ठीक बाद आने वाले 'न' का 'ण' हो जाता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (HasMorphosyntax(vibhakti = Vibhakti.DVITIYA, vacana = Vacana.BAHUVACANA).matches(context)) return false

        val surface = context.surface
        
        // Find triggers: र (r) or ष (ṣ)
        val triggerIndex = surface.lastIndexOfAny(setOf('र', 'ष', 'ऋ', 'ृ', 'ॠ', 'ॄ'))
        if (triggerIndex == -1) return false
        
        // Find target: न (n)
        val targetIndex = surface.indexOf('न', triggerIndex)
        if (targetIndex == -1) return false

        // 8.4.1 requires NO intervening characters except virama
        val intervenors = surface.substring(triggerIndex + 1, targetIndex).replace("्", "")
        return intervenors.isEmpty()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val surface = context.surface
        val triggerIndex = surface.lastIndexOfAny(setOf('र', 'ष', 'ऋ', 'ृ', 'ॠ', 'ॄ'))
        val targetIndex = surface.indexOf('न', triggerIndex)
        
        var offset = 0
        val targetTerm = context.terms.find { term ->
            val start = offset
            offset += term.surface.length
            targetIndex in start until offset
        } ?: return DerivationChange(context, "8.4.1: Target 'n' not found.")

        val newSurface = targetTerm.surface.replaceFirst('न', 'ण')
        
        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.1: Retroflexed 'n' to 'ṇ' immediately following '${surface[triggerIndex]}'."
        )
    }

    private fun String.lastIndexOfAny(chars: Set<Char>): Int {
        for (i in length - 1 downTo 0) {
            if (this[i] in chars) return i
        }
        return -1
    }
}
