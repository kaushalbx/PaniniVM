package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.KrtAffix
import dev.panini.core.Vibhakti
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.17: नित्यं क्रीडाजीविकयोः.
 * Prescribes obligatory Ṣaṣṭhī Tatpuruṣa compound when designating sports or livelihoods.
 * Example: dantalekhakah (livelihood), uddalakapuspabhanjika (sport).
 */
object NityamKridajivikayohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.17",
    text = "नित्यं क्रीडाजीविकयोः",
    hindiExplanation = "षष्ठ्यन्त समर्थ सुबन्त का क्रीडा और जीविका अर्थ में नित्य तत्पुरुष समास होता है (उदा. दन्तलेखकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220017,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.SASTHI &&
            context.uttaraPada.krtAffix == KrtAffix.NVUL &&
            context.semanticRelations.any {
                it == SamasaSemanticRelation.SPORT_OR_PLAY || it == SamasaSemanticRelation.LIVELIHOOD
            }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.17 forms Nitya Ṣaṣṭhī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
