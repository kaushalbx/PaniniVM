package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.*

/** 2.3.64 कृत्वोऽर्थप्रयोगे कालेऽधिकरणे. */
object KrtvorthaPrayogeKaleAdhikaraneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.64", text = "कृत्वोऽर्थप्रयोगे कालेऽधिकरणे",
    hindiExplanation = "स्पष्ट क्रियागणना के प्रयोग में वस्तुतः कालाधिकरण की शेषत्वविवक्षा में षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230064,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
) {
    fun hasSeshaTimeFrequency(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        val relations = participant.semanticRelations
        return setOf(SemanticRelation.FREQUENCY_TIME_LOCUS, SemanticRelation.TIME_REFERENCE,
            SemanticRelation.LOCATION, SemanticRelation.SESHA_RELATION).all { it in relations } &&
            participant.frequencyGovernor?.let(CountingFrequencyExpressions::count) != null
    }

    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.ADHIKARANA) &&
        hasSeshaTimeFrequency(context) &&
        Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI, KarakaEvidence(number, text, "Ṣaṣṭhī expresses as śeṣa the temporal locus linked to explicit " +
            "${context.participant?.frequencyGovernor?.form} (2.3.64)."),
    )
}
