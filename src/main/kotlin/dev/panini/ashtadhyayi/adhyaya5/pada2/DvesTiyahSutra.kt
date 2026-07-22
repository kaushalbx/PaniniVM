package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.54: द्वेस्तीयः। */
object DvesTiyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.54", text = "द्वेस्तीयः", hindiExplanation = "द्वि से पूरणार्थे तीय प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 5, pada = 2, optional = false, kramaValue = 520054,
    role = SutraRole.Apavada, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    blocks = setOf("5.2.48"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.samjnas.any { it.samjna == Samjna.PURANA } &&
        context.terms.singleOrNull()?.surface == "द्वि"
    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.single()
        return DerivationChange(context.replaceTerm(target.id, target.copy(surface = "द्वितीय")), "$text: द्वि → द्वितीय।")
    }
}
