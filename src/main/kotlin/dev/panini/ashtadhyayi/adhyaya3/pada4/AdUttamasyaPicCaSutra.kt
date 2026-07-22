package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.core.Purusha
import dev.panini.derivation.*
import dev.panini.sutra.*

/** 3.4.92: आडुत्तमस्य पिच्च. Adds आट् to first-person LOT endings. */
object AdUttamasyaPicCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.92", text = "आडुत्तमस्य पिच्च",
    hindiExplanation = "लोट् के उत्तमपुरुष प्रत्ययों के आदि में आट् आगम होता है और वे पित् माने जाते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340092,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LOT &&
            context.effectiveContext.rupa.purusha == Purusha.UTTAMA &&
            ending.upadesha in setOf("इट्", "वहि", "महिङ्") &&
            context.allEffectiveTerms.none { it.id == "lot-at-agama" } &&
            ending.surface in setOf("ए", "वहे", "महे")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val agama = DerivationTerm("lot-at-agama", "", TermKind.AGAMA, upadesha = "आट्")
        val strongKryadiStem = context.terms.any { it.id == "shna" }
        val augmented = when (ending.upadesha) {
            "वहि", "महिङ्" -> if (strongKryadiStem) ending else ending.copy(surface = "आ${ending.surface}")
            else -> ending
        }
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + agama + augmented),
            "3.4.92 supplies आट् to the LOT first-person ending and makes it pit.",
        )
    }
}
