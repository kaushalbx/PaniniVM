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
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.analysis.CaseDependentKarakaOutcome

/**
 * Sūtra 2.3.59 विभाषोपसर्गे.
 * Option for Ṣaṣṭhī (or Dvitīyā) with prefixed div in the inherited transaction sense.
 */
object VibhasopasargeSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.59", text = "विभाषोपसर्गे",
    hindiExplanation = "उपसर्गयुक्त दिव् के तदर्थ कर्म में षष्ठी अथवा द्वितीया का विकल्प होता है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230059,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.50", "2.3.52", "2.3.58"),
), OptionalVibhaktiRuleOutcome, CaseDependentKarakaOutcome {
    override val permittedCases = setOf(Vibhakti.SASTHI, Vibhakti.DVITIYA)
    override fun karakaForCase(vibhakti: Vibhakti): Karaka =
        if (vibhakti == Vibhakti.SASTHI) Karaka.SAMBANDHA else Karaka.KARMAN

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka in setOf(Karaka.ANIRDHARITA, Karaka.KARMAN, Karaka.SAMBANDHA) &&
            KarakaDhatuDomains.isPrefixedDiv(context) &&
            DivasTadarthasyaSutra.hasSeshaDivObject(context) &&
            !DvitiyaBrahmaneSutra.hasBrahmanaDivObject(context) &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes the object of prefixed div in the inherited transaction sense (2.3.59)."),
        )
    }
}
