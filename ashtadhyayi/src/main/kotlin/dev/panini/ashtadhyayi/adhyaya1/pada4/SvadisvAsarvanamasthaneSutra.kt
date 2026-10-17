package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 1.4.17 establishes a pada boundary before non-sarvanāmasthāna svādi. */
object SvadisvAsarvanamasthaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.17", text = "स्वादिष्वसर्वनामस्थाने",
    hindiExplanation = "सर्वनामस्थान से भिन्न स्वादि प्रत्यय परे पूर्व अङ्ग की पद संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140017,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private fun target(context: DerivationState) = context.terms.zipWithNext().firstOrNull { (stem, affix) ->
        stem.kind == TermKind.PRATIPADIKA && context.isEligibleNonSarvanamasthanaSvadi(affix) &&
            context.samjnas.none { it.targetId == stem.id && it.samjna == Samjna.PADA } &&
            (context.samjnas.none { it.targetId == stem.id && it.samjna == Samjna.BHA } ||
                affix.varnas.firstOrNull()?.let { it !is Svara && it != Vyanjana.YA } == true)
    }?.first

    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        return DerivationChange(context.copy(samjnas = context.samjnas.filterNot {
            it.targetId == stem.id && it.samjna == Samjna.BHA
        }.toSet()).withSamjnas(setOf(SamjnaAssignment(stem.id, Samjna.PADA))),
            "1.4.17 designates the preceding stem as pada before eligible svādi.")
    }
}
