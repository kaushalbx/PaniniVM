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
 * Sūtra 2.3.54 रुजार्थानां भाववचनानामज्वरेः.
 * Assigns Ṣaṣṭhī for the object of verbs denoting disease/pain (except jvara).
 */
object RujarthanamBhavavacananamAjvarehSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.54", text = "रुजार्थानां भाववचनानामज्वरेः",
    hindiExplanation = "भावकर्तृक रुजार्थ धातु के वस्तुतः कर्म की शेषत्वविवक्षा में षष्ठी होती है; ज्वर् तथा सन्तापि वर्जित हैं।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230054,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.50", "2.3.52"),
) {
    fun hasSeshaBhavaAgentObject(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        val relations = participant.semanticRelations
        return KarakaDhatuDomains.isDiseasePainVerb(context) &&
            setOf(SemanticRelation.DISEASE_PAIN_OBJECT, SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.SESHA_RELATION).all { it in relations } &&
            context.allParticipants.any { it.id != participant.id &&
                SemanticRelation.INDEPENDENT_AGENT in it.semanticRelations && SemanticRelation.BHAVA_EVENT in it.semanticRelations }
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
            hasSeshaBhavaAgentObject(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी expresses as śeṣa the underlying object of an eligible affliction verb " +
            "with a distinct bhāva agent (2.3.54)."),
    )
}
