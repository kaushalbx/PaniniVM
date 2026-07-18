package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.derivation.TingAffix
import dev.panini.derivation.VarnaSubstitution
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
        if (ending.upadesha != TingAffix.DHVAM.upadesha || !ending.surface.startsWith('ध')) return false
        val endingIndex = context.terms.lastIndex
        val angaSurface = context.copy(terms = context.terms.take(endingIndex)).surface
        return angaSurface.endsWith("षी")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "ढ" + ending.surface.drop(1)))
                .addSubstitution(VarnaSubstitution(ending.id, 'ध', "ढ", sutra)),
            "8.3.78 substitutes ढ् for the ध् of षीध्वम् after the aṅga.",
        )
    }
}
