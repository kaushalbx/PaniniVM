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
 * Sūtra 2.2.14: कर्मणि च (registered as 2.2.102 for unique ID).
 * Prohibition of Ṣaṣṭhī Tatpuruṣa when object has Ṣaṣṭhī suffix.
 */
object KarmaniChaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.102",
    text = "कर्मणि च",
    hindiExplanation = "कर्म अर्थ में षष्ठी प्रत्यय होने पर समर्थ सुबन्त का समास नहीं होता है (उदा. आश्चर्यो गवां दोहोऽगोपेन)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220102,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return false
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
