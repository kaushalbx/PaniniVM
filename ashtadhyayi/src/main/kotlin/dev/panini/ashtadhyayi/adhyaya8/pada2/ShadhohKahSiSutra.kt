package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 8.2.41: षढोः कः सि. ष् or ढ् is replaced by क् before स्. */
object ShadhohKahSiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.41",
    text = "षढोः कः सि",
    hindiExplanation = "स् परे होने पर ष् अथवा ढ् के स्थान पर क् होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820041,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = targetIndex(context) >= 0

    override fun apply(context: DerivationState): DerivationChange {
        val index = targetIndex(context)
        val target = context.terms[index]
        val source = target.varnas.last()
        val replacement = target.varnas.dropLast(1) + Vyanjana.KA
        return DerivationChange(
            context.substituteTermVarnas(target.id, replacement, source, listOf(Vyanjana.KA), sutra),
            "8.2.41 substitutes क् for $source before स्.",
        )
    }

    private fun targetIndex(context: DerivationState): Int =
        (0 until context.terms.lastIndex).firstOrNull { index ->
            val left = context.terms[index].varnas
            val right = context.terms[index + 1].varnas
            left.lastOrNull() in setOf(Vyanjana.SSA, Vyanjana.DDHA) && right.firstOrNull() == Vyanjana.SA
        } ?: -1
}
