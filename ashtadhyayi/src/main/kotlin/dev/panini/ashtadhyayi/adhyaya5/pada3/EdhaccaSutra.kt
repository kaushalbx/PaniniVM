package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

import dev.panini.shiksha.Samjna

/**
 * 5.3.43: एधच्च.
 * Prescribes suffix 'धा' with 'ए' augment after 'एक' stem yielding 'एकधा' (ekadhā).
 */
object EdhaccaSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.43",
    text = "एधच्च",
    hindiExplanation = "एक प्रातिपदिक से धा प्रत्यय परे एध आगम होकर 'एकधा' रूप सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 3,
    optional = false,
    kramaValue = 530043,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.3.42"),
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "धा" || it.surface == "धा" }
        return !isAlreadyApplied && (lastTerm.upadesha == "एक" || lastTerm.surface == "एक")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhaTerm = DerivationTerm(
            id = "taddhita_dha",
            surface = "धा",
            kind = TermKind.PRATYAYA,
            upadesha = "धा",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + dhaTerm),
            explanation = "$text: added Taddhita division suffix धा for 'एक' -> 'एकधा'"
        )
    }
}
