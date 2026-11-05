package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
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
import dev.panini.analysis.OptionalKarakaRuleOutcome
import dev.panini.analysis.meaningEvidenceStrength

/**
 * Sūtra 1.4.53 हृक्रोरन्यतरस्याम्.
 * For roots hṛ (हृ) and kṛ (कृ) in causative (ṇi) constructions, the agent of non-causative
 * optionally becomes object (karman) or remains instrument/agent (karana/kartr).
 */
object HrKrorAnyatarasyamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.53", text = "हृक्रोरन्यतरस्याम्",
    hindiExplanation = "हृञ्-कृञोः अणौ यः कर्ता स णौ वा कर्मसंज्ञः स्यात्।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = true, kramaValue = 140053,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23"),
    anuvrtti = setOf("1.4.49", "1.4.52"),
), OptionalKarakaRuleOutcome {
    override fun declinedDesignation(context: KarakaRuleContext): KarakaRuleResult.Assigned {
        val agent = SvatantrahKartaSutra.apply(context)
        return agent.copy(evidence = agent.evidence.copy(reason =
            "The aṇi-kartā established by 1.4.53 retains agent designation when optional object designation is declined."))
    }
    override fun matches(context: KarakaRuleContext): Boolean {
        val matchesRoot = KarakaDhatuDomains.isHrKr(context)
        return KarakaDhatuDomains.isCausativeConstruction(context) &&
            matchesRoot &&
            context.participant.meaningEvidenceStrength(SemanticRelation.CAUSEE_AGENT) >= 2 &&
            context.participant.meaningEvidenceStrength(SemanticRelation.PRIOR_CAUSATIVE_AGENT) < 2
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The non-causative agent of hṛ/kṛ optionally becomes Karman in causative (1.4.53)."),
    )
}
