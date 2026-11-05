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
 * Sūtra 2.3.58 दिवस्तदर्थस्य.
 * Assigns Ṣaṣṭhī for the karman of div when used in the sense of gambling/transaction.
 */
object DivasTadarthasyaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.58", text = "दिवस्तदर्थस्य",
    hindiExplanation = "द्यूत/व्यवहार अर्थ वाली दिव् धातु के कर्म में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230058,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.50", "2.3.52", "2.3.57"),
) {
    fun hasSeshaDivObject(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return KarakaDhatuDomains.isDivTransactionRoot(context) &&
            setOf(SemanticRelation.TRANSACTION_GAMBLING_OBJECT, SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.SESHA_RELATION).all { it in relations }
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
            hasSeshaDivObject(context) && !KarakaDhatuDomains.isPrefixedDiv(context) &&
            !DvitiyaBrahmaneSutra.hasBrahmanaDivObject(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes karman of div in gambling sense (2.3.58)."),
    )
}
