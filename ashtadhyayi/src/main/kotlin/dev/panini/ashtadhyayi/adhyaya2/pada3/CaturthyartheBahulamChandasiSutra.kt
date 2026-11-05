package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.sutra.*

/** 2.3.62 चतुर्थ्यर्थे बहुलं छन्दसि. */
object CaturthyartheBahulamChandasiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.62", text = "चतुर्थ्यर्थे बहुलं छन्दसि",
    hindiExplanation = "छन्दस् में चतुर्थी के अर्थ में बहुलतः षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230062,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"), anuvrtti = setOf("2.3.50"),
), AttestedVibhaktiRuleOutcome {
    override val attestedCase = Vibhakti.SASTHI
    fun hasLicensedUse(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        return context.register.isVedic && participant.vedicCaseLicense?.sutra == number &&
            SemanticRelation.VEDIC_DATIVE_MEANING in participant.semanticRelations
    }

    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        context.karaka != dev.panini.core.Karaka.SAMBODHANA &&
        hasLicensedUse(context) &&
        Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI, KarakaEvidence(number, text, "Licensed chandas genitive in dative meaning: ${context.participant?.vedicCaseLicense?.reference} (2.3.62)."),
    )
}
