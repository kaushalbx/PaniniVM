package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.44: च्लेः सिच्. Substitutes सिच् for the abstract aorist marker च्लि. */
object ClehSicSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.44",
    text = "च्लेः सिच्",
    hindiExplanation = "च्लि के स्थान पर सिच् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310044,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { it.upadesha == "च्लि" }

    override fun apply(context: DerivationState): DerivationChange {
        val cli = context.terms.first { it.upadesha == "च्लि" }
        val sic = DerivationTerm(
            id = cli.id,
            surface = "सिच्",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.U, ItMarker.KIT),
            upadesha = "सिच्",
        )
        return DerivationChange(
            context.replaceTerm(cli.id, sic).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            "3.1.44 substitutes सिच् for च्लि.",
        )
    }
}
