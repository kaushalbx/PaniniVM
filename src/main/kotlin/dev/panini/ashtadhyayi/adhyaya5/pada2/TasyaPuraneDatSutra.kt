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
import dev.panini.sutra.SutraType

/** 5.2.48: तस्य पूरणे डटौ */
object TasyaPuraneDatSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.48",
    text = "तस्य पूरणे डट्",
    hindiExplanation = "संख्यावाची प्रातिपदिक से पूरण (क्रमसूचक) अर्थ में डट् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520048,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val hasPuranaRequest = context.samjnas.any { it.samjna == Samjna.PURANA }
        val isAlreadyApplied = context.terms.any { it.surface == "अ" || it.upadesha == "डट्" }
        val supportedBase = lastTerm.surface in setOf(
            "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश",
            "सप्तदश", "अष्टादश", "नवदश",
        ) || lastTerm.upadesha in setOf(
            "विंशति", "त्रिंशत्", "चत्वारिंशत्", "पञ्चाशत्",
            "षष्टि", "सप्तति", "अशीति", "नवति",
            "शत", "सहस्र", "अयुत", "लक्ष", "प्रयुत", "कोटि",
        )
        return hasPuranaRequest && !isAlreadyApplied && supportedBase
    }

    override fun apply(context: DerivationState): DerivationChange {
        val datTerm = DerivationTerm(
            id = "purana_dat",
            // After its it-markers are removed, डट् contributes the inherent a already
            // represented by the final consonant of the Devanāgarī base.
            surface = "",
            kind = TermKind.PRATYAYA,
            upadesha = "डट्"
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + datTerm),
            explanation = "$text: added suffix डट् (अ)"
        )
    }
}
