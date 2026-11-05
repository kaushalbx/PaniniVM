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
import dev.panini.analysis.OptionalVibhaktiRuleOutcome
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.27 सर्वनाम्नस्तृतीया च.
 * Assigns Tṛtīyā (and Ṣaṣṭhī by 2.3.26) for pronouns in connection with the word hetu.
 */
object SarvanamnasTrtiyaCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.27", text = "सर्वनाम्नस्तृतीया च",
    hindiExplanation = "सर्वनाम के साथ हेतु शब्द के प्रयोग में तृतीया तथा षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230027,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.23", "2.3.26"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)
    fun candidateCases(context: VibhaktiRuleContext): Set<Vibhakti> =
        if (SasthiHetuprayogeSutra.hasConstruction(context) &&
            SemanticRelation.PRONOMINAL_HETU in context.participant?.semanticRelations.orEmpty()) permittedCases else emptySet()
    override fun matches(context: VibhaktiRuleContext): Boolean = dev.panini.analysis.VartikaCaseUses.nimitta(context) || (!context.abhihita &&
            SasthiHetuprayogeSutra.hasConstruction(context) &&
            SemanticRelation.PRONOMINAL_HETU in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.CAUSE_HETU in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.EXPLICIT_HETU_USE in context.participant?.semanticRelations.orEmpty() &&
            ((Vibhakti.TRTIYA in context.morphologicalCandidates && SasthiHetuprayogeSutra.permitsCase(context, Vibhakti.TRTIYA)) ||
                (Vibhakti.SASTHI in context.morphologicalCandidates && SasthiHetuprayogeSutra.permitsCase(context, Vibhakti.SASTHI)))
)
    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        if (dev.panini.analysis.VartikaCaseUses.nimitta(context)) return VibhaktiRuleResult.Assigned(context.morphologicalCandidates.single(), KarakaEvidence(number, text, "The written case stands.${dev.panini.analysis.VartikaCaseUses.note(context)}"))
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates &&
            SasthiHetuprayogeSutra.permitsCase(context, Vibhakti.TRTIYA)) Vibhakti.TRTIYA else Vibhakti.SASTHI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes pronominal cause with hetu (2.3.27)."),
        )
    }
}
