package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraRole

/*
object AdhikaraRegistry {
    val domains: List<Sutra<*, *>> by lazy {
        Ashtadhyayi.registry.sutras.filter { it.role is SutraRole.Adhikara }
    }

    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        val activeDomains = domains.filter { domain ->
            val role = domain.role as SutraRole.Adhikara
            val start = role.customStartKrama ?: domain.krama
            val end = role.endKrama
            sutraKrama in start..end
        }
        return activeDomains.all { (it.role as SutraRole.Adhikara).isContextEligible(context) }
    }

    fun isDerivationEligible(sutraKrama: Int, state: dev.panini.derivation.DerivationState): Boolean {
        val activeDomains = domains.filter { domain ->
            val role = domain.role as SutraRole.Adhikara
            val start = role.customStartKrama ?: domain.krama
            val end = role.endKrama
            sutraKrama in start..end
        }
        return activeDomains.all { domain ->
            domain.number in state.activeAdhikaras || (domain as? dev.panini.derivation.DerivationSutra)?.matches(state) == true
        }
    }
}
*/
