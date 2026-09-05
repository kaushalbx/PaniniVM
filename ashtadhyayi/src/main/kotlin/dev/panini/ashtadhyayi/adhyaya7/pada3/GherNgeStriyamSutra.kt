package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Feminine Ghi stem before ṅe: form the yai dative singular ending. */
object GherNgeStriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.127",
    text = "घेर्ङे स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग घि-अन्त के बाद एकवचन ङे में यै/वै रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730127,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.STRI ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.CHATURTHI ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha == "ङे" && stem.surface.lastOrNull() in setOf('इ', 'ि', 'उ', 'ु')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val ending = when (stem.surface.last()) {
            'इ' -> "यै"
            'ि' -> "्यै"
            'उ' -> "वै"
            'ु' -> "्वै"
            else -> error("GherNgeStriyamSutra matched a non-ik stem")
        }
        val base = stem.surface.dropLast(1) + ending
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = base),
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.127: Formed the feminine Ghi dative-singular यै ending before ङे.",
        )
    }
}
