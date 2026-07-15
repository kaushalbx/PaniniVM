package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 6.1.8: लिटि धातोरनभ्यासस्य. */
object LitiDhatorAnabhyasasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.8", text = "लिटि धातोरनभ्यासस्य",
    hindiExplanation = "लिट् परे होने पर अनभ्यास धातु का द्विर्वचन होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610008,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT && context.terms.any { it.kind == TermKind.DHATU } && context.terms.none { it.id == "abhyasa" }

    override fun apply(context: DerivationState): DerivationChange {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val dhatu = context.terms[index]
        val abhyasa = DerivationTerm("abhyasa", dhatu.surface, TermKind.DHATU, upadesha = dhatu.upadesha)
        return DerivationChange(context.copy(terms = context.terms.take(index) + abhyasa + context.terms.drop(index)), "6.1.8 duplicates ${dhatu.surface} as the preliminary abhyāsa in लिट्.")
    }
}
