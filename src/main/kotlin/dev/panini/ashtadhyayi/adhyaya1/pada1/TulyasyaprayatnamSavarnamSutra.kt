package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.derivation.SamjnaAssignment
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 1.1.9 labels explicitly compared like-class sounds as savarna. */
object TulyasyaprayatnamSavarnamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.9",
    text = "तुल्यास्यप्रयत्नं सवर्णम्",
    hindiExplanation = "तुल्य आस्य और प्रयत्न वाले वर्ण सवर्ण होते हैं।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110009,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    stage = SutraStage.SAMJNA,
    traceTemplateValue = "{sutra} assigns सवर्ण to sounds with matching place and effort.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = eligible(context).any { comparison ->
        SamjnaAssignment(comparison.leftTermId, Samjna.SAVARNA) !in context.samjnas ||
                SamjnaAssignment(comparison.rightTermId, Samjna.SAVARNA) !in context.samjnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = eligible(context).flatMap { comparison ->
            listOf(
                SamjnaAssignment(comparison.leftTermId, Samjna.SAVARNA),
                SamjnaAssignment(comparison.rightTermId, Samjna.SAVARNA)
            )
        }.toSet()
        return DerivationChange(
            context.withSamjnas(assignments),
            "$sutra assigns सवर्ण to matched varṇa comparisons."
        )
    }
}

private fun eligible(state: DerivationState) = state.varnaComparisons.filter { comparison ->
    comparison.samePlaceAndEffort && !comparison.forbidden && comparison.leftIsVowel == comparison.rightIsVowel
}
