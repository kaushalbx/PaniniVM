package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 8.3.14: r is deleted immediately before r, including within a pada. */
object RoRiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.14", text = "रो रि",
    hindiExplanation = "रेफ के बाद रेफ होने पर पहले रेफ का लोप होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false,
    kramaValue = 830014, role = SutraRole.Vidhi, action = SutraAction.LOPA,
    scope = SutraScope.VARNA, stage = SutraStage.POST_RUTVA,
), DerivationSutra {
    private fun target(context: DerivationState): Pair<DerivationTerm, Int>? {
        context.terms.forEachIndexed { termIndex, term ->
            term.varnas.indices.forEach { index ->
                val next = term.varnas.getOrNull(index + 1)
                    ?: context.terms.getOrNull(termIndex + 1)?.varnas?.firstOrNull()
                if (term.varnas[index] == Vyanjana.RA && next == Vyanjana.RA) return term to index
            }
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (term, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(term.id, index, emptyList(), sutra),
            "8.3.14: Deleted the first र् immediately before र्.")
    }
}
