package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 5.2.55: त्रेः सम्प्रसारणं च। */
object TresSamprasaranamCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.55", text = "त्रेः सम्प्रसारणं च", hindiExplanation = "त्रि से पूरणार्थे तीय तथा सम्प्रसारण होता है।",
    type = SutraType.APAVADA, chapter = 5, pada = 2, optional = false, kramaValue = 520055,
    role = SutraRole.Apavada, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    blocks = setOf("5.2.48"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.samjnas.any { it.samjna == Samjna.PURANA } &&
        context.terms.singleOrNull()?.surface == "त्रि" && context.terms.none { it.upadesha == "तीय" }
    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.single()
        val tiya = DerivationTerm(
            id = "purana_tiya",
            surface = "तीय",
            kind = TermKind.PRATYAYA,
            upadesha = "तीय",
            createdBySutra = sutra,
        )
        val changedBase = target.copy(surface = "तृ")
        return DerivationChange(
            context.replaceTerm(target.id, changedBase).copy(terms = listOf(changedBase, tiya)),
            "$text: त्रि का सम्प्रसारण तृ और तीय प्रत्यय।",
        )
    }
}
