package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
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
    hindiExplanation = "पदान्त स् तथा सजुष् के अन्तिम ष् के स्थान पर रुँ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820066,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.RUTVA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val eligibleStage = context.stage == DerivationStage.IT_PROCESSED ||
            context.stage == DerivationStage.PADA_FORMED ||
            context.stage == DerivationStage.FINAL
        if (!eligibleStage) return false

        val finalSurface = context.terms.lastOrNull()?.surface ?: return false
        return finalSurface.endsWith(Vyanjana.SA.halanta) ||
            finalSurface.endsWith("सजुष्") ||
            internalSankhyaIndex(context) >= 0
    }

    override fun apply(context: DerivationState): DerivationChange {
        val internalIndex = internalSankhyaIndex(context)
        val target = context.terms[internalIndex.takeIf { it >= 0 } ?: context.terms.lastIndex]
        val source = if (target.surface.endsWith("सजुष्")) 'ष' else 'स'
        val changed = internalIndex.takeIf { it >= 0 }?.let { index ->
            val target = context.terms[index]
            context.copy(terms = context.terms.toMutableList().also {
                it[index] = target.copy(
                    surface = target.surface.dropLast(2) + "रुँ",
                    itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
                )
            })
        } ?: context.copy(
            terms = context.terms.dropLast(1) + context.terms.last()
                .copy(
                    surface = context.terms.last().surface.dropLast(2) + "रुँ",
                    itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
                )
        )
        return DerivationChange(
            changed.addSubstitution(VarnaSubstitution(target.id, source, "रुँ", number)),
            "8.2.66 substitutes रुँ for पद-final ${source}्.",
        )
    }

    private fun internalSankhyaIndex(context: DerivationState): Int = context.terms.indices.firstOrNull { index ->
        index < context.terms.lastIndex && context.terms[index].surface.endsWith(Vyanjana.SA.halanta) &&
            context.samjnas.any { it.targetId == context.terms[index].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == context.terms[index + 1].id && it.samjna == Samjna.SANKHYA }
    } ?: -1
}
