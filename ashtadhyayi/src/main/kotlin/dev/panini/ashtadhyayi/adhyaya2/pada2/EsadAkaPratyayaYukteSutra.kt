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
 * Sūtra 2.2.7: ईषदकप्रत्यययुक्ते.
 * Prohibits Nañ Tatpuruṣa compound with eṣat / aka-pratyayanta terms.
 * Example: ईषत् कतम् / अकारः (no Nañ compound formed).
 */
object EsadAkaPratyayaYukteSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.7",
    text = "ईषदकप्रत्यययुक्ते",
    hindiExplanation = "ईषत् अव्यय तथा अक-प्रत्ययान्त पदों के साथ नञ् समास का निषेध होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220007,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.NAN_TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.NAN_TATPURUSA &&
            (uttara.startsWith("ईषत्") || uttara.endsWith("क"))
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
