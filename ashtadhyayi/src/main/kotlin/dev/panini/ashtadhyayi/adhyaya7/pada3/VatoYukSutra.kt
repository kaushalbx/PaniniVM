package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.NonOperativeUpadeshaFunction
import dev.panini.derivation.NonOperativeUpadeshaSegment
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.33: वातो युक्. Adds yuk-āgama (य्) to vā/pā/ma. */
object VatoYukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.33", text = "वातो युक्",
    hindiExplanation = "वा, पा, मा धातु से उण् प्रत्यय परे होने पर युक् (य्) आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false, kramaValue = 730033,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return (root.surface == "वा" || root.surface == "पा" || root.surface == "म") &&
            suffix.surface == "उ" &&
            context.allEffectiveTerms.none { it.id == "yuk-agama" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val root = context.terms[rootIndex]
        val yukAgama = DerivationTerm(
            "yuk-agama", "य्क्", TermKind.AGAMA,
            upadesha = "युक्", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            nonOperativeUpadeshaSegments = listOf(
                NonOperativeUpadeshaSegment(1, 2, "ु", NonOperativeUpadeshaFunction.UCCARANARTHA),
            ),
            augmentTargetId = root.id, mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            context.copy(
                terms = context.terms.take(rootIndex + 1) + yukAgama + context.terms.drop(rootIndex + 1)
            ),
            "7.3.33 introduces raw युक् targeted at the end of the root."
        )
    }
}
