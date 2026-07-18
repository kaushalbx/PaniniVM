package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.85: व्यत्ययो बहुलम्. Models the attested Vedic मि -> नि variation in LET. */
object VyatyayoBahulamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.85",
    text = "व्यत्ययो बहुलम्",
    hindiExplanation = "छन्दस् में प्रत्ययों का व्यत्यय बहुल रूप से होता है; यहाँ लेट् के मि का नि होता है।",
    type = SutraType.VIBHASHA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310085,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LET &&
            ending.matchesUpadesha("मिप्") && ending.surface == "मि"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "नि")),
            "3.1.85 permits the Vedic नि ending in place of मि.",
        )
    }
}
