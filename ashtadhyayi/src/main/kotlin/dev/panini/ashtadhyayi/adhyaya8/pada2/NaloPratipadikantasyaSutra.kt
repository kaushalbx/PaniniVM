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
        if (context.terms.isEmpty()) return false
        val stem = context.terms.first()
        val affix = context.terms.getOrNull(1)

        val insideSankhyaCompound = affix != null && context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SANKHYA }

        val hasDroppedSup = context.droppedTerms.any { it.id.startsWith("sup-") }

        return stem.kind == TermKind.PRATIPADIKA && stem.surface.endsWith("न्") &&
            (affix == null || affix.upadesha in setOf("भ्याम्", "भिस्", "भ्यस्", "सुप्", "मट्", "सु", "नाम्") || insideSankhyaCompound || hasDroppedSup)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first()
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = stem.surface.dropLast(2))),
            explanation = "8.2.7: Deleted final न् of the prātipadika.",
        )
    }
}
