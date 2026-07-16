package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.LopaType
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 6.1.10: श्लौ. A root is duplicated when the following शप् has undergone ślu. */
object ShlauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.10",
    text = "श्लौ",
    hindiExplanation = "श्लु परे होने पर धातु का द्विर्वचन होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610010,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.JUHOTYADI &&
            context.droppedTerms.any { it.upadesha == "शप्" && it.deletionType == LopaType.SHLU } &&
            context.terms.none { it.id == "abhyasa" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val dhatu = context.terms[index]
        val source = if (dhatu.matchesUpadesha("हु")) "हु" else dhatu.surface
        val abhyasa = DerivationTerm("abhyasa", source, TermKind.DHATU, upadesha = dhatu.upadesha)
        return DerivationChange(
            context.copy(terms = context.terms.take(index) + abhyasa + context.terms.drop(index)),
            "6.1.10 duplicates ${dhatu.surface} before the ślu-elided vikaraṇa.",
        )
    }
}
