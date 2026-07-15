package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 8.2.23: saṃyogāntasya lopaḥ.
 * At the end of a pada, if there is a consonant cluster (saṃyoga), 
 * its final member (per 1.1.52) is deleted.
 */
object SamyogantasyaLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.23",
    text = "संयोगान्तस्य लोपः",
    hindiExplanation = "पदान्त में संयोग (हल्-समुदाय) के अन्तिम वर्ण का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820023,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
    dependencies = setOf("1.1.7", "1.4.14", "1.1.52")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        
        // 1. Must be a pada (per 1.4.14)
        val isPada = context.samjnas.any { it.targetId == lastTerm.id && it.samjna == Samjna.PADA }
        if (!isPada) return false
 
        // 2. Must end in a consonant cluster (saṃyoga, per 1.1.7)
        val surface = lastTerm.surface
        if (surface.length < 3) return false
        
        return surface.endsWith('्') && surface[surface.length - 3] == '्'
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        // 1.1.52: Delete only the final member of the cluster (consonant + virama)
        val newSurface = lastTerm.surface.dropLast(2)
        
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.2.23: Deleted final member of consonant cluster at pada-end."
        )
    }
}
