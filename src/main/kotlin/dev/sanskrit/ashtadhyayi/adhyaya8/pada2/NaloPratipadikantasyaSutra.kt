package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return stem.kind == TermKind.PRATIPADIKA && stem.surface.endsWith("न्") &&
            affix.upadesha in setOf("भ्याम्", "भिस्", "भ्यस्", "सुप्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = stem.surface.dropLast(2)))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "8.2.7: Deleted final न् of the prātipadika before the bhy-/sup ending.",
        )
    }
}
