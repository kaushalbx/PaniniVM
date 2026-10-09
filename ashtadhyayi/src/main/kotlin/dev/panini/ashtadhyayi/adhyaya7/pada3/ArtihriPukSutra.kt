package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.SanadiAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.sutra.*

/** 7.3.36: the ā-final portion of अर्तिह्रीव्लीरीक्नूयीक्ष्माय्यातां पुङ्णौ. */
object ArtihriPukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.36", text = "अर्तिह्रीव्लीरीक्नूयीक्ष्माय्यातां पुङ्णौ",
    hindiExplanation = "आकारान्त धातु को णिच् परे पुक् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730036, role = SutraRole.Vidhi, action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { it.matchesAffix(SanadiAffix.NIC) } &&
            context.terms.any { eligibleRoot(it) } &&
            context.allEffectiveTerms.none { it.createdBySutra == sutra }

    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { eligibleRoot(it) }
        val augment = DerivationTerm(
            id = "${root.id}-puk", surface = "पुँक्", upadesha = "पुँक्",
            kind = TermKind.AGAMA, createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = root.id,
        )
        return DerivationChange(context.addTerm(augment), "7.3.36 introduces raw पुक् for placement at the end of the ā-final root before णिच्.")
    }

    private fun eligibleRoot(term: DerivationTerm): Boolean =
        term.kind == TermKind.DHATU && term.varnas.lastOrNull() == Svara.AA &&
            DhatuPatha.all.any { it.upadesha == term.upadesha && it.sourceVarnas.lastOrNull() == Svara.AA }
}
