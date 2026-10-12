package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 8.2.24 restricts 8.2.23: after r, only cluster-final s is deleted. */
object RatSasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.24", text = "रात्सस्य",
    hindiExplanation = "रेफ के बाद संयोगान्त पद के अन्तिम स् का ही लोप होता है, अन्य व्यञ्जन का नहीं।",
    type = SutraType.APAVADA, chapter = 8, pada = 2, optional = false,
    kramaValue = 820024, role = SutraRole.Apavada, action = SutraAction.LOPA,
    scope = SutraScope.VARNA, dependencies = setOf("1.4.14"),
    blocks = setOf("8.2.23"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val term = context.terms.lastOrNull() ?: return false
        return context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PADA } &&
            term.varnas.takeLast(2) == listOf(Vyanjana.RA, Vyanjana.SA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context)) { "8.2.24 requires a pada ending in r-s." }
        val term = context.terms.last()
        return DerivationChange(
            context.deleteTermVarnas(term.id, term.varnas.lastIndex, 1, sutra)
                .copy(stage = DerivationStage.FINAL),
            "8.2.24: Deleted only cluster-final स् after र्.",
        )
    }
}
