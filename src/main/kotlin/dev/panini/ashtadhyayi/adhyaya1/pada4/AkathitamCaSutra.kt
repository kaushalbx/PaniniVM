package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleResult

/**
 * Sūtra 1.4.51 अकथितं च.
 * Assigns Karma-saṃjñā to secondary arguments (gauṇa karman) of dvikarmaka roots
 * (दुह्, याच्, रुध्, प्रच्छ्, चि, ब्रू, शास्, जि, मन्थ्, मुष्, नी, हृ, कृष्, वह्).
 */
object AkathitamCaSutra : Sutra<KarakaRuleContext, KarakaRuleResult>(
    number = "1.4.51", text = "अकथितं च",
    hindiExplanation = "अपादानादिभिरविशेषितः कारकः कर्मसंज्ञः स्यात्। (दुह्-याच्-रुध्-प्रच्छि-चि-ब्रू-शासि-जि-मन्थि-मुषाम्; नी-हृ-कृष्-वहम्)।",
    type = SutraType.SAMJNA, chapter = 1, pada = 4, optional = false, kramaValue = 140051,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.DHATU, SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA_CANDIDATE),
    adhikara = setOf("1.4.23", "1.4.49"),
) {
    private val dvikarmakaDhatus = setOf(
        "दुह्", "याच्", "रुध्", "प्रच्छ्", "चि", "ब्रू", "शास्", "जि", "मन्थ्", "मुष्",
        "नी", "हृ", "कृष्", "वह्", "दोह्", "याच्", "रोध्", "प्रच्छ्", "चे", "ब्रू", "शास्", "जे", "मन्थ्", "मोष्", "ने", "हार", "कर्ष", "वाह"
    )

    override fun matches(context: KarakaRuleContext): Boolean {
        val root = context.dhatu.surface.trimEnd('्', 'ँ')
        val matchesDhatu = dvikarmakaDhatus.any { d -> root.startsWith(d) || d.startsWith(root) }
        return context.prayoga != Prayoga.CAUSATIVE && matchesDhatu && Karaka.KARMAN in context.candidates
    }

    override fun apply(context: KarakaRuleContext) = KarakaRuleResult.Assigned(
        Karaka.KARMAN,
        KarakaEvidence(number, text, "The secondary argument of a dvikarmaka root is assigned Karma-saṃjñā (1.4.51 अकथितं च)."),
    )
}
