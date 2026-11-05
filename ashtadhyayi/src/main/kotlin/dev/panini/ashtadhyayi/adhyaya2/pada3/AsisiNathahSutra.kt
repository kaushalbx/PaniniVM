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
 * Sūtra 2.3.55 आशिषि नाथः.
 * Assigns Ṣaṣṭhī for the object of nāth (to bless/hope).
 */
object AsisiNathahSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.55", text = "आशिषि नाथः",
    hindiExplanation = "इदं मे भूयात् रूप आशासन के अर्थ में नाथ् के वस्तुतः कर्म की शेषत्वविवक्षा में षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230055,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.50", "2.3.52"),
) {
    fun hasSeshaWishObject(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return KarakaDhatuDomains.isNath(context) &&
            setOf(SemanticRelation.BLESSING_HOPE_OBJECT, SemanticRelation.DESIRED_OBJECT,
                SemanticRelation.SESHA_RELATION).all { it in relations }
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
            hasSeshaWishObject(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी expresses as śeṣa the underlying object of nāth/nādh in the wish 'may I have this' sense (2.3.55)."),
    )
}
