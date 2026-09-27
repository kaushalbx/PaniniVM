package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Feminine Ghi stem before ṅi: form the yām locative singular ending. */
object GherNgiStriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.129",
    text = "घेर्ङि स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग घि-अन्त के बाद एकवचन ङि में याम्/वाम् रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730129,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.STRI ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.SAPTAMI ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha == "ङि" && stem.varnas.lastOrNull() in setOf(Svara.I, Svara.U)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val source = stem.varnas.last() as Svara
        val ending: List<Varna> = when (source) {
            Svara.I -> listOf(Vyanjana.YA, Svara.AA, Vyanjana.MA)
            Svara.U -> listOf(Vyanjana.VA, Svara.AA, Vyanjana.MA)
            else -> error("GherNgiStriyamSutra matched a non-ik stem")
        }
        val surface = (stem.varnas.dropLast(1) + ending).toDevanagari()
        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, surface, source, ending, sutra,
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.129: Formed the feminine Ghi locative-singular याम् ending before ङि.",
        )
    }
}
