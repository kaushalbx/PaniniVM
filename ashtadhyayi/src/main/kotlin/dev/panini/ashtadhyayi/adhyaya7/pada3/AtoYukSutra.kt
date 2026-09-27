package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.114: āto yuk.
 * The y-augment is introduced between an ā-final aṅga and the dual ओस्
 * ending; the ordinary s-final resolution gives योः.
 */
object AtoYukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.114",
    text = "आतो युक्",
    hindiExplanation = "आकारान्त अङ्ग के बाद ओस्-प्रत्यय में युक् होकर योः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730114,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage in setOf(DerivationStage.INITIAL, DerivationStage.PRATYAYA_SELECTED)) return false
        if (context.terms.size == 1) {
            val term = context.terms.single()
            return context.effectiveContext.rupa.linga == Linga.STRI &&
                term.upadeshaVarnas.lastOrNull() == Svara.AA &&
                term.varnas.takeLast(2) == listOf(Svara.O, Vyanjana.SA)
        }
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return stem.varnas.lastOrNull() == Svara.AA &&
            affix.upadesha == "ओस्" &&
            affix.varnas != listOf(Vyanjana.YA, Svara.O, Ayogavaha.VISARGA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (context.terms.size == 1) {
            val term = context.terms.single()
            return DerivationChange(
                state = context.substituteTermSurface(
                    term.id,
                    (term.varnas.dropLast(2) + listOf(Vyanjana.YA, Svara.O, Ayogavaha.VISARGA)).toDevanagari(),
                    Svara.O,
                    listOf(Vyanjana.YA, Svara.O),
                    sutra,
                )
                    .copy(stage = DerivationStage.PADA_FORMED),
                explanation = "7.3.114: Introduced युक् into the merged ā-final dual ओस् form, yielding योः.",
            )
        }
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context
                .substituteTermSurface(
                    stem.id,
                    (stem.varnas.dropLast(1) + Svara.A).toDevanagari(),
                    Svara.AA,
                    listOf(Svara.A),
                    sutra,
                )
                .replaceWholeAffix(
                    affix.id,
                    listOf(Vyanjana.YA, Svara.O, Ayogavaha.VISARGA),
                    sutra,
                    dev.panini.derivation.WholeAffixDesignationPolicy.Consume,
                )
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.114: Replaced final ā plus dual ओस् with युक् + ओस्, yielding योः.",
        )
    }
}
