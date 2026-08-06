package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 8.2.7: Delete a final n of a prātipadika before the bhy- and sup endings. */
object NaloPratipadikantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.7",
    text = "नलोपः प्रातिपदिकान्तस्य",
    hindiExplanation = "भ्य- और सुप् प्रत्यय परे होने पर प्रातिपदिकान्त नकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820007,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PADA_FORMATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val isSamasa = context.samjnas.any { it.samjna == Samjna.SAMASA }
        if (isSamasa) {
            val nonFinalTerms = context.terms.dropLast(1)
            return nonFinalTerms.any { t ->
                t.kind == TermKind.PRATIPADIKA && isNEndingStem(t.surface)
            }
        }
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val insideSankhyaCompound = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SANKHYA }
        return stem.kind == TermKind.PRATIPADIKA && isNEndingStem(stem.surface) &&
            (affix.upadesha in setOf("भ्याम्", "भिस्", "भ्यस्", "सुप्", "मट्") || insideSankhyaCompound)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isSamasa = context.samjnas.any { it.samjna == Samjna.SAMASA }
        if (isSamasa) {
            var updatedState = context
            val nonFinalTerms = context.terms.dropLast(1)
            for (t in nonFinalTerms) {
                if (t.kind == TermKind.PRATIPADIKA && isNEndingStem(t.surface)) {
                    val newSurface = performNaloPa(t.surface)
                    updatedState = updatedState.replaceTerm(t.id, t.copy(surface = newSurface))
                }
            }
            return DerivationChange(
                state = updatedState,
                explanation = "8.2.7: Deleted final न् of compound pūrvapada.",
            )
        }
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = performNaloPa(stem.surface))),
            explanation = "8.2.7: Deleted final न् of the prātipadika before the bhy-/sup ending.",
        )
    }

    private fun isNEndingStem(s: String): Boolean =
        s.endsWith("न्") || (s.endsWith("n") && s.length > 1)

    private fun performNaloPa(s: String): String = when {
        s.endsWith("न्") -> s.dropLast(2)
        s.endsWith("n") && s.length > 1 -> s.dropLast(1)
        else -> s
    }
}
