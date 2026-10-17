package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.core.Vacana
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** Semantic plurality is retained even after the sup has been incorporated. */
object EtaIdBahuvacaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.81", text = "एत ईद्बहुवचने",
    hindiExplanation = "बह्वर्थोक्ति में अदस् के दकार से परे एकार को ईकार और दकार को मकार आदेश होता है।",
    type = SutraType.APAVADA, chapter = 8, pada = 2, optional = false, kramaValue = 820081,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    blocks = setOf("8.2.80"),
), DerivationSutra {
    private fun target(context: DerivationState): AdasMutvaTarget? {
        if (context.effectiveContext.rupa.vacana != Vacana.BAHUVACANA) return null
        return adasMutvaTarget(context)?.takeIf { it.stem.varnas[it.dIndex + 1] == Svara.E }
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (stem, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarnaOccurrences(stem.id,
            mapOf(index to Vyanjana.MA, index + 1 to Svara.II), sutra),
            "8.2.81 replaces adas d with m and its following e with ī in semantic plurality, overriding the general u grade.")
    }
}
