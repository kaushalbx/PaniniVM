package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.core.Karaka
import dev.panini.core.LopaType
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
 * Sūtra 2.3.45 नक्षत्रे च लुपि.
 * Assigns Tṛtīyā or Saptamī for asterism words when lup elision occurs.
 */
object NaksatreCaLupiSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.45", text = "नक्षत्रे च लुपि",
    hindiExplanation = "लुप्-प्रत्यय-लोप होने पर नक्षत्रवाचक शब्द में काल अर्थ में तृतीया अथवा सप्तमी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230045,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.36", "2.3.43", "2.3.44"),
), OptionalVibhaktiRuleOutcome {
    override val permittedCases = setOf(Vibhakti.TRTIYA, Vibhakti.SAPTAMI)

    fun hasNakshatraLupLocus(context: VibhaktiRuleContext): Boolean {
        val participant = context.participant ?: return false
        val relations = participant.semanticRelations
        return context.karaka == Karaka.ADHIKARANA &&
            SemanticRelation.LOCATION in relations &&
            SemanticRelation.ASTROLOGICAL_TIME in relations &&
            SemanticRelation.NAKSATRA_EXPRESSION in relations &&
            participant.taddhitaLopa?.lopa == LopaType.LUP
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            hasNakshatraLupLocus(context) &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes nakṣatra time as an action locus under lup-elision " +
                "(${context.participant?.taddhitaLopa?.sutra}; 2.3.45)."),
        )
    }
}
