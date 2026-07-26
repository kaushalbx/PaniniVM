package dev.panini.ashtadhyayi.adhyaya3.pada2

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
 * 3.2.124: लटः शतृशानचावप्रथमासमानाधिकरणे.
 * Substitutes 'शतृ' (at -> अत् / अन्त्) for Parasmaipada and 'शानच्' (ānac -> मान / आन) for Ātmanepada in Laṭ.
 */
object LatahSatrsanacauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.124",
    text = "लटः शतृशानचावप्रथमासमानाधिकरणे",
    hindiExplanation = "लटः के स्थान पर शतृ और शानच् प्रत्यय अप्रथमा समानाधिकरण विषय में होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320124,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isParticipleRequested = context.samjnas.any { it.samjna == Samjna.SATR || it.samjna == Samjna.SANAC }
        val hasDhatu = context.terms.any { it.kind == TermKind.DHATU }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isParticipleRequested && hasDhatu && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isSanac = context.samjnas.any { it.samjna == Samjna.SANAC }
        val pratyayaTerm = if (isSanac) {
            DerivationTerm(
                id = "sanac_pratyaya",
                surface = "मान",
                kind = TermKind.PRATYAYA,
                itMarkers = setOf(ItMarker.SH),
                upadesha = "शानच्",
                createdBySutra = sutra,
            )
        } else {
            DerivationTerm(
                id = "satr_pratyaya",
                surface = "अत्",
                kind = TermKind.PRATYAYA,
                itMarkers = setOf(ItMarker.SH),
                upadesha = "शतृ",
                createdBySutra = sutra,
            )
        }
        return DerivationChange(
            state = context.copy(
                terms = context.terms + pratyayaTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.2.124 introduces present participle affix ${pratyayaTerm.upadesha} (${pratyayaTerm.surface})."
        )
    }
}
