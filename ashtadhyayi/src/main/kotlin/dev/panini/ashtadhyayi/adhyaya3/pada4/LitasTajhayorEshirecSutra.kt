package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Varna
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
            ((ending.matchesUpadesha("त") && ending.varnas != listOf(Svara.E)) ||
                (ending.matchesUpadesha("झ") && ending.varnas != listOf(Svara.I, Vyanjana.RA, Svara.E)))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement: List<Varna> = if (ending.matchesUpadesha("त")) listOf(Svara.E) else listOf(Svara.I, Vyanjana.RA, Svara.E)
        val replacementLabel = if (ending.matchesUpadesha("त")) "ए" else "इरे"
        return DerivationChange(context.replaceWholeAffix(ending.id, replacement, sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume),
            "3.4.81 replaces ${ending.upadesha} with $replacementLabel in लिट्.")
    }
}
