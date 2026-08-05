package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.4.2: द्वन्द्वश्च प्राणितूर्यसेनाङ्गानाम्.
 */
object DvandvaschaPranituryaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.4.2",
    text = "द्वन्द्वश्च प्राणितूर्यसेनाङ्गानाम्",
    hindiExplanation = "प्राण्यङ्गानां तूर्याङ्गानां सेनाङ्गानां च द्वन्द्वः एकवद् भवति।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240002,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val pranituryaWords = setOf(
        "पाणि", "पाद", "शिरस्", "ग्रीवा", "अक्षि", "नासिका",
        "मार्दङ्गिक", "वैणविक", "पाणविक",
        "रथिक", "पादात", "अश्वारोह", "हास्तिक",
    )

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val padas = context.padas.map { it.upadesha }
        return context.samasaType == SamasaType.DVANDVA && padas.all { it in pranituryaWords }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.4.2: Formed Samāhāra Dvandva (neuter singular) for prāṇi/tūrya/senā limbs ($compoundStem).",
        )
    }
}
