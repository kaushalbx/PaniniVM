package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult
import dev.panini.vyakaranam.analysis.SemanticRelation

/**
 * Sūtra 1.4.53 ह्रिक्रोरन्यतरस्याम्.
 * For roots hṛ (हृ) and kṛ (कृ) in causative (ṇi) constructions, the agent of non-causative
 * optionally becomes object (karman) or remains instrument/agent (karana/kartr).
 */
object HrKrorAnyatarasyamSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.53", text = "ह्रिक्रोरन्यतरस्याम्",
    hindiExplanation = "हृञ्-कृञोः अणौ यः कर्ता स णौ वा कर्मसंज्ञः स्यात्।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = true, kramaValue = 140053,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23", "1.4.49"),
) {
    private val hrKrRoots = setOf("हृ", "कृ", "हार", "कार", "हर", "कर")

    override fun matches(context: KarakaRuleContext): Boolean {
        val root = context.dhatu.surface.trimEnd('्', 'ँ')
        val matchesRoot = hrKrRoots.any { r -> root.contains(r) || r.contains(root) }
        return context.prayoga == Prayoga.CAUSATIVE &&
            matchesRoot &&
            SemanticRelation.PROMPTER_CAUSE !in context.participant.semanticRelations &&
            Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The non-causative agent of hṛ/kṛ optionally becomes Karman in causative (1.4.53)."),
    )
}
