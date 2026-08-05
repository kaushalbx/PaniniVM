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
 * Sūtra 2.2.10: न निर्धारणे.
 * Prohibits Ṣaṣṭhī Tatpuruṣa compound formation when the genitive case denotes Nirdhāraṇa (specification from a group).
 * Example: नृणाम् द्विजः श्रेष्ठः (no compound *nṛdvijaḥ* in nirdhāraṇa sense).
 */
object NaNirdhareSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.10",
    text = "न निर्धारणे",
    hindiExplanation = "निर्धारण (समुदाय से एक का पृथक्करण) अर्थ में विद्यमान षष्ठ्यन्त सुबन्त का समास नहीं होता (उदा. नृणां द्विजः श्रेष्ठः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220010,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        // Triggers for Ṣaṣṭhī Tatpuruṣa in Nirdhāraṇa contexts
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.SASTHI &&
            context.padas.size >= 2
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        // Prohibition rule: does not form compound
        return SamasaRuleResult.NotApplicable
    }
}
