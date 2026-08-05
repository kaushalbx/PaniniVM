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
 * Sūtra 2.2.11: पूरणगुणसुहितार्थसदसत्प्रकृत्यव्ययतव्यसमानाधिकरणेन.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound with pūraṇa (ordinals), guṇa (qualities), suhita, etc.
 * Example: सतां श्रेष्ठः / काकस्य वार्ष्ण्यम् (no compound *kākavārṣṇyam*).
 */
object PuranaGunasuhitaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.11",
    text = "पूरणगुणसुहितार्थसदसत्प्रकृत्यव्ययतव्यसमानाधिकरणेन",
    hindiExplanation = "पूरण (क्रमवाचक), गुणवाचक, सुहित आदि के साथ षष्ठी तत्पुरुष समास का निषेध होता है (उदा. काकस्य वार्ष्ण्यम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220011,
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
