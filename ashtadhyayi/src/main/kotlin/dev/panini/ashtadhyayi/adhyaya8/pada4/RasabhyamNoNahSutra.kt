package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.1: ra-ṣābhyāṃ no ṇaḥ samānapade.
 * Changes dental 'n' to retroflex 'ṇ' if immediately preceded by 'r' or 'ṣ'
 * in the same word (pada).
 */
object RasabhyamNoNahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.1",
    text = "रषाभ्यां नो णः समानपदे",
    hindiExplanation = "एक ही पद में र् या ष् के ठीक बाद आने वाले 'न' का 'ण' हो जाता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (HasMorphosyntax(vibhakti = Vibhakti.DVITIYA, vacana = Vacana.BAHUVACANA).matches(context)) return false

        // 3.4.105 substitutes रन् for the liṅ Ātmanepada झ ending. The
        // attested potential ending remains dental, e.g. लभेरन्.
        if (context.terms.any { it.varnas == listOf(Vyanjana.RA, Svara.A, Vyanjana.NA) }) return false
        return findTarget(context) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = findTarget(context) ?: return DerivationChange(context, "8.4.1: Target 'n' not found.")

        return DerivationChange(
            state = context.replaceTermVarna(target.term.id, target.varnaIndex, listOf(Vyanjana.NNA), sutra)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.1: Retroflexed 'n' to 'ṇ' immediately following '${target.trigger.devanagari}'."
        )
    }

    private fun findTarget(context: DerivationState): Target? {
        val positions = context.terms.flatMap { term -> term.varnas.indices.map { term to it } }
        for (i in 0 until positions.lastIndex) {
            val (triggerTerm, triggerIndex) = positions[i]
            val trigger = triggerTerm.varnas[triggerIndex]
            if (trigger !in triggers) continue
            val (targetTerm, targetIndex) = positions[i + 1]
            if (targetTerm.varnas[targetIndex] == Vyanjana.NA) return Target(targetTerm, targetIndex, trigger)
        }
        return null
    }

    private val triggers = setOf(Vyanjana.RA, Vyanjana.SSA, Svara.R, Svara.RR)
    private data class Target(val term: DerivationTerm, val varnaIndex: Int, val trigger: dev.panini.shiksha.Varna)
}
