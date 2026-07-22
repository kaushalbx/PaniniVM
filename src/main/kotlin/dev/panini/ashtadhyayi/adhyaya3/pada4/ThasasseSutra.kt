package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.80: थासस्से. */
object ThasasseSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.80",
    text = "थासस्से",
    hindiExplanation = "टित् लकार में आत्मनेपद के थास् प्रत्यय के स्थान पर से होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340080,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        return context.effectiveContext.rupa.lakara in setOf(Lakara.LAT, Lakara.LET, Lakara.LIT, Lakara.LOT, Lakara.LRT, Lakara.LUT) &&
            context.substitutions.none { it.sutra == "3.4.96" } &&
            context.substitutions.none { it.sutra == "3.4.91" } &&
            ending.matchesUpadesha("थास्") &&
            ending.surface != "से"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "से")),
            "3.4.80 replaces the Ātmanepada थास् ending with से.",
        )
    }
}
