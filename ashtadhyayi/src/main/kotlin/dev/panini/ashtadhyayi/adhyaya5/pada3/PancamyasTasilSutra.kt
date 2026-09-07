package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.NonOperativeUpadeshaFunction
import dev.panini.derivation.NonOperativeUpadeshaSegment
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.3.7 पञ्चम्यास्तसिल्.
 * Prescribes tasil (तस्) Avyaya-Taddhita affix after 5th case nominal stems (e.g. yataḥ, tataḥ, kutaḥ, sarvataḥ).
 */
object PancamyasTasilSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.7", text = "पञ्चम्यास्तसिल्",
    hindiExplanation = "पञ्चम्यन्त समर्थ पद से 'तसिल्' (तस्) अव्ययतद्धित प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 5, pada = 3, optional = false, kramaValue = 530007,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.any { it.kind == TermKind.PRATIPADIKA } &&
        context.allEffectiveTerms.none { it.upadesha == "तसिल्" }

    override fun apply(context: DerivationState): DerivationChange {
        val tasilTerm = DerivationTerm(
            id = "tasil",
            // The written इ is uccāraṇārtha. It belongs to the recorded
            // upadeśa, not to the material on which grammatical operations act.
            surface = "तस्ल्",
            kind = TermKind.PRATYAYA,
            upadesha = "तसिल्",
            createdBySutra = number,
            nonOperativeUpadeshaSegments = listOf(
                NonOperativeUpadeshaSegment(2, 3, "ि", NonOperativeUpadeshaFunction.UCCARANARTHA),
            ),
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.addTerm(tasilTerm),
            explanation = "5.3.7 prescribes तसिल्; its written इ is uccāraṇārtha and the operative raw affix is तस्+ल्.",
        )
    }
}
