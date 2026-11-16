package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/**
 * 8.4.57 अणोऽप्रगृह्यस्यानुनासिकः — at a pause (वा, from 8.4.56) a final aṇ vowel that is not
 * pragṛhya is optionally nasalised: दधि → दधिँ. Aṇ is the first aṇ (अ इ उ) with its savarṇas (1.1.69).
 * By default the rule is declined, so a word at a pause keeps its usual spelling.
 */
object AnoPragrhyasyaAnunasikahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.57", text = "अणोऽप्रगृह्यस्यानुनासिकः",
    hindiExplanation = "अवसान में अप्रगृह्य अण् को विकल्प से अनुनासिक होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840057,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val an = setOf(Svara.A, Svara.AA, Svara.I, Svara.II, Svara.U, Svara.UU)

    private fun target(state: DerivationState): DerivationTerm? {
        if (state.effectiveContext.finalBoundary != SpeechBoundary.AVASANA) return null
        if (state.stage != DerivationStage.PADA_FORMED && state.stage != DerivationStage.FINAL) return null
        val term = state.terms.lastOrNull { it.varnas.isNotEmpty() } ?: return null
        val last = term.phonologicalText.effectiveVarnas.lastOrNull() ?: return null
        if (last.varna !in an || last.nasalized) return null
        if (state.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRAGRHYA }) return null
        return term
    }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val term = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(term.id, term.varnas.lastIndex, listOf(term.varnas.last()), sutra,
                nasalizedReplacement = true), "8.4.57 nasalises the final aṇ at avasāna."),
            DerivationChange(state, "8.4.57 declined.", applied = false))
    }
}
