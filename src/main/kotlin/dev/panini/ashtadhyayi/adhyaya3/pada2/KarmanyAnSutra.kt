package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.ItMarker
import dev.panini.core.Prayoga
import dev.panini.derivation.*
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.2.1: karmaṇy aṇ.
 * The suffix 'aṇ' is added to a root when it is in composition with a direct object (upapada).
 */
object KarmanyAnSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.1",
    text = "कर्मण्यण्",
    hindiExplanation = "कर्म उपपद होने पर धातु से 'अण्' प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320001,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("3.1.91")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("3.1.91" !in context.activeAdhikaras) return false

        // Match an agentive derivation with a root but no kṛt suffix yet.
        return HasMorphosyntax(prayoga = Prayoga.KARTARI).matches(context) &&
               context.terms.any { it.kind == TermKind.DHATU } &&
               context.allEffectiveTerms.none { it.kind == TermKind.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val an = DerivationTerm(
            id = "an-suffix",
            surface = "अण्",
            kind = TermKind.PRATYAYA,
            upadesha = "अण्",
            itMarkers = setOf(ItMarker.NG) // 'ṇ' is ṇit
        )
        return DerivationChange(
            state = context.addTerm(an).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            explanation = "3.2.1 selects the kṛt suffix 'aṇ'."
        )
    }
}
