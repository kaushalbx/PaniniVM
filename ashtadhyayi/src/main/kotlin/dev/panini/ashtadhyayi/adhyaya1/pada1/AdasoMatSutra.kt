package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** Adas identity survives inflection; spelling alone does not establish origin.
 * The long vowels produced by 8.2.80/81 are visible to this specific designation. */
object AdasoMatSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.12", text = "अदसो मात्",
    hindiExplanation = "अदस् से बने पूर्ण पद में मकार के बाद अन्तिम ईकार या ऊकार की प्रगृह्य संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110012,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VARNA,
), DerivationSutra {
    private fun targets(context: DerivationState): List<DerivationTerm> = context.terms.filter { term ->
        term.kind == TermKind.PRATIPADIKA && term.upadesha == "अदस्" &&
            (term.formedPadaRupa != null || (context.stage >= DerivationStage.PADA_FORMED &&
                context.terms.none { it.kind == TermKind.PRATYAYA })) &&
            term.varnas.lastOrNull() in setOf(Svara.II, Svara.UU) &&
            term.varnas.getOrNull(term.varnas.lastIndex - 1) == Vyanjana.MA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.PRAGRHYA }
    }
    override fun matches(context: DerivationState): Boolean = targets(context).isNotEmpty()
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.withSamjnas(targets(context).map { SamjnaAssignment(it.id, Samjna.PRAGRHYA) }.toSet()),
        "1.1.12 designates completed adas ī/ū immediately after m as pragṛhya, including the forms produced by mutva.")
}
