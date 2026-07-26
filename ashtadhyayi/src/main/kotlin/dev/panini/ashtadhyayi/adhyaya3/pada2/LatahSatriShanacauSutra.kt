package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.124 लटः शतृशानचावप्रथमासमानाधिकरणे.
 * Prescribes śatṛ and śānac active/middle participle affixes in Laṭ.
 */
object LatahSatriShanacauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.124", text = "लटः शतृशानचावप्रथमासमानाधिकरणे",
    hindiExplanation = "अप्रथमा समानाधिकरण में लट् लकार के स्थान पर 'शतृ' तथा 'शानच्' प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320124,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LAT &&
        context.effectiveContext.requestedMeaning != null &&
        context.allEffectiveTerms.none { it.upadesha in setOf("शतृ", "शानच्") }

    override fun apply(context: DerivationState): DerivationChange {
        val satri = DerivationTerm("satri", "अत्", TermKind.PRATYAYA, upadesha = "शतृ")
        return DerivationChange(
            state = context.addTerm(satri),
            explanation = "3.2.124 prescribes शतृ / शानच् in place of लट्.",
        )
    }
}
