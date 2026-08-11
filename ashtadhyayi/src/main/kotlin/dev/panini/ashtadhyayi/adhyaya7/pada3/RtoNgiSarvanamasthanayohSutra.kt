package dev.panini.ashtadhyayi.adhyaya7.pada3

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
 * 7.3.110: ṛto ṅi-sarvanāmasthānayoḥ.
 * Applies guṇa ('a' + 'r' = 'ar') to ṛ-ending stems ('pitṛ', 'mātṛ', 'dātṛ') before sarvanāmasthāna affixes or locative singular 'ṅi'.
 */
object RtoNgiSarvanamasthanayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.110",
    text = "ऋतो ङिसर्वनामस्थानयोः",
    hindiExplanation = "ऋकारान्त अङ्ग को गुण (अर्) होता है ङि और सर्वनामस्थान विभक्तियों के परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730110,
    role = SutraRole.Vidhi,
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

        val isRStem = stem.upadesha.endsWith("ृ") || stem.surface.endsWith("ृ")
        if (!isRStem) return false

        val isEligibleAffix = affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut", "sup-ngi") ||
            affix.upadesha in setOf("सुँ", "औ", "जस्", "अम्", "औट्", "ङि")
        return isEligibleAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = stem.surface.dropLast(1) + "अर्"

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.110: Applied guṇa 'ar' to ṛ-stem '${stem.surface}' before sarvanāmasthāna/ṅi (becoming $newSurface)."
        )
    }
}
