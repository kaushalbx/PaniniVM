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
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.47 सम्बोधने च.
 * Assigns Prathamā in address / vocative (sambodhana).
 */
object SambodhaneCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.47", text = "सम्बोधने च",
    hindiExplanation = "सम्बोधन अर्थ में प्रथमा विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230047,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA),
    adhikara = emptySet(),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        context.karaka == Karaka.SAMBODHANA && Vibhakti.PRATHAMA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PRATHAMA,
        KarakaEvidence(number, text, "प्रथमा realizes vocative address / sambodhana (2.3.47)."),
    )
}
