package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.Kala
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasKala
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object VartamaneLatSutra : Sutra<DerivationState, DerivationChange>(
    "3.2.123",
    "वर्तमाने लट्",
    "वर्तमान अर्थ में लट् होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320123,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(c: DerivationState) =
        c.effectiveContext.rupa.lakara == Lakara.LAT &&
            HasKala(Kala.VARTAMANA).matches(c) && c.stage == DerivationStage.INITIAL

    override fun apply(c: DerivationState) = DerivationChange(
        c.addTerm(DerivationTerm("lat", "लट्", TermKind.PRATYAYA))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED), "3.2.123 selects लट्."
    )
}
