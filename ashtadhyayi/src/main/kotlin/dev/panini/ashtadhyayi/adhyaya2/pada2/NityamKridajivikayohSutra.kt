package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
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
    private val professionOrSportSuffixes = setOf("लेखक", "भञ्जिका", "पालक", "नर्तक", "कारक")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            professionOrSportSuffixes.any { uttara.endsWith(it) }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.17 forms Nitya Ṣaṣṭhī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
