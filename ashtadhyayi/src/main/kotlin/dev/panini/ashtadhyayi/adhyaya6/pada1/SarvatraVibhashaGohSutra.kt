package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 6.1.122: optional prakṛtibhāva of pada-final go before short a. */
object SarvatraVibhashaGohSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.122", text = "सर्वत्र विभाषा गोः",
    hindiExplanation = "पदान्त गो के बाद ह्रस्व अकार होने पर विकल्प से प्रकृतिभाव होता है।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 1, optional = true,
    kramaValue = 610122, role = SutraRole.Apavada, action = SutraAction.NIYAMA,
    scope = SutraScope.VARNA, stage = SutraStage.SANDHI,
    priority = SutraPriority.APAVADA, blocks = setOf("6.1.78", "6.1.109"),
), DerivationSutra {
    private fun boundary(context: DerivationState): Int? =
        (0 until context.terms.lastIndex).firstOrNull { index ->
            val left = context.terms[index]
            val right = context.terms[index + 1]
            left.varnas == listOf(Vyanjana.GA, Svara.O) && right.varnas.firstOrNull() == Svara.A &&
                SamjnaAssignment(left.id, Samjna.PADA) in context.samjnas &&
                !context.isBlockedAtBoundary(sutra, left.id, right.id)
        }

    override fun matches(context: DerivationState): Boolean = boundary(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val index = requireNotNull(boundary(context))
        val left = context.terms[index]
        val right = context.terms[index + 1]
        val retained = listOf(sutra, "6.1.78", "6.1.109", "6.1.123").fold(context) { state, rule ->
            state.blockAtBoundary(rule, left.id, right.id, sutra)
        }
        return DerivationChange(retained, "6.1.122 retains गो and the following short अ at this boundary (प्रकृतिभाव).")
    }
}
