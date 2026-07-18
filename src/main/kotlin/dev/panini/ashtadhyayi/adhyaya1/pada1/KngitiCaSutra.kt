package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItMarker
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

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
