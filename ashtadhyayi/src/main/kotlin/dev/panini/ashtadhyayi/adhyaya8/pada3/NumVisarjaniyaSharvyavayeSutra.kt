package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.3.58 नुम्विसर्जनीयशर्व्यवायेऽपि — the स् of a pratyaya becomes ष् after इण् or कु even when a नुम्,
 * a visarga or a शर् stands between: धनुस् + शि → धनूंषि, सर्पिस् + सुप् → सर्पिःषु.
 * The intervening anusvāra must be the नुम् (a न् inserted by a 7.1 rule, then 8.3.24): पुंसु, whose
 * anusvāra is the stem's own, keeps स्.
 */
object NumVisarjaniyaSharvyavayeSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.58", text = "नुम्विसर्जनीयशर्व्यवायेऽपि",
    hindiExplanation = "नुम्, विसर्ग या शर् के व्यवधान में भी इण्-कु से परे आदेश-प्रत्यय के सकार को षकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830058,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val ku: Set<Varna> = setOf(Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA, Vyanjana.NGA)
    private val shar: Set<Varna> = setOf(Vyanjana.SHA, Vyanjana.SSA, Vyanjana.SA)

    private data class Located(val term: DerivationTerm, val index: Int)

    private fun DerivationState.isNumAnusvara(at: Located): Boolean =
        at.term.varnas[at.index] == Ayogavaha.ANUSVARA &&
            substitutions.any { it.targetId == at.term.id && it.sutra == "8.3.24" && it.sourceVarnaIndex == at.index } &&
            substitutions.any { it.targetId == at.term.id && it.sutra.startsWith("7.1.") &&
                it.sourceVarnaIndex == null && it.replacementVarnas == listOf(Vyanjana.NA) }

    private fun target(state: DerivationState): Located? {
        if (state.stage != DerivationStage.PADA_FORMED && state.stage != DerivationStage.FINAL) return null
        val engine = Ashtadhyayi.pratyaharaEngine
        val located = state.terms.flatMap { term -> term.varnas.indices.map { Located(term, it) } }
        for ((position, here) in located.withIndex()) {
            val term = here.term
            if (term.kind != TermKind.PRATYAYA || term.varnas[here.index] != Vyanjana.SA) continue
            if (term.itProcessingPhase != ItProcessingPhase.PROCESSED ||
                term.itDesignations.isNotEmpty() || term.deferredItDesignations.isNotEmpty()) continue
            // 8.3.55 अपदान्तस्य: not the last sound of the word.
            if (position == located.lastIndex) continue
            val between = located.getOrNull(position - 1) ?: continue
            val before = located.getOrNull(position - 2) ?: continue
            val intervenes = between.term.varnas[between.index].let { it == Ayogavaha.VISARGA || it in shar } ||
                state.isNumAnusvara(between)
            val trigger = before.term.varnas[before.index]
            if (intervenes && (engine.contains(Pratyahara.IN, trigger) || trigger in ku)) return here
        }
        return null
    }

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val here = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(here.term.id, here.index, listOf(Vyanjana.SSA), sutra),
            "8.3.58 gives ष् for the स् of ${here.term.upadesha} across a नुम्, visarga or शर्.")
    }
}
