package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val base = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        return base.kind == TermKind.PRATIPADIKA && base.varnas.takeLast(vimshati.size) == vimshati &&
            suffix.kind == TermKind.PRATYAYA && suffix.upadesha == "डट्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val base = context.terms[context.terms.lastIndex - 1]
        val result = base.varnas.dropLast(2)
        return DerivationChange(
            context.substituteTermVarnas(base.id, result, Vyanjana.TA, emptyList(), sutra),
            "$text deletes final ti from ${base.surface}.",
        )
    }

    private val vimshati = listOf(
        Vyanjana.VA, Svara.I, Ayogavaha.ANUSVARA, Vyanjana.SHA, Svara.A, Vyanjana.TA, Svara.I,
    )
}
