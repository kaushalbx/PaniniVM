package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.DhatuGana
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.78: रुधादिभ्यः श्नम्. The nasal vikaraṇa is infixed after the root's final vowel. */
object RudhadibhyahShnamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.78",
    text = "रुधादिभ्यः श्नम्",
    hindiExplanation = "रुधादि-गण के धातुओं से परे श्नम् विकरण होता है और उसका नकार अन्तिम स्वर के बाद आता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310078,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DHATU,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return !context.hasSanadyantaDhatu() &&
            dhatu.gana == DhatuGana.RUDHADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्नम्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val shnam = DerivationTerm(
            "shnam", "श्नम्", TermKind.PRATYAYA,
            upadesha = "श्नम्", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = dhatu.id,
        )
        return DerivationChange(
            context.insertBeforeTingOrLingAugment(shnam),
            "3.1.78 introduces raw श्नम् for placement after the final vowel by 1.1.47.",
        )
    }
}
