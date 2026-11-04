package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.*

/** 2.3.60 द्वितीया ब्राह्मणे. */
object DvitiyaBrahmaneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.60", text = "द्वितीया ब्राह्मणे",
    hindiExplanation = "ब्राह्मण-पाठ के प्रयोग में दिव् के तदर्थ कर्म में द्वितीया होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230060,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"),
), CaseDependentKarakaOutcome {
    override fun karakaForCase(vibhakti: Vibhakti): Karaka = Karaka.KARMAN
    fun hasBrahmanaDivObject(context: VibhaktiRuleContext): Boolean =
        SemanticRelation.BRAHMANA_DIV_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            DivasTadarthasyaSutra.hasSeshaDivObject(context)

    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN) &&
        hasBrahmanaDivObject(context) &&
        Vibhakti.DVITIYA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.DVITIYA, KarakaEvidence(number, text, "Dvitīyā realizes the div transaction object in Brāhmaṇa usage (2.3.60)."),
    )
}
