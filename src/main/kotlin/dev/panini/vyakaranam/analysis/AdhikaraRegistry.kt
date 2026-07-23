package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada4.AkadaradEkaSamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnabhihiteSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PratyayahSutra
import dev.panini.sutra.Sutra

data class AdhikaraDomain(
    val sutra: Sutra<*, *>,
    val endKrama: Int,
    val customStartKrama: Int? = null,
    val isContextEligible: (VibhaktiRuleContext) -> Boolean = { true },
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
    )

    fun isVibhaktiEligible(sutraKrama: Int, context: VibhaktiRuleContext): Boolean {
        val activeDomains = domains.filter { sutraKrama in it.startKrama..it.endKrama }
        return activeDomains.all { it.isContextEligible(context) }
    }
}
