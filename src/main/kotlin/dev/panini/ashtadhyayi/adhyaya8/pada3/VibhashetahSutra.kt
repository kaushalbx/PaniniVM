package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 8.3.79: विभाषेटः. */
object VibhashetahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.79", text = "विभाषेटः",
    hindiExplanation = "इट् के बाद लुङ् के धकार को विकल्प से ढकार आदेश होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830079,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LUNG) return false
        if (context.allEffectiveTerms.none { it.id == "it-agama" }) return false
        val ending = context.terms.lastOrNull() ?: return false
        return ending.upadesha == "ध्वम्" && ending.surface.startsWith("ध") &&
            context.droppedTerms.any { it.upadesha == "सिच्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "ढ" + ending.surface.drop(1))),
            "8.3.79 optionally substitutes ढ् for the LUNG ending's ध् after इट्.",
        )
    }
}
