package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.*

/** 2.3.61 प्रेष्यब्रुवोर्हविषो देवतासम्प्रदाने. */
object PresyabruvorHavisoDevatasampradaneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.61", text = "प्रेष्यब्रुवोर्हविषो देवतासम्प्रदाने",
    hindiExplanation = "देवता सम्प्रदान होने पर प्रेष्य/ब्रूहि के हविः-कर्म में षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230061,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
) {
    /** Positive domain only; prasthita prohibition is applied separately and remains explainable. */
    fun hasRitualOblationObject(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        val relations = participant.semanticRelations
        return KarakaDhatuDomains.isPresyaOrBruhi(context) &&
            setOf(SemanticRelation.DEITY_OBLATION_OBJECT, SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.SESHA_RELATION).all { it in relations } &&
            context.allParticipants.any { it.id != participant.id && SemanticRelation.DEITY_REFERENT in it.semanticRelations &&
                SemanticRelation.RECIPIENT in it.semanticRelations }
    }

    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
        hasRitualOblationObject(context) &&
        Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI, KarakaEvidence(number, text, "Ṣaṣṭhī realizes the oblation object with preṣya/brūhi and a deity recipient (2.3.61)."),
    )
}
