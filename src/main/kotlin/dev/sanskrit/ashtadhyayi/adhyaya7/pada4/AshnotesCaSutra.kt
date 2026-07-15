package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

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

/** 7.4.72: अश्नोतेश्च. */
object AshnotesCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.72", text = "अश्नोतेश्च",
    hindiExplanation = "लिट् में दीर्घ हुए अभ्यास के बाद अश्नोति धातु से पहले नुट् का आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740072,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasaIndex = context.terms.indexOfFirst { it.id == "abhyasa" }
        val dhatu = context.terms.drop(abhyasaIndex + 1).firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            abhyasaIndex >= 0 && context.terms[abhyasaIndex].surface.startsWith('आ') &&
            dhatu.matchesUpadesha("अश्") && context.terms.none { it.id == "nut" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasaIndex = context.terms.indexOfFirst { it.id == "abhyasa" }
        val nut = DerivationTerm("nut", "न्", TermKind.AGAMA, upadesha = "नुट्")
        return DerivationChange(
            context.copy(terms = context.terms.take(abhyasaIndex + 1) + nut + context.terms.drop(abhyasaIndex + 1)),
            "7.4.72 inserts the effective न् of नुट् after the lengthened abhyāsa before अश्नोति.",
        )
    }
}
