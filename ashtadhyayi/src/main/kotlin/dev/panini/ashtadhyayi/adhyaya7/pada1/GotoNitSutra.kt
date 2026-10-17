package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.hasCurrentAffix
import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.90: goto ṇit.
 * Sarvanāmasthāna case affixes following the 'go' stem are treated as ṇit (causing vṛddhi 'au' per 7.2.115).
 */
object GotoNitSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.90",
    text = "गोतो णित्",
    hindiExplanation = "गो अङ्ग से उत्तर सम्बुद्धि-भिन्न सर्वनामस्थान प्रत्यय णित्-वत् होते हैं (ओकार -> औकार)।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710090,
    role = SutraRole.Atidesha,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // This handler currently performs the resulting vṛddhi as well as ṇit
        // treatment. Before sup-am, 6.1.93 is the mandatory exception to that
        // vṛddhi; do not destroy its o before the affix's it-processing finishes.
        if (affix.hasCurrentAffix(SupAffix.AM)) return false

        if (stem.varnas.lastOrNull() != Svara.O) return false
        val isGo = stem.upadesha == "गो" || stem.varnas == listOf(Vyanjana.GA, Svara.O)
        if (!isGo) return false

        val isSarvanamasthana = listOf(SupAffix.SU, SupAffix.AU, SupAffix.JAS, SupAffix.AM, SupAffix.AUT)
            .any(affix::hasCurrentAffix)
        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTermVarna(
                stem.id, stem.varnas.lastIndex, listOf(Svara.AU), sutra,
            )
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.90 & 7.2.115: Applied vṛddhi 'au' to 'go' stem before ṇit-sarvanāmasthāna."
        )
    }
}
