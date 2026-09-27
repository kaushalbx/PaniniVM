package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 8.3.78: इणः षीध्वंलुङ्लिटां धोऽङ्गात्. */
object InahShidhvamLunglitamDhoAngatSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.78",
    text = "इणः षीध्वंलुङ्लिटां धोऽङ्गात्",
    hindiExplanation = "इण्-अन्त अङ्ग के बाद षीध्वम् के ध् को ढ् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830078,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LING) return false
        val ending = context.terms.lastOrNull() ?: return false
        if (ending.upadesha != TingAffix.DHVAM.upadesha || ending.varnas.firstOrNull() != Vyanjana.DHA) return false
        val endingIndex = context.terms.lastIndex
        val angaVarnas = context.terms.take(endingIndex).flatMap { it.varnas }
        return angaVarnas.takeLast(2) == listOf(Vyanjana.SSA, Svara.II)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.substituteTermVarnas(
                ending.id, listOf(Vyanjana.DDHA) + ending.varnas.drop(1),
                Vyanjana.DHA, listOf(Vyanjana.DDHA), sutra,
            ),
            "8.3.78 substitutes ढ् for the ध् of षीध्वम् after the aṅga.",
        )
    }
}
