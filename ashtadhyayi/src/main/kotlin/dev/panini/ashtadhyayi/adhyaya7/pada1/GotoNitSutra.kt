package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
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

        val isGo = stem.upadesha == "गो" || stem.surface == "गो"
        if (!isGo) return false

        val isSarvanamasthana = affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut") ||
            affix.upadesha in setOf("सुँ", "औ", "जस्", "अम्", "औट्")
        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = "गौ"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.90 & 7.2.115: Applied vṛddhi 'au' to 'go' stem before ṇit-sarvanāmasthāna."
        )
    }
}
