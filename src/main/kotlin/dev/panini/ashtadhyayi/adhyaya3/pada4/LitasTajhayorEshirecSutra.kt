package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.81: लिटस्तझयोरेशिरेच्. */
object LitasTajhayorEshirecSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.81", text = "लिटस्तझयोरेशिरेच्",
    hindiExplanation = "लिट् में आत्मनेपद के त और झ के स्थान पर क्रमशः एश् और इरेच् होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340081,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            ((ending.matchesUpadesha("त") && ending.surface != "ए") ||
                (ending.matchesUpadesha("झ") && ending.surface != "इरे"))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = if (ending.matchesUpadesha("त")) "ए" else "इरे"
        return DerivationChange(context.replaceTerm(ending.id, ending.copy(surface = replacement)),
            "3.4.81 replaces ${ending.upadesha} with $replacement in लिट्.")
    }
}
