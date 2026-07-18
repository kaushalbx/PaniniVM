package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.108: jher jus. Replaces liṅ's झि with जुस्; initial ज् is an it-marker. */
object JherJusSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.108",
    text = "झेर्जुस्",
    hindiExplanation = "लिङ् में झि के स्थान पर जुस् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340108,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.stage != DerivationStage.INITIAL &&
            ending.matchesUpadesha("झि") &&
            ending.surface != "ुस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "ुस्")),
            "3.4.108 replaces झि with जुस्; its initial ज् is not pronounced.",
        )
    }
}
