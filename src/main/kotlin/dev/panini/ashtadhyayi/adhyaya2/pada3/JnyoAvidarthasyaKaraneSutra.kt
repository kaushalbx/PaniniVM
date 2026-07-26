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
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.51 ज्ञोऽविदर्थस्य करणे.
 * Assigns Ṣaṣṭhī for the karaṇa of the dhātu jñā when used in a sense other than knowledge (e.g. memory/ruling).
 */
object JnyoAvidarthasyaKaraneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.51", text = "ज्ञोऽविदर्थस्य करणे",
    hindiExplanation = "ज्ञान अर्थ से भिन्न अर्थ वाली ज्ञ धातु के करण में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230051,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARANA &&
            SemanticRelation.MEMORY_OR_RULING_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes karaṇa for jñā dhātu in memory/ruling sense (2.3.51)."),
    )
}
