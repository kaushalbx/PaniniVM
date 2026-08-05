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
 * Sūtra 2.1.66: प्रशंसावचनैश्च.
 * Prescribes Karmadhāraya compound with words expressing praise or excellence (e.g. prakāṇḍa, mattaka).
 * Example: गोप्रकाण्डम्, छात्रमत्तकः.
 */
object PrasamsavacanaiscaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.66",
    text = "प्रशंसावचनैश्च",
    hindiExplanation = "प्रशंसावाचक सुबन्त शब्दों के साथ समर्थ सुबन्त का कर्मधारय समास होता है (उदा. गोप्रकाण्डम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210066,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    private val praiseWords = setOf("प्रकाण्ड", "मत्तक", "उद्धत", "प्रशस्त", "वर", "श्रेष्ठ")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            uttara in praiseWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.66 forms Praśaṁsā Karmadhāraya compound '$compoundStem'.",
        )
    }
}
