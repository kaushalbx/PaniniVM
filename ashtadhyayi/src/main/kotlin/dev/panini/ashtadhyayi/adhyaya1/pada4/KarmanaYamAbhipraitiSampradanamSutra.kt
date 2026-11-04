package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.KarakaDhatuDomains
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleResult
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.hasEstablishedMeaning
import dev.panini.analysis.IntendedObject
import dev.panini.analysis.isStrongestOrdinaryReading

object KarmanaYamAbhipraitiSampradanamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.32", text = "कर्मणा यमभिप्रैति स सम्प्रदानम्",
    hindiExplanation = "कर्म के द्वारा जिसे अभिप्रेत किया जाता है उसकी सम्प्रदानसंज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140032,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
) {
    private val specialRecipientRules by lazy {
        listOf(RucyarthanamPriyamanahSutra, ShlaghahnusthashapamJnyipsyamanahSutra,
            DharerUttamarnahSutra, SprherIpsitahSutra, KrudhaDruhersyasuyarthanamYamPratiKopahSutra,
            KrudhaDruhorUpasrstayohKarmaSutra, RadhiksyorYasyaViprasnahSutra,
            PratyangbhruvahPurvasyaKartaSutra, AnupratigrnasCaSutra, ParikrayaneSampradanamAnyatarasyamSutra)
    }

    override fun matches(context: KarakaRuleContext): Boolean {
        if (!hasRecipientIntent(context)) return false
        fun isObject(other: dev.panini.analysis.ParticipantFacts): Boolean {
            if (other.id == context.participant.id) return false
            // Object status is a prerequisite established by the complete
            // canonical rule engine, including special and optional readings.
            return context.mediatingObjectResolutions[other.id]?.let {
                (it.resolved == Karaka.KARMAN || Karaka.KARMAN in it.coexistingKarakas) &&
                    it.evidence.any { proof -> proof.applied && proof.sutra.startsWith("1.4.") }
            } == true
        }
        val binding = context.participant.intendedObject
        val eligibleWrittenObject = context.allParticipants.any(::isObject)
        return when (binding) {
            is IntendedObject.Written -> context.allParticipants.singleOrNull { it.id == binding.participantId }
                ?.let(::isObject) == true
            IntendedObject.Implied -> true
            null -> eligibleWrittenObject
        }
    }

    /** Recipient-side eligibility, before requiring a proven mediating object. */
    fun hasRecipientIntent(context: KarakaRuleContext): Boolean {
        if (!context.participant.hasEstablishedMeaning(SemanticRelation.RECIPIENT)) return false
        val binding = context.participant.intendedObject
        val unboundSpecialReading = binding == null &&
            (KarakaDhatuDomains.isPriorActionRecipientDomain(context) ||
                SemanticRelation.PRIOR_ACTION_AGENT in context.participant.semanticRelations ||
                SemanticRelation.INQUIRY_DESTINY_TARGET in context.participant.semanticRelations)
        val isExcludedSampradana = specialRecipientRules.any { it.matches(context) } || unboundSpecialReading
        return !isExcludedSampradana && context.participant.isStrongestOrdinaryReading(SemanticRelation.RECIPIENT)
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.SAMPRADANA,
        KarakaEvidence(number, text, "The participant is the intended recipient of the object (कर्मणा यम् अभिप्रैति)."),
    )
}
