package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.9: Ato bhis ais.
 * Replaces instrumental-plural 'bhis' with 'ais' after an a-final stem.
 * This is an Apavāda to the general rule (not yet implemented) that would keep 'bhis'.
 */
object AtoBhisAisSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.9",
    text = "अतो भिस ऐस्",
    hindiExplanation = "अकारान्त अङ्ग के बाद भिस् के स्थान पर ऐस् होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710009,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.linga == Linga.STRI) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isAEnding = stem.varnas.lastOrNull() in setOf(Svara.A, Svara.AA)

        return isAEnding && affix.matchesUpadesha(SupAffix.BHIS.upadesha) &&
                affix.varnas == SupAffix.BHIS.initialVarnas &&
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceWholeAffix(
                affix.id,
                listOf(Svara.AI, Vyanjana.SA),
                sutra,
                dev.panini.derivation.WholeAffixDesignationPolicy.Consume,
            ).addVarnaSubstitution(
                affix.id,
                Vyanjana.BHA,
                listOf(Svara.AI, Vyanjana.SA),
                sutra,
            ),
            explanation = "7.1.9 substitutes ऐस् for instrumental-plural भिस्."
        )
    }
}
