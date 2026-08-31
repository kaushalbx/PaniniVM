package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
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
                term.surface.endsWith("ओस्")
        }
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return Varnamala.endsWithAA(stem.surface) &&
            affix.upadesha == "ओस्" &&
            affix.surface != "योः"
    }

    override fun apply(context: DerivationState): DerivationChange {
        if (context.terms.size == 1) {
            val term = context.terms.single()
            return DerivationChange(
                state = context.replaceTerm(term.id, term.copy(surface = term.surface.removeSuffix("ओस्") + "योः"))
                    .copy(stage = DerivationStage.PADA_FORMED),
                explanation = "7.3.114: Introduced युक् into the merged ā-final dual ओस् form, yielding योः.",
            )
        }
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context
                .replaceTerm(stem.id, stem.copy(surface = stem.surface.dropLast(1)))
                .replaceWholeAffix(affix.id, "योः", sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.114: Replaced final ā plus dual ओस् with युक् + ओस्, yielding योः.",
        )
    }
}
