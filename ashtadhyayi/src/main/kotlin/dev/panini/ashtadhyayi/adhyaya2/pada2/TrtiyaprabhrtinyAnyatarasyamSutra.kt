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
 * Sūtra 2.2.3: तृतीयाप्रभृतीन्यन्यतरस्याम्.
 * Prescribes optional Ekadeśi Tatpuruṣa compound from 3rd vibhakti onwards.
 * Example: पूर्वः अह्नः = पूर्वाह्णः.
 */
object TrtiyaprabhrtinyAnyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.3",
    text = "तृतीयाप्रभृतीन्यन्यतरस्याम्",
    hindiExplanation = "तृतीया आदि विभक्त्यन्त एकादेशी सुबन्त का विकल्प से तत्पुरुष समास होता है (उदा. पूर्वाह्णः)।",
    type = SutraType.VIBHASHA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220003,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val ekadeshaWords = setOf("पूर्व", "अपर", "अधर", "उत्तर")
    private val nonPrathamaVibhaktis = setOf(
        Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI, Vibhakti.SASTHI, Vibhakti.SAPTAMI
    )

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti in nonPrathamaVibhaktis &&
            purva in ekadeshaWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.3 forms Ekadeśi Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
