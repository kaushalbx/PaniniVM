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
 * Sūtra 2.1.47: क्षेपे प्रशंसायां च.
 * Prescribes Saptamī Tatpuruṣa compound in deprecation or laudatory context.
 * Example: पात्रे सम्मिताः = पात्रेसम्सम्मिताः / पात्रसम्मिताः (pātrasammitāḥ - fit only to eat).
 */
object KsepePrasamsayamChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.47",
    text = "क्षेपे प्रशंसायां च",
    hindiExplanation = "क्षेप (निन्दा) अथवा प्रशंसा अर्थ में सप्तम्यन्त समर्थ सुबन्त का समास होता है (उदा. पात्रसम्मिताः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210047,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val ksepaUttaras = setOf("सम्मित", "क्षेडी", "गर्दभ", "मत्कुण", "कुक्कुट")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            ksepaUttaras.contains(uttara)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.47 forms Saptamī Tatpuruṣa compound '$compoundStem' in kṣepa/praśaṁsā context.",
        )
    }
}
