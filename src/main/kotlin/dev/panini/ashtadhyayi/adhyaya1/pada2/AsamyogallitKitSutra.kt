package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 1.2.5: असंयोगाल्लिट् कित्. */
object AsamyogallitKitSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.5",
    text = "असंयोगाल्लिट् कित्",
    hindiExplanation = "संयोगान्त से भिन्न धातु के बाद अपित् लिट् प्रत्यय को कित् माना जाता है।",
    type = SutraType.ATIDESHA,
    chapter = 1,
    pada = 2,
    optional = false,
    kramaValue = 120005,
    role = SutraRole.Atidesha,
    action = SutraAction.ATIDESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LIT) return false
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        val affix = context.terms.lastOrNull()?.takeIf { it.kind == TermKind.PRATYAYA } ?: return false
        val isPit = affix.id in setOf("ting-tip", "ting-sip", "ting-mip")
        return !endsInConsonantCluster(dhatu.surface) &&
            affix.id.startsWith("ting-") &&
            !isPit &&
            sutra !in affix.establishedBySutras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            context.replaceTerm(
                affix.id,
                affix.copy(establishedBySutras = affix.establishedBySutras + sutra),
            ),
            "1.2.5 treats the non-pit LIṬ affix after a non-cluster-final root as kit.",
        )
    }

    private fun endsInConsonantCluster(surface: String): Boolean =
        surface.endsWith('्') && surface.getOrNull(surface.lastIndex - 2) == '्'
}
