package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.UniversalSamasaTransformation

/** 8.2.72: वस्रंसुध्वंस्वनडुहां दः — final consonant is replaced by द् in pada position. */
object VasransudhvasvanaduhamDahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "8.2.72",
    text = "वस्रंसुध्वंस्वनडुहां दः",
    hindiExplanation = "वस्, स्रंस्, ध्वंस् और अनडुह् के अन्त्य वर्ण के स्थान पर पदान्त में द् होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820072,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaPriority = 70,
), SamasaSutra, UniversalSamasaTransformation {
    private val lexicalMembers = setOf("स्रंस्", "ध्वंस्", "अनडुह्")

    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && eligible(context.purvaPada.upadesha)

    override fun apply(context: SamasaRuleContext): SamasaRuleResult.Formed {
        val source = context.purvaPada.upadesha
        val replacement = replaceFinalConsonantWithD(source)
        return SamasaRuleResult.Formed(
            compoundStem = replacement + context.padas.drop(1).joinToString("") { it.upadesha },
            explanation = "8.2.72 replaces the final consonant of $source with द् in pada position.",
            memberEdits = mapOf(0 to replacement),
        )

    }

    private fun eligible(stem: String): Boolean = stem.endsWith("वस्") || stem in lexicalMembers

    private fun replaceFinalConsonantWithD(stem: String): String {
        require(stem.endsWith('्')) { "8.2.72 requires a consonant-final stem: $stem" }
        return stem.dropLast(2) + "द्"
    }
}
