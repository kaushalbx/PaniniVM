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
 * Sūtra 2.2.14: कर्मणि च.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound when genitive case expresses object of action (karmaṇi) with kṛt affixes.
 * Example: आश्चर्यो गवां दोहोऽगोपेन (no compound *godohaḥ* when expressing object of action).
 */
object KarmaniChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.14",
    text = "कर्मणि च",
    hindiExplanation = "कर्मणि षष्ठी का तृजक आदि के साथ समास का निषेध होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220014,
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
