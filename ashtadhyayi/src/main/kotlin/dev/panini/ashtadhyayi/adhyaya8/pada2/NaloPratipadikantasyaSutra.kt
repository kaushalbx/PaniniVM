package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
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
        return findTarget(context) != null
    }

    private fun findTarget(context: DerivationState): DerivationTerm? {
        if (context.terms.isEmpty()) return null
        return context.terms.withIndex().firstNotNullOfOrNull { (index, stem) ->
            val affix = context.terms.getOrNull(index + 1)

            val insideSankhyaCompound = affix != null && context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SANKHYA } &&
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SANKHYA }
            val insideSamasa = affix != null && context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SAMASA } &&
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SAMASA }

            val hasDroppedSup = context.droppedTerms.any { it.id.startsWith("sup-") }

            // A compound's non-final pada may acquire n through a substitute such as ānaṅ.
            // Do not extend this early compound operation to a newly n-final compound head:
            // that member still has its own inflectional lifecycle (e.g. vidvas -> vidvān).
            val isPratipadikaNanta = stem.upadeshaVarnas.lastOrNull() == Vyanjana.NA || insideSamasa

            stem.takeIf {
                isPratipadikaNanta && stem.kind == TermKind.PRATIPADIKA && stem.varnas.lastOrNull() == Vyanjana.NA &&
                    (affix == null || affix.upadesha in setOf("भ्याम्", "भिस्", "भ्यस्", "सुप्", "मट्", "सु", "नाम्") ||
                        insideSankhyaCompound || insideSamasa || hasDroppedSup)
            }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(findTarget(context))
        return DerivationChange(
            state = context.substituteTermVarnas(stem.id, stem.varnas.dropLast(1), Vyanjana.NA, emptyList(), sutra),
            explanation = "8.2.7: Deleted final न् of the prātipadika.",
        )
    }
}
