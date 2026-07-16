package dev.sanskrit.ashtadhyayi.adhyaya3.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasKala
import dev.sanskrit.derivation.Kala
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
