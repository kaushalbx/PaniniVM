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
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.analysis.DirectionalAffixGovernance
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.core.TaddhitaAffix

/**
 * Sūtra 2.3.31 एनपा द्वितीया.
 * Assigns Dvitīyā in connection with words ending with the -enaP suffix (e.g. dakṣiṇena).
 */
object EnapaDvitiyaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.31", text = "एनपा द्वितीया",
    hindiExplanation = "एनप्-प्रत्ययान्त शब्द के योग में द्वितीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230031,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.DVITIYA, Vibhakti.SASTHI)
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            DirectionalAffixGovernance.affix(context) == TaddhitaAffix.ENAP &&
            context.morphologicalCandidates.any { it in permittedCases }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        if (Vibhakti.DVITIYA in context.morphologicalCandidates) Vibhakti.DVITIYA else Vibhakti.SASTHI,
        KarakaEvidence(number, text, "द्वितीया/षष्ठी under the linked enaP governor ${context.participant?.governingAvyaya?.form}; " +
            "derivation: ${context.participant?.governingAvyaya?.derivation} (2.3.31)."),
    )
}
