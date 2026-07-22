package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 6.4.142: ति विंशतेर्डिति — final ति of विंशति is deleted before a डित् suffix. */
object TiVimshaterDitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.142",
    text = "ति विंशतेर्डिति",
    hindiExplanation = "डित् प्रत्यय परे होने पर विंशति के अन्तिम ति का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640142,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val base = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        return base.kind == TermKind.PRATIPADIKA && base.surface.endsWith("विंशति") &&
            suffix.kind == TermKind.PRATYAYA && suffix.upadesha == "डट्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val base = context.terms[context.terms.lastIndex - 1]
        val changed = base.copy(surface = base.surface.dropLast(2))
        return DerivationChange(
            context.replaceTerm(base.id, changed),
            "$text: ${base.surface} → ${changed.surface}।",
        )
    }
}
