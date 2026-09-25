package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.lastVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.93: एत ऐ. Changes the final ए of first-person LOT endings to ऐ. */
object EtaAiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.93", text = "एत ऐ",
    hindiExplanation = "लोट् के उत्तमपुरुष प्रत्यय के एकार के स्थान पर ऐकार होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340093,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    blocks = setOf("6.1.77", "6.1.78", "6.1.87", "6.1.88", "6.1.97", "6.1.101"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT &&
            context.allEffectiveTerms.any { it.id == "lot-at-agama" || "3.4.92" in it.establishedBySutras } &&
            ending.upadesha in setOf("इट्", "वहि", "महिङ्") &&
            ending.surface.lastVarna() == Svara.E
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val varnas = ending.varnas
        val replacedPrefix = if (varnas.takeLast(2) == listOf(Svara.AA, Svara.E)) {
            varnas.dropLast(2)
        } else varnas.dropLast(1)
        val replacement = (replacedPrefix + Svara.AI).toDevanagari()
        val replaced = context.replaceWholeAffix(
            ending.id,
            replacement,
            sutra,
            dev.panini.derivation.WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()),
        )
        return DerivationChange(
            replaced.removeTerm("lot-at-agama", sutra),
            "3.4.93 replaces the ending's ए with ऐ in the LOT first person.",
        )
    }
}
