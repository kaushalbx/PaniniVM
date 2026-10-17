package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

internal data class HaClusterBoundary(val left: DerivationTerm, val right: DerivationTerm)

/** Immediate h + nasal cluster, not h followed by an intervening vowel. */
internal fun haClusterBoundary(state: DerivationState, nasal: Vyanjana): HaClusterBoundary? {
    for ((index, left) in state.terms.withIndex()) {
        if (left.varnas.lastOrNull() != Vyanjana.MA ||
            !state.isPadaFinalVarna(left, left.varnas.lastIndex)) continue
        val following = state.terms.drop(index + 1).filter { it.varnas.isNotEmpty() }
        val right = following.firstOrNull() ?: continue
        if (state.isBlockedAtBoundary("8.3.23", left.id, right.id)) continue
        val sounds = following.asSequence().flatMap { it.varnas.asSequence() }.take(2).toList()
        if (sounds == listOf(Vyanjana.HA, nasal)) return HaClusterBoundary(left, right)
    }
    return null
}

object HeMapareVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.26", text = "हे मपरे वा",
    hindiExplanation = "मकार के बाद मकारपरक हकार होने पर विकल्प से मकार रहता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830026,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI, blocks = setOf("8.3.23"),
), DerivationSutra {
    override val advancesTripadiFrontier = false
    override fun matches(context: DerivationState) = haClusterBoundary(context, Vyanjana.MA) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, right) = requireNotNull(haClusterBoundary(context, Vyanjana.MA))
        return DerivationChange(context.blockAtBoundary("8.3.23", left.id, right.id, sutra),
            "8.3.26 optionally retains final m before immediate h+m; decline leaves anusvara applicable.")
    }
}

object NapareNahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.27", text = "नपरे नः",
    hindiExplanation = "मकार के बाद नकारपरक हकार होने पर विकल्प से नकार होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830027,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI, blocks = setOf("8.3.23"),
), DerivationSutra {
    override fun matches(context: DerivationState) = haClusterBoundary(context, Vyanjana.NA) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (left, _) = requireNotNull(haClusterBoundary(context, Vyanjana.NA))
        return DerivationChange(context.replaceTermVarna(left.id, left.varnas.lastIndex, listOf(Vyanjana.NA), sutra),
            "8.3.27 optionally substitutes n for final m before immediate h+n.")
    }
}
