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
import dev.panini.vyakaranam.ast.MulaPratipadika

/**
 * Sūtra 2.3.26 षष्ठी हेतुप्रयोगे.
 * Assigns Ṣaṣṭhī when the word 'hetu' is explicitly used along with cause/effect.
 */
object SasthiHetuprayogeSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.26", text = "षष्ठी हेतुप्रयोगे",
    hindiExplanation = "हेतु शब्द का प्रयोग होने पर षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230026,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    fun hasConstruction(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            (context.participant?.governingNominal == null || hasHetuGovernor(context)) &&
            SemanticRelation.EXPLICIT_HETU_USE in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.CAUSE_HETU in context.participant?.semanticRelations.orEmpty()

    fun hasHetuGovernor(context: VibhaktiRuleContext): Boolean {
        val stem = context.participant?.governingNominal?.pratipadika as? MulaPratipadika ?: return false
        return stem.text == "हेतु" && stem.vikaras.isEmpty()
    }

    fun candidateCases(context: VibhaktiRuleContext): Set<Vibhakti> =
        if (hasConstruction(context)) setOf(Vibhakti.SASTHI) else emptySet()

    /** A written member cannot change case independently of its co-referent partner. */
    fun permitsCase(context: VibhaktiRuleContext, case: Vibhakti): Boolean {
        val participant = context.participant ?: return true
        val pronominal = SemanticRelation.PRONOMINAL_HETU in participant.semanticRelations
        participant.governingNominal?.let { governor ->
            return governor.sup.affixCandidates.any {
                it.vibhakti == case && (!pronominal || participant.vacana == null || it.vacana == participant.vacana)
            }
        }
        val linked = context.allParticipants.filter { it.governingNominal === participant.expression }
        return linked.all { case in it.possibleVibhaktis &&
            (!pronominal || participant.vacana == null || it.vacana == null || participant.vacana == it.vacana) }
    }

    override fun matches(context: VibhaktiRuleContext): Boolean =
        hasConstruction(context) &&
            permitsCase(context, Vibhakti.SASTHI) &&
            !SarvanamnasTrtiyaCaSutra.matches(context) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes cause with explicit use of word hetu (2.3.26)."),
    )
}
