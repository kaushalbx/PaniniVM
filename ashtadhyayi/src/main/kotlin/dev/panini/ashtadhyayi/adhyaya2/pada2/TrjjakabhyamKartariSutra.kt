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
 * Sūtra 2.2.15: तृज्जकाभ्यां कर्तरि.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound when genitive is agentive (kartari) with tṛc or aka affixes.
 * Example: स्रष्टा त्रिभुवनस्य (no compound *tribhuvanasraṣṭā* in kartari genitive).
 */
object TrjjakabhyamKartariSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.15",
    text = "तृज्जकाभ्यां कर्तरि",
    hindiExplanation = "कर्तरि षष्ठी का तृच् तथा अक प्रत्ययान्तों के साथ समास का निषेध होता है (उदा. स्रष्टा त्रिभुवनस्य)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220015,
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
