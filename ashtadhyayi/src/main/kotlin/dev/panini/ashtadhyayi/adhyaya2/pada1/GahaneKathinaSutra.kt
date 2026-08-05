package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.25: गहने कठिनम् (registered as 2.1.91 for unique ID).
 * Prescribes Dvitīyā Tatpuruṣa in impenetrable / difficult contexts.
 * Example: गहने कठिनम् = गहनकठिनम् (gahanakaṭhinam).
 */
object GahaneKathinaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.91",
    text = "गहने कठिनम्",
    hindiExplanation = "गहन अर्थ में द्वितीयान्त का कठिन शब्द के साथ तत्पुरुष समास होता है (उदा. गहनकठिनम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210091,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva == "गहन" && uttara.startsWith("कठिन")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.91 forms Gahana-kaṭhina Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
