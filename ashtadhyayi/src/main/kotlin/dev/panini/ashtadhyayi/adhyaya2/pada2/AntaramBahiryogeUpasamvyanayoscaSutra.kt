package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.4: अन्तरं बहिर्योगे उपसंव्यानयोश्च.
 * Prescribes Tatpuruṣa compound of 'antara' when signifying exterior connection or undergarment.
 * Example: गृहान्तरम्, परिधानम्.
 */
object AntaramBahiryogeUpasamvyanayoscaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.4",
    text = "अन्तरं बहिर्योगे उपसंव्यानयोश्च",
    hindiExplanation = "बहिर्योग और उपसंव्यान अर्थ में अन्तर शब्द का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. गृहान्तरम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220004,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            uttara == "अन्तर"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.4 forms Antara Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
