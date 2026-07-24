package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada4.AkadaradEkaSamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnabhihiteSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohAdhikaraSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PratyayahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.AngasyaAdhikaraSutra
import dev.panini.ashtadhyayi.adhyaya8.pada1.PadasyaAdhikaraSutra
import dev.panini.sutra.Sutra

data class AdhikaraDomain(
    val sutra: Sutra<*, *>,
    val endKrama: Int,
    val customStartKrama: Int? = null,
    val isContextEligible: (VibhaktiRuleContext) -> Boolean = { true },
    val isDerivationEligible: (dev.panini.derivation.DerivationState) -> Boolean = { true },
) {
    val sutraNumber: String get() = sutra.number
    val sutraText: String get() = sutra.text
    val startKrama: Int get() = customStartKrama ?: sutra.krama
}

object AdhikaraRegistry {
    val domains: List<AdhikaraDomain> = listOf(
        AdhikaraDomain(
            sutra = AkadaradEkaSamjnaSutra,
            endKrama = 220038,
        ),
        AdhikaraDomain(
            sutra = KarakeSutra,
            endKrama = 140055,
        ),
        AdhikaraDomain(
            sutra = AnabhihiteSutra,
            customStartKrama = 230002,
            endKrama = 230073,
            isContextEligible = { context -> !context.abhihita },
        ),
        AdhikaraDomain(
            sutra = PratyayahSutra,
            endKrama = 540160,
        ),
        AdhikaraDomain(
            sutra = DhatohAdhikaraSutra,
            endKrama = 340117,
            isDerivationEligible = { state ->
                "3.1.91" in state.activeAdhikaras || DhatohAdhikaraSutra.matches(state)
            }
        ),
        AdhikaraDomain(
            sutra = AngasyaAdhikaraSutra,
            endKrama = 740097,
            isDerivationEligible = { state ->
                state.terms.size >= 2 && state.terms.any { it.kind == dev.panini.derivation.TermKind.PRATYAYA }
            }
        ),
        AdhikaraDomain(
            sutra = PadasyaAdhikaraSutra,
            endKrama = 830119,
            isDerivationEligible = { true }
        ),
    )

    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        val activeDomains = domains.filter { sutraKrama in it.startKrama..it.endKrama }
        return activeDomains.all { it.isContextEligible(context) }
    }

    fun isDerivationEligible(sutraKrama: Int, state: dev.panini.derivation.DerivationState): Boolean {
        val activeDomains = domains.filter { sutraKrama in it.startKrama..it.endKrama }
        return activeDomains.all { it.isDerivationEligible(state) }
    }
}
