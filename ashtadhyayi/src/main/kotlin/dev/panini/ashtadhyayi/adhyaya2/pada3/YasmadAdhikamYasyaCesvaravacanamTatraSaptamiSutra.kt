package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.3.9 यस्मादधिकं यस्य चेश्वरवचनं तत्र सप्तमी. */
object YasmadAdhikamYasyaCesvaravacanamTatraSaptamiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.9",
    text = "यस्मादधिकं यस्य चेश्वरवचनं तत्र सप्तमी",
    hindiExplanation = "जिसकी अपेक्षा अधिकता अथवा स्वामित्व कहा जाए, उसमें सप्तमी होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230009,
    anuvrtti = setOf("2.3.8"),
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            hasReferenceConstruction(context) &&
            Vibhakti.SAPTAMI in context.morphologicalCandidates

    fun hasReferenceConstruction(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        return SemanticRelation.KARMAPRAVACANIYA_GOVERNANCE in relations &&
            ((SemanticRelation.SUPERIORITY_REFERENCE in relations &&
                SemanticRelation.UPA_SUPERIORITY_KARMAPRAVACANIYA_LINK in relations) ||
                (SemanticRelation.SOVEREIGNTY_REFERENCE in relations &&
                    SemanticRelation.ADHI_SOVEREIGNTY_KARMAPRAVACANIYA_LINK in relations))
    }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SAPTAMI,
        KarakaEvidence(number, text, "Saptamī marks the reference for superiority or lordship (2.3.9)."),
    )
}
