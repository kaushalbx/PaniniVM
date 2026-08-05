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
 * Sūtra 2.2.29: चार्थे द्वन्द्वः.
 * Prescribes Dvandva compound formation between multiple subanta terms connected in 'ca' (and) sense.
 * Matching: requires at least two padas in the context (any vibhakti — Dvandva is vibhakti-agnostic).
 */
object CartheDvandvahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.29",
    text = "चार्थे द्वन्द्वः",
    hindiExplanation = "'च' (और) के अर्थ में स्थित अनेक समर्थ सुबन्तों का द्वन्द्व समास होता है (उदा. रामश्च कृष्णश्च = रामकृष्णौ)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220029,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.DVANDVA
    override val isGeneralFallback: Boolean = true
    // Dvandva is 'ca'-coordinated: any two prathama-inflected nominals qualify
    override fun matches(context: SamasaRuleContext): Boolean = context.padas.size >= 2

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.2.29 forms Dvandva compound '$stem'.",
        )
    }
}
