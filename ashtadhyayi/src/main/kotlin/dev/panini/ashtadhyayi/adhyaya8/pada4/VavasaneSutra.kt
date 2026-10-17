package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SpeechBoundary
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.carStopCounterpart
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.56: vāvasāne.
 * At an explicitly marked pause, a jhal sound is optionally replaced by a car sound (devoiced).
 * This makes the devoicing optional at the end of a sentence or pause.
 */
object VavasaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.56",
    text = "वावसाने",
    hindiExplanation = "अवसान (विराम) में झल् वर्णों के स्थान पर विकल्प से चर् आदेश होता है।",
    type = SutraType.VIBHASHA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840056,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.SANDHI,
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? {
        if (context.effectiveContext.finalBoundary != SpeechBoundary.AVASANA) return null
        if (context.stage != DerivationStage.PADA_FORMED && context.stage != DerivationStage.FINAL) return null
        val term = context.terms.lastOrNull { it.varnas.isNotEmpty() } ?: return null
        val source = term.varnas.last()
        val replacement = source.carStopCounterpart() ?: return null
        return term.takeIf { replacement != source && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, source) }
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = requireNotNull(target(context))
        val finalConsonant = lastTerm.varnas.last()
        val substitute = requireNotNull(finalConsonant.carStopCounterpart())

        return DerivationChange(
            state = context.replaceTermVarna(lastTerm.id, lastTerm.varnas.lastIndex, listOf(substitute), sutra)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.56: Optionally devoiced ${finalConsonant.devanagari} to ${substitute.devanagari} at avasāna."
        )
    }

    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        apply(state),
        DerivationChange(state, "8.4.56: Declined optional devoicing at avasāna.", applied = false)
    )

}
