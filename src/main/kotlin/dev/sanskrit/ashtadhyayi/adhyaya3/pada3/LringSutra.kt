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
 * 3.3.139: liṅnimitte lṛṅ kriyātipattau.
 * Selects the conditional mood 'lṛṅ' under conditional conditions (kriyātipatti).
 */
object LringSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.139",
    text = "लिङ्निमित्ते लृङ् क्रियातिपत्तौ",
    hindiExplanation = "क्रिया की असिद्धि होने पर भविष्यत् और भूतकाल में लृङ् लकार होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330139,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(c: DerivationState) =
        c.effectiveContext.rupa.lakara == Lakara.LRNG &&
            HasKala(Kala.BHAVISYAT).matches(c) && c.stage == DerivationStage.INITIAL

    override fun apply(c: DerivationState) = DerivationChange(
        c.addTerm(DerivationTerm("lrng", "लृङ्", TermKind.PRATYAYA, upadesha = "लृङ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED), "3.3.139 selects लृङ्."
    )
}
