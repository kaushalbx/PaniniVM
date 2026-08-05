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
 * Sūtra 2.2.13: अधिकरणवाचिनश् च.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound when kta-participle denotes locative (adhikaraṇa).
 * Example: राज्ञाम् मतम् / ज्ञानम् (no compound in adhikaraṇa sense).
 */
object AdhikaranavacinasChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.13",
    text = "अधिकरणवाचिनश् च",
    hindiExplanation = "अधिकरण अर्थ में विहित क्तान्त सुबन्त का षष्ठ्यन्त के साथ समास का निषेध होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220013,
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
