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
 * Sūtra 3.1.55 पुषादिद्युताद्य्लृदिभ्यः परस्मैपदेषु.
 * Prescribes aṅ vikaraṇa in Luṅ Parasmaipada for puṣ-ādi, dyut-ādi, and ḷt-it roots.
 */
object PusAdiDyutAdyLdtahParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.55", text = "पुषादिद्युताद्य्लृदिभ्यः परस्मैपदेषु",
    hindiExplanation = "परस्मैपद में लुङ् लकार रहने पर पुषादि, द्युतादि तथा ॠदित् धातुओं से 'अङ्' (अ) विकरण होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310055,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
        context.allEffectiveTerms.any { it.upadesha in setOf("पुष्", "पुषँ", "द्युत्", "द्युतँ", "लिख्", "लिखँ") || it.surface in setOf("पुष्", "द्युत्", "लिख्") } &&
        context.allEffectiveTerms.none { it.upadesha in setOf("अङ्", "सिच्", "चङ्", "चिण्") }

    override fun apply(context: DerivationState): DerivationChange {
        val ang = DerivationTerm("ang", "अङ्", TermKind.PRATYAYA, upadesha = "अङ्", createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(ang),
            explanation = "3.1.55 prescribes अङ् vikaraṇa in Luṅ Parasmaipada.",
        )
    }
}
