package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val dhatu = context.terms.drop(abhyasaIndex + 1).first { it.kind == TermKind.DHATU }
        val nut = DerivationTerm(
            "nut", "नुट्", TermKind.AGAMA,
            upadesha = "नुट्", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = dhatu.id, mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            context.copy(terms = context.terms.take(abhyasaIndex + 1) + nut + context.terms.drop(abhyasaIndex + 1)),
            "7.4.72 introduces raw नुट् after the lengthened abhyāsa, targeted at the beginning of अश्नोति.",
        )
    }
}
