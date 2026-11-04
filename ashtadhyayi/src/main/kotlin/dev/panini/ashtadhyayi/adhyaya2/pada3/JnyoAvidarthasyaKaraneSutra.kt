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
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.51 ज्ञोऽविदर्थस्य करणे.
 * Assigns Ṣaṣṭhī when the underlying instrument of non-knowledge jñā is construed as śeṣa.
 */
object JnyoAvidarthasyaKaraneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.51", text = "ज्ञोऽविदर्थस्य करणे",
    hindiExplanation = "ज्ञान अर्थ से भिन्न अर्थ वाली ज्ञ धातु के करण में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230051,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    fun hasSeshaInstrumentConstruction(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return KarakaDhatuDomains.isJna(context) &&
            SemanticRelation.JNA_NON_KNOWLEDGE_SENSE in relations &&
            SemanticRelation.INSTRUMENT in relations && SemanticRelation.SESHA_RELATION in relations
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.KARANA, Karaka.ANIRDHARITA) &&
            hasSeshaInstrumentConstruction(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी expresses as śeṣa the underlying instrument of non-knowledge jñā (2.3.51)."),
    )
}
