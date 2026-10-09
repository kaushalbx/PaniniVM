package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.KrtAffix
import dev.panini.shiksha.toDevanagari
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.43: कृत्यैर्ऋणे.
 * Prescribes Saptamī Tatpuruṣa with a yat-derived member in debt/obligation semantics.
 * Example: मासदेयम्.
 */
object KrtyairRneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.43",
    text = "कृत्यैर्ऋणे",
    hindiExplanation = "सप्तम्यन्त सुबन्त का यत्-प्रत्ययान्त के साथ ऋण अथवा नियोग अर्थ में तत्पुरुष समास होता है (उदा. मासदेयम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210043,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.SAPTAMI &&
            context.uttaraPada.krtAffix == KrtAffix.YAT &&
            SamasaSemanticRelation.DEBT_OR_OBLIGATION in context.semanticRelations
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.flatMap { it.varnas }.toDevanagari()
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.43 forms Kṛtya-Ṛṇa Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
