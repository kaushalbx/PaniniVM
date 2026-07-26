package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.96: तव्यत्तव्यानीयरः.
 * Introduces the prescriptive kṛtya suffixes 'तव्यत्', 'तव्य', and 'अनीयर्' after a verbal root.
 */
object TavyattavyaaniyarahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.96",
    text = "तव्यत्तव्यानीयरः",
    hindiExplanation = "धातु से परे तव्यत्, तव्य तथा अनीयर् प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310096,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isTavyaRequested = context.samjnas.any { it.samjna == Samjna.TAVYA || it.samjna == Samjna.ANIYAR }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isTavyaRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isAniyar = context.samjnas.any { it.samjna == Samjna.ANIYAR }
        val pratyayaTerm = if (isAniyar) {
            DerivationTerm(
                id = "aniyar_pratyaya",
                surface = "अनीयर्",
                kind = TermKind.PRATYAYA,
                itMarkers = emptySet(),
                upadesha = "अनीयर्",
                createdBySutra = sutra,
            )
        } else {
            DerivationTerm(
                id = "tavya_pratyaya",
                surface = "तव्यत्",
                kind = TermKind.PRATYAYA,
                itMarkers = setOf(ItMarker.T),
                upadesha = "तव्यत्",
                createdBySutra = sutra,
            )
        }

        return DerivationChange(
            state = context.copy(
                terms = context.terms + pratyayaTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.96 introduces suffix ${pratyayaTerm.upadesha}."
        )
    }
}
