package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.sutra.*

/** 2.3.63 यजेश्च करणे. */
object YajesCaKaraneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.63", text = "यजेश्च करणे",
    hindiExplanation = "छन्दस् में यज् के करण में बहुलतः षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230063,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE), adhikara = setOf("2.3.1"), anuvrtti = setOf("2.3.50", "2.3.62"),
), AttestedVibhaktiRuleOutcome, CaseDependentKarakaOutcome {
    override val attestedCase = Vibhakti.SASTHI
    override fun karakaForCase(vibhakti: Vibhakti): Karaka = Karaka.KARANA
    fun hasLicensedUse(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        return context.register.isVedic && participant.vedicCaseLicense?.sutra == number &&
            KarakaDhatuDomains.isYaj(context) && SemanticRelation.INSTRUMENT in participant.semanticRelations &&
            SemanticRelation.VEDIC_YAJ_INSTRUMENT in participant.semanticRelations
    }

    override fun matches(context: VibhaktiRuleContext) = !context.abhihita &&
        context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARANA) && hasLicensedUse(context) &&
        Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI, KarakaEvidence(number, text, "Licensed chandas genitive instrument of yaj: ${context.participant?.vedicCaseLicense?.reference} (2.3.63)."),
    )
}
