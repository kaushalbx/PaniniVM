package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.*
import dev.sanskrit.sutra.*

/** 3.4.93: एत ऐ. Changes the final ए of first-person LOT endings to ऐ. */
object EtaAiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.93", text = "एत ऐ",
    hindiExplanation = "लोट् के उत्तमपुरुष प्रत्यय के एकार के स्थान पर ऐकार होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340093,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT &&
            context.allEffectiveTerms.any { it.id == "lot-at-agama" } &&
            ending.upadesha in setOf("इट्", "वहि", "महिङ्") &&
            (ending.surface == "ए" || ending.surface.endsWith("े"))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = if (ending.surface == "ए") "ऐ" else ending.surface.dropLast(1) + "ै"
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = replacement)),
            "3.4.93 replaces the ending's ए with ऐ in the LOT first person.",
        )
    }
}
