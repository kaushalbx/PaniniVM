package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * 2.2.9: याजकादिभिश्च.
 *
 * Ṣaṣṭhī (6th case) nominals compound with agent nouns of the yājakādi group ('yājaka', 'pūjaka', 'paricāraka', 'bhāṣaka').
 */
object YajakadibhishchaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.9",
    text = "याजकादिभिश्च",
    hindiExplanation = "षष्ठ्यन्तं याजकादिभिः सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220009,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.TATPURUSA
    private val yajakadiGroup = setOf("याजक", "पूजक", "परिचारक", "भाषक", "शिक्षक")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return uttara in yajakadiGroup
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.9: Formed Ṣaṣṭhī Tatpuruṣa compound with yājakādi member ($compoundStem).",
        )
    }
}
