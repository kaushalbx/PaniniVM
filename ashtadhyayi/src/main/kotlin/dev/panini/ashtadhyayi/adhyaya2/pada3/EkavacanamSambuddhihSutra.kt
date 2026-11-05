package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.NominalDesignation
import dev.panini.analysis.NominalDesignationResult

/**
 * Sūtra 2.3.49 एकवचनं सम्बुद्धिः.
 * Assigns sambuddhi saṃjñā to singular Prathamā used in address / vocative (sambodhana).
 */
object EkavacanamSambuddhihSutra : Sutra<VibhaktiRuleContext, NominalDesignationResult>(
    number = "2.3.49", text = "एकवचनं सम्बुद्धिः",
    hindiExplanation = "आमन्त्रित (सम्बोधन प्रथमा) के एकवचन रूप की सम्बुद्धि संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 2, pada = 3, optional = false, kramaValue = 230049,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA),
    adhikara = emptySet(), anuvrtti = setOf("2.3.46", "2.3.47"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        context.karaka == Karaka.SAMBODHANA &&
            context.participant?.vacana == Vacana.EKAVACANA &&
            Vibhakti.PRATHAMA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = NominalDesignationResult(
        NominalDesignation.SAMBUDDHI,
        KarakaEvidence(number, text, "Singular vocative is designated as sambuddhi (2.3.49)."),
    )
}
