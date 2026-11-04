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
 * 2.3.52: adhīgartha-dayeśāṁ karmaṇi.
 * Assigns genitive (Ṣaṣṭhī) to the object of remembering (adhī), pitying (day), or ruling (īś).
 */
object AdhigarthaDayesamKarmaniSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.52",
    text = "अधीगर्थदयेशां कर्मणि",
    hindiExplanation = "स्मरणार्थक, दय् और ईश् धातुओं के वस्तुतः कर्म की शेषत्वविवक्षा में षष्ठी होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230052,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    fun hasSeshaObjectConstruction(context: VibhaktiRuleContext): Boolean {
        val relations = context.participant?.semanticRelations.orEmpty()
        val dhatu = KarakaDhatuDomains.governingRoot(context) ?: return false
        val senseAndRoot =
            (SemanticRelation.REMEMBERING_SENSE in relations && KarakaDhatuDomains.isRemembering(context)) ||
            (SemanticRelation.DAY_DANA_GATI_RAKSHANA_SENSE in relations && KarakaDhatuDomains.isDay(dhatu)) ||
            (SemanticRelation.ISH_AISHVARYA_SENSE in relations && KarakaDhatuDomains.isIsh(dhatu))
        return senseAndRoot && SemanticRelation.DESIRED_OBJECT in relations && SemanticRelation.SESHA_RELATION in relations
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita && context.karaka in setOf(Karaka.KARMAN, Karaka.ANIRDHARITA) &&
            Vibhakti.SASTHI in context.morphologicalCandidates && hasSeshaObjectConstruction(context)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult =
        VibhaktiRuleResult.Assigned(
            Vibhakti.SASTHI,
            KarakaEvidence(number, text, "Assigns Ṣaṣṭhī for object of remembering/pitying/ruling verbs.")
        )
}
