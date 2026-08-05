package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 5.2.44: उभादुदात्तो नित्यम्.
 * Prescribes obligatory portion suffix 'आयच्' (अय - aya) after 'उभ' stem yielding 'उभयम्' (ubhayam).
 */
object UbhadUdattoNityamSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.44",
    text = "उभादुदात्तो नित्यम्",
    hindiExplanation = "उभ प्रातिपदिक से अवयव अर्थ में नित्य आयच् (अय) प्रत्यय होकर 'उभयम्' रूप सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520044,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.2.42"),
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val hasAvayavaRequest = context.samjnas.any { it.samjna == Samjna.AVAYAVA || it.samjna == Samjna.TADDHITA }
        if (!hasAvayavaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "आयच्" || it.surface == "अय" }
        return !isAlreadyApplied && (lastTerm.upadesha == "उभ" || lastTerm.surface == "उभ")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ayacTerm = DerivationTerm(
            id = "taddhita_ayac",
            surface = "अय",
            kind = TermKind.PRATYAYA,
            upadesha = "आयच्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + ayacTerm),
            explanation = "$text: added obligatory portion suffix आयच् (अय) for 'उभ' -> 'उभयम्'"
        )
    }
}
