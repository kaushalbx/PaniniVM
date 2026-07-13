package dev.sanskrit.ashtadhyayi.adhyaya3.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasKala
import dev.sanskrit.derivation.Kala
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        HasKala(Kala.VARTAMANA).matches(c) && c.stage == DerivationStage.INITIAL

    override fun apply(c: DerivationState) = DerivationChange(
        c.addTerm(DerivationTerm("lat", "लट्", TermKind.PRATYAYA))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED), "3.2.123 selects लट्."
    )
}
