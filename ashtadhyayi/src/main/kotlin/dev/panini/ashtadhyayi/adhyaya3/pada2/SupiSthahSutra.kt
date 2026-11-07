package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.78 सुपि स्थः.
 * Prescribes ka (क) Kṛt affix after root sthā (स्था) when preceded by a subanta upapada.
 */
object SupiSthahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.4", text = "सुपि स्थः",
    hindiExplanation = "सुबन्त उपपद रहते 'स्था' धातु से 'क' (अ) प्रत्यय होता है (उदा. गृहस्थः, नगस्थः)।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320004,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.any { it.kind == TermKind.PRATYAYA && !dev.panini.derivation.SanadiAffixes.contains(it.upadesha) }) return false
        if (context.samjnas.none { it.samjna == dev.panini.shiksha.Samjna.KA }) return false
        val rootTerm = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        // The upapada is a word before the root; an upasarga is not one.
        val hasUpapada = context.terms.takeWhile { it !== rootTerm }.any { term -> term.kind == TermKind.PRATIPADIKA &&
            context.samjnas.none { it.targetId == term.id && it.samjna == dev.panini.shiksha.Samjna.UPASARGA } }
        return (rootTerm.upadesha == "स्था" || rootTerm.surface == "स्था") && hasUpapada
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kaTerm = DerivationTerm("ka", "क", TermKind.PRATYAYA, upadesha = "क", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(kaTerm),
            explanation = "3.2.4 prescribes क Kṛt affix after sthā root with subanta upapada.",
        )
    }
}
