package dev.sanskrit.ashtadhyayi.adhyaya3.pada3

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

object BhavishyatiLrtSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.15",
    text = "लृट् शेषे च",
    hindiExplanation = "भविष्यत् काल में लृट् लकार होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330015,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(c: DerivationState) =
        HasKala(Kala.BHAVISYAT).matches(c) && c.stage == DerivationStage.INITIAL

    override fun apply(c: DerivationState) = DerivationChange(
        c.addTerm(DerivationTerm("lrt", "लृट्", TermKind.PRATYAYA, upadesha = "लृट्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED), "3.3.15 selects लृट्."
    )
}
