package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.InterpretivePrinciple
import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.ParibhashaVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.71 आदिरन्त्येन सहेता.
 * Defines pratyāhāra formation: initial sound combined with final it-marker denotes all intervening sounds.
 */
object AdirAntyenaSahetaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.71", text = "आदिरन्त्येन सहेता",
    hindiExplanation = "अन्तिम इत्-संज्ञक वर्ण के साथ आदि वर्ण अपने तथा मध्यवर्ती वर्णों का बोधक होता है (प्रत्याहार निर्माण)।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110071,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra, ParibhashaVidhayakaSutra {
    override val artha = InterpretivePrincipleArtha(InterpretivePrinciple.PRATYAHARA_RANGE)

    override fun matches(context: DerivationState): Boolean =
        "1.1.71" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.71"),
            explanation = "1.1.71 defines pratyāhāra formation principle.",
        )
}
