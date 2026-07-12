package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraInput
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraStage
import dev.sanskrit.sutra.SutraType

/** 
 * 1.1.5: kṅiti ca.
 * Guna and vrddhi are prohibited when a kit (marked with K) or ngit (marked with NG) 
 * affix follows.
 */
object KngitiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.5",
    text = "क्ङिति च",
    hindiExplanation = "कित् और ङित् प्रत्यय के परे होने पर गुण और वृद्धि का निषेध होता है।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110005,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("1.1.3", "7.3.84", "6.1.87") // Blocks specific substitution rules
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // 1.1.56: We check 'effective' markers (current or inherited from sthānī)
        val affix = context.terms.lastOrNull() ?: return false
        val isKngit = affix.hasEffectiveMarker(ItMarker.KIT) || affix.hasEffectiveMarker(ItMarker.NGIT)
        
        return isKngit && context.blockedSutras["1.1.3"] != sutra
    }

    override fun apply(context: DerivationState): DerivationChange {
        return DerivationChange(
            state = context.blockSutra("1.1.3", sutra)
                .blockSutra("7.3.84", sutra)
                .blockSutra("6.1.87", sutra),
            explanation = "1.1.5 blocks guna/vrddhi before a kit/ngit affix."
        )
    }
}
