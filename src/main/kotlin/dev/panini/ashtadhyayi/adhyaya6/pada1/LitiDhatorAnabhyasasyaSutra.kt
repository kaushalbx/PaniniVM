package dev.panini.ashtadhyayi.adhyaya6.pada1

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

/** 6.1.8: लिटि धातोरनभ्यासस्य. */
object LitiDhatorAnabhyasasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.8", text = "लिटि धातोरनभ्यासस्य",
    hindiExplanation = "लिट् परे होने पर अनभ्यास धातु का द्विर्वचन होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610008,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.terms.any { it.kind == TermKind.DHATU } &&
            context.terms.none { it.id == "abhyasa" } &&
            context.substitutions.none { it.sutra == "6.4.120" }

    override fun apply(context: DerivationState): DerivationChange {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val dhatu = context.terms[index]
        val abhyasa = DerivationTerm("abhyasa", dhatu.surface, TermKind.DHATU, upadesha = dhatu.upadesha)
        return DerivationChange(context.copy(terms = context.terms.take(index) + abhyasa + context.terms.drop(index)), "6.1.8 duplicates ${dhatu.surface} as the preliminary abhyāsa in लिट्.")
    }
}
