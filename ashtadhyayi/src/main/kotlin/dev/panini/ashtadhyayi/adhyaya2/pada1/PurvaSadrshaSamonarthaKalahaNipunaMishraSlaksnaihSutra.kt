package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.31: पूर्वसदृशसमोनार्थकलहनिपुणमिश्रश्लक्ष्णैः.
 * Prescribes Tṛtīyā Tatpuruṣa compound with words meaning previous, equal, deficient, conflict, skilled, mixed, smooth.
 * Example: मासेन पूर्वः = मासपूर्वः, पित्रा सदृशः = पितृसदृशः.
 */
object PurvaSadrshaSamonarthaKalahaNipunaMishraSlaksnaihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.31",
    text = "पूर्वसदृशसमोनार्थकलहनिपुणमिश्रश्लक्ष्णैः",
    hindiExplanation = "तृतीयान्त समर्थ सुबन्त का पूर्व, सदृश, सम, ऊन, कलह, निपुण, मिश्र, श्लक्ष्ण शब्दों के साथ तत्पुरुष समास होता है (उदा. पितृसदृशः, मासपूर्वः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210031,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 5,
), SamasaSutra {
    private val targetWords = setOf("पूर्व", "सदृश", "सम", "ऊन", "कलह", "निपुण", "मिश्र", "श्लक्ष्ण")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.TRTIYA &&
            uttara in targetWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.31 forms Pūrva-Sadṛśa Tṛtīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
