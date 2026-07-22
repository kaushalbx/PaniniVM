package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object SasajusoRuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.66",
    text = "ससजुषो रुः",
    hindiExplanation = "स् के स्थान पर रुँ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820066,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        (context.stage == DerivationStage.IT_PROCESSED || context.stage == DerivationStage.PADA_FORMED || context.stage == DerivationStage.FINAL) &&
        context.terms.last().surface.endsWith(Vyanjana.SA.halanta) || internalSankhyaIndex(context) >= 0

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        internalSankhyaIndex(context).takeIf { it >= 0 }?.let { index ->
            val target = context.terms[index]
            context.copy(terms = context.terms.toMutableList().also {
                it[index] = target.copy(surface = target.surface.dropLast(2) + Vyanjana.RA.devanagari)
            })
        } ?: context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .copy(surface = context.terms.last().surface.dropLast(2) + Vyanjana.RA.devanagari)
        ), "8.2.66 replaces स् with रुँ."
    )

    private fun internalSankhyaIndex(context: DerivationState): Int = context.terms.indices.firstOrNull { index ->
        index < context.terms.lastIndex && context.terms[index].surface.endsWith(Vyanjana.SA.halanta) &&
            context.samjnas.any { it.targetId == context.terms[index].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == context.terms[index + 1].id && it.samjna == Samjna.SANKHYA }
    } ?: -1
}
