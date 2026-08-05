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
 * Sūtra 2.2.24: अनेकमन्यपदार्थे.
 * Prescribes Bahuvrīhi compound: pūrvapada is in prathama (first case), meaning belongs to an external referent.
 * Matching: pūrvapada vibhakti == PRATHAMA and samasaType == BAHUVRIHI (declared by caller).
 */
object AnekamAnyapadartheSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.24",
    text = "अनेकमन्यपदार्थे",
    hindiExplanation = "अन्य पद का अर्थ प्रधान होने पर प्रथमान्त समर्थ सुबन्तों का बहुव्रीहि समास होता है (उदा. पीताम्बरः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.BAHUVRIHI
    override val isGeneralFallback: Boolean = true
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.PRATHAMA

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.2.24 forms Bahuvrīhi compound '$stem'.",
        )
    }
}
