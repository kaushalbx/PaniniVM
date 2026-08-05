package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.53: पद्दन्नोमास्हृन्ग्निशत्कष्वनलक्षणे.
 * Substitutions pad, danta, nas, mās, hṛd before certain endings.
 */
object PaddhannomasdhrnSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.53",
    text = "पद्दन्नोमास्हृन्ग्निशत्कष्वनलक्षणे",
    hindiExplanation = "अनलक्षण विषय में पद, दत्, नस्, मास्, हृद् आदि आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630053,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "पाद" || first == "दन्त" || first == "नासिक" || first == "मास" || first == "हृदय"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.53 applies pad/dat/nas/mās/hṛd substitutions in '$compoundStem'.",
        )
    }
}
