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

/** 3.4.95: आत ऐ. The आ of the two dual Ātmanepada endings becomes ऐ in LET. */
object AtaAiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.95", text = "आत ऐ", hindiExplanation = "लेट् में आत्मनेपद द्विवचन के आ के स्थान पर ऐ होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340095,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LET &&
            ending.upadesha in setOf("आताम्", "आथाम्") && ending.surface in setOf("आते", "आथे")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "ऐ${ending.surface.drop(1)}")),
            "3.4.95 replaces the initial आ of the dual Ātmanepada ending with ऐ.",
        )
    }
}
