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

/** 5.2.54: द्वेस्तीयः। */
object DvesTiyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.54", text = "द्वेस्तीयः", hindiExplanation = "द्वि से पूरणार्थे तीय प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 5, pada = 2, optional = false, kramaValue = 520054,
    role = SutraRole.Apavada, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.2.48"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.samjnas.any { it.samjna == Samjna.PURANA } &&
        context.terms.singleOrNull()?.surface == "द्वि" && context.terms.none { it.upadesha == "तीय" }
    override fun apply(context: DerivationState): DerivationChange {
        val tiya = DerivationTerm(
            id = "purana_tiya",
            surface = "तीय",
            kind = TermKind.PRATYAYA,
            upadesha = "तीय",
            createdBySutra = sutra,
        )
        return DerivationChange(context.copy(terms = context.terms + tiya), "$text: द्वि के बाद तीय प्रत्यय।")
    }
}
