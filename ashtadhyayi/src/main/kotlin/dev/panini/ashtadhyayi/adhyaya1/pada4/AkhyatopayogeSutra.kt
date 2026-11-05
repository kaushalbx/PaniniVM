package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.hasEstablishedMeaning

object AkhyatopayogeSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.29", text = "आख्यातोपयोगे",
    hindiExplanation = "नियमपूर्वक विद्या स्वीकार करने में वक्ता की अपादानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140029,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.24"),
) {
    override fun matches(context: KarakaRuleContext): Boolean {
        // Upayoga is a construction meaning, not a closed list of roots.
        // Kāśikā includes both adhīte and āgamayati.
        return context.participant.hasEstablishedMeaning(SemanticRelation.FORMAL_INSTRUCTION_SOURCE)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.APADANA,
        KarakaEvidence(
            number,
            text,
            "The participant is the teacher from whom regular instruction is accepted (akhyata)."
        ),
    )
}
