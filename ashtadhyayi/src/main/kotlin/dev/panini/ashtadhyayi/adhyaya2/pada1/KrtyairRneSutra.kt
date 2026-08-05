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
 * Sūtra 2.1.33: कृत्यैर्ऋणे.
 * Prescribes Tṛtīyā Tatpuruṣa compound with kṛtya-ending words when obligation/debt is signified.
 * Example: कुशाग्रच्छेद्यः.
 */
object KrtyairRneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.33",
    text = "कृत्यैर्ऋणे",
    hindiExplanation = "तृतीयान्त समर्थ सुबन्त का कृत्य प्रत्ययान्त के साथ ऋण अर्थ में तत्पुरुष समास होता है (उदा. कुशाग्रच्छेद्यः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210033,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val krtyaSuffixes = setOf("देय", "छेद्य", "धार्य", "कृत्य", "कार्य")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.TRTIYA &&
            krtyaSuffixes.any { uttara.endsWith(it) }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.33 forms Kṛtya-Ṛṇa Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
