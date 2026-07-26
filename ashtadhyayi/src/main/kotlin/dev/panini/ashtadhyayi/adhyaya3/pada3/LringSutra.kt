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
