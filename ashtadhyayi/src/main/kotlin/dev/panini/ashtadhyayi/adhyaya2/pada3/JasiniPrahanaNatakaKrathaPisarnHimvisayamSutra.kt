package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.core.Karaka
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.56 जासिनिप्रहणनाटक्राथपिषां हिंसायाम्.
 * Specific genitive for the relationally construed object of the stated injury constructions.
 */
object JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.56", text = "जासिनिप्रहणनाटक्राथपिषां हिंसायाम्",
    hindiExplanation = "निर्दिष्ट जासि, निप्रहण, नाट, क्राथ और पिष् की हिंसाक्रिया के वस्तुतः कर्म की शेषत्वविवक्षा में षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230056,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    fun hasSeshaInjuryObject(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return KarakaDhatuDomains.isSpecifiedInjuryVerb(context) &&
            setOf(SemanticRelation.INJURY_VIOLENCE_OBJECT, SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.SESHA_RELATION).all { it in relations }
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
            hasSeshaInjuryObject(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes object of injury/violence verbs (2.3.56)."),
    )
}
