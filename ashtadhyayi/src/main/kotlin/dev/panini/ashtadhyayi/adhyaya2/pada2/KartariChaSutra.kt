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
 * Sūtra 2.2.16: कर्तरि च.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound when genitive expresses agent (kartari) with kṛt affixes.
 * Example: भवतः शायिका (no compound *bhavacchāyikā* in kartari genitive).
 */
object KartariChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.16",
    text = "कर्तरि च",
    hindiExplanation = "कर्तरि षष्ठी का कृदन्त सुबन्तों के साथ समास का निषेध होता है (उदा. भवतः शायिका)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220016,
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
