package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.48 निश्रीद्रुस्रुभ्यः कर्तरि च.
 * Prescribes caṅ vikaraṇa in Luṅ after ni, śri, dru, sru in active voice.
 */
object NishriDruSruBhyahKarthariChaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.48", text = "निश्रीद्रुस्रुभ्यः कर्तरि च",
    hindiExplanation = "कर्तरि लुङ् लकार में नि-पूर्वक धातु, श्री, द्रु तथा स्रु धातुओं से 'चङ्' (अ) प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310048,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
        context.allEffectiveTerms.any { it.upadesha in setOf("श्रीञ्", "द्रु", "स्रु", "निश्री", "निश्रीञ्") || it.surface in setOf("श्री", "द्रु", "स्रु", "निश्री") } &&
        context.allEffectiveTerms.none { it.upadesha in setOf("चङ्", "सिँच्", "अङ्", "चिण्") }

    override fun apply(context: DerivationState): DerivationChange {
        val cang = DerivationTerm("cang", "चङ्", TermKind.PRATYAYA, upadesha = "चङ्", createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(cang),
            explanation = "3.1.48 prescribes चङ् vikaraṇa in Luṅ.",
        )
    }
}
