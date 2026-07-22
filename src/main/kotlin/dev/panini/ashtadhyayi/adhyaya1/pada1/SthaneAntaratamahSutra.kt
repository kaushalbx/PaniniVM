package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.50: sthāne'ntaratamaḥ.
 * In a substitution, the most similar substitute (based on place of articulation, etc.) is chosen.
 */
object SthaneAntaratamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.50",
    text = "स्थानेऽन्तरतमः",
    hindiExplanation = "प्रसङ्ग प्राप्त होने पर सदृशतम आदेश होता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110050,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean = false // It's an interpretive rule, not a state-transition rule.

    override fun apply(context: DerivationState): DerivationChange =
        error("Paribhasha sutra 1.1.50 should not be applied directly as a state transition.")

    /**
     * Given a source character and a set of possible substitutes, returns the most similar one.
     */
    fun selectBest(source: Char, substitutes: Set<String>): String {
        if (substitutes.size <= 1) return substitutes.firstOrNull() ?: ""

        val sourceSthanas = Varnamala.getSthana(source)

        return substitutes.maxByOrNull { substitute ->
            val subChar = substitute.firstOrNull() ?: return@maxByOrNull 0
            val subSthanas = Varnamala.getSthana(subChar)

            // Score based on overlap of sthanas
            (sourceSthanas intersect subSthanas).size
        } ?: substitutes.first()
    }
}
