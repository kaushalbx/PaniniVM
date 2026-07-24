package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada4.AkadaradEkaSamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnabhihiteSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohAdhikaraSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PratyayahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.AngasyaAdhikaraSutra
import dev.panini.ashtadhyayi.adhyaya8.pada1.PadasyaAdhikaraSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraType

data class AdhikaraDomain(
    val sutra: Sutra<*, *>,
    val isDerivationEligible: (dev.panini.derivation.DerivationState) -> Boolean = { state ->
        if (sutra.type == SutraType.ADHIKARA || sutra.role == SutraRole.Adhikara) {
            val id = sutra.number
            id in state.activeAdhikaras || (sutra as? dev.panini.derivation.DerivationSutra)?.matches(state) == true
        } else {
            true
        }
    },
) {
    val endKrama: Int get() = sutra.endKrama ?: error("Adhikara domain ${sutra.number} must define an endKrama")
    val sutraNumber: String get() = sutra.number
    val sutraText: String get() = sutra.text
    val startKrama: Int get() = sutra.customStartKrama ?: sutra.krama
}

object AdhikaraRegistry {
    val domains: List<AdhikaraDomain> = listOf(
        AdhikaraDomain(
            sutra = AkadaradEkaSamjnaSutra,
        ),
        AdhikaraDomain(
            sutra = KarakeSutra,
        ),
        AdhikaraDomain(
            sutra = AnabhihiteSutra,
        ),
        AdhikaraDomain(
            sutra = PratyayahSutra,
        ),
        AdhikaraDomain(
            sutra = DhatohAdhikaraSutra,
        ),
        AdhikaraDomain(
            sutra = AngasyaAdhikaraSutra,
        ),
        AdhikaraDomain(
            sutra = PadasyaAdhikaraSutra,
        ),
    )

    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        val activeDomains = domains.filter { sutraKrama in it.startKrama..it.endKrama }
        return activeDomains.all { it.sutra.isContextEligible(context) }
    }

    fun isDerivationEligible(sutraKrama: Int, state: dev.panini.derivation.DerivationState): Boolean {
        val activeDomains = domains.filter { sutraKrama in it.startKrama..it.endKrama }
        return activeDomains.all { it.isDerivationEligible(state) }
    }
}
