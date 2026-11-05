package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
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
 * Sūtra 2.3.48 सामन्त्रितम्.
 * Assigns āmantrita saṃjñā to Prathamā used in address / vocative (sambodhana).
 */
object SamantriteSutra : Sutra<VibhaktiRuleContext, NominalDesignationResult>(
    number = "2.3.48", text = "सामन्त्रितम्",
    hindiExplanation = "सम्बोधन अर्थ में प्रयुक्त प्रथमा विभक्ति की आमन्त्रित संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 2, pada = 3, optional = false, kramaValue = 230048,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA),
    adhikara = emptySet(), anuvrtti = setOf("2.3.46", "2.3.47"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        context.karaka == Karaka.SAMBODHANA && Vibhakti.PRATHAMA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = NominalDesignationResult(
        NominalDesignation.AMANTRITA,
        KarakaEvidence(number, text, "प्रथमा in vocative is designated as āmantrita (2.3.48)."),
    )
}
