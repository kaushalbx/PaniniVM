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

/** 3.1.81: क्र्यादिभ्यः श्ना. */
object KryadibhyahShnaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.81",
    text = "क्र्यादिभ्यः श्ना",
    hindiExplanation = "क्र्यादि-गण के धातुओं से परे श्ना विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310081,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == DhatuGana.KRYADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्ना" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shna = DerivationTerm(
            "shna", "श्ना", TermKind.PRATYAYA,
            upadesha = "श्ना", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            context.insertBeforeTingOrLingAugment(shna),
            "3.1.81 introduces raw श्ना; its surviving ना/नी/न् is determined by subsequent rules.",
        )
    }
}
