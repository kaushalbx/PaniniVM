package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.12: क्तेन च पूजायाम्.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound when the genitive expresses worshipping / respect with kta-participle.
 * Example: राज्ञाम् पूजितः (no compound *rājapūjitaḥ* in pūjā sense).
 */
object KtenaChAPujayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.12",
    text = "क्तेन च पूजायाम्",
    hindiExplanation = "पूजा अर्थ में विहित क्तान्त सुबन्त का षष्ठ्यन्त के साथ समास का निषेध होता है (उदा. राज्ञां पूजितः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220012,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.SASTHI &&
            context.padas.size >= 2
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
