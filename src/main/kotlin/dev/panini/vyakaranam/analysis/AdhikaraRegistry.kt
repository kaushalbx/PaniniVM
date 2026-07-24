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

object AdhikaraRegistry {
    val domains: List<Sutra<*, *>> = listOf(
        AkadaradEkaSamjnaSutra,
        KarakeSutra,
        AnabhihiteSutra,
        PratyayahSutra,
        DhatohAdhikaraSutra,
        AngasyaAdhikaraSutra,
        PadasyaAdhikaraSutra,
    )

    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        val activeDomains = domains.filter { domain ->
            val start = domain.adhikaraMetadata?.customStartKrama ?: domain.krama
            val end = domain.adhikaraMetadata?.endKrama ?: error("Adhikara domain ${domain.number} must define an endKrama")
            sutraKrama in start..end
        }
        return activeDomains.all { it.adhikaraMetadata?.isContextEligible?.invoke(context) ?: true }
    }

    fun isDerivationEligible(sutraKrama: Int, state: dev.panini.derivation.DerivationState): Boolean {
        val activeDomains = domains.filter { domain ->
            val start = domain.adhikaraMetadata?.customStartKrama ?: domain.krama
            val end = domain.adhikaraMetadata?.endKrama ?: error("Adhikara domain ${domain.number} must define an endKrama")
            sutraKrama in start..end
        }
        return activeDomains.all { domain ->
            if (domain.type == SutraType.ADHIKARA || domain.role == SutraRole.Adhikara) {
                domain.number in state.activeAdhikaras || (domain as? dev.panini.derivation.DerivationSutra)?.matches(state) == true
            } else {
                true
            }
        }
    }
}
