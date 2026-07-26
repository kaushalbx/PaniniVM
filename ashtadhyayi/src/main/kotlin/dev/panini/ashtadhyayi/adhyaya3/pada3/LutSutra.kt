package dev.panini.ashtadhyayi.adhyaya3.pada3

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

/**
 * 3.3.15: anadyatane luṭ.
 * Selects the periphrastic future 'luṭ' under non-today future conditions.
 */
object LutSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.15",
    text = "अनद्यतने लुट्",
    hindiExplanation = "अनद्यतन भविष्यत् काल में लुट् लकार होता है।",
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
        c.effectiveContext.rupa.lakara == Lakara.LUT &&
            HasKala(Kala.BHAVISYAT).matches(c) && c.stage == DerivationStage.INITIAL

    override fun apply(c: DerivationState) = DerivationChange(
        c.addTerm(DerivationTerm("lut", "लुट्", TermKind.PRATYAYA, upadesha = "लुट्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED), "3.3.15 selects लुट्."
    )
}
