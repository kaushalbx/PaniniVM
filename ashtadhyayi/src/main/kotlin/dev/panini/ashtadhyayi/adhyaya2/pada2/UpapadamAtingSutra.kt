package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.19: उपपदमतिङ्.
 * Prescribes Tatpuruṣa compound of an Upapada (noun standing in locative in Kṛt-pratyaya rules, 3.1.92)
 * with a Kṛdanta term that is not a finite verb (atiṅ).
 * Example: कुम्भं करोति इति = कुम्भकारः, साम गाति इति = सामगः, धर्मं जानाति इति = धर्मज्ञः.
 */
object UpapadamAtingSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.19",
    text = "उपपदमतिङ्",
    hindiExplanation = "उपपद सुबन्त का अतिङ् (अ-तिङन्त) कृदन्त के साथ समर्थ समास होता है (उदा. कुम्भकारः, सामगः, धर्मज्ञः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220019,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.UPAPADA_TATPURUSA,
), SamasaSutra {
    private val krdantaStems = setOf(
        "कार", "ग", "ज्ञ", "द", "धर", "प", "हर", "कर", "सद्", "स्थ", "ज", "दा", "पा", "गा", "विध", "ज्ञानी", "पालीन"
    )

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        if (context.samasaType == SamasaType.UPAPADA_TATPURUSA) return true
        val uttara = context.uttaraPada
        return Samjna.KRT in uttara.samjnas || uttara.upadesha in krdantaStems
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha.removeSuffix("न्").removeSuffix("न").removeSuffix("म्")
        val uttara = context.uttaraPada.upadesha
        val stem = "$purva$uttara"
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.2.19 (उपपदमतिङ्) forms Upapada Tatpuruṣa compound '$stem'.",
        )
    }
}
