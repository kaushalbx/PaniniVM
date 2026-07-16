package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        return context.effectiveContext.rupa.lakara in setOf(Lakara.LAT, Lakara.LET) &&
            context.substitutions.none { it.sutra == "3.4.96" } &&
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
