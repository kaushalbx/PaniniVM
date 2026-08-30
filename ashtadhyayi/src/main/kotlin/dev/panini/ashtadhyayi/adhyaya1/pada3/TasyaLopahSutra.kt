package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.9: tasya lopaḥ.
 * The sound designated as 'it' disappears.
 * This implementation generalizes the removal of characters marked by preceding rules.
 */
object TasyaLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.9",
    text = "तस्य लोपः",
    hindiExplanation = "जिस वर्ण की इत् संज्ञा हुई है, उसका लोप (अदर्शन) होता है।",
    type = SutraType.NITYA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130009,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.PRATYAYA_SELECTED && context.terms.none { it.itProcessingPending }) {
            return true
        }
        return context.terms.filter { it.itProcessingPending }.any { term ->
            term.itDesignations.isNotEmpty() ||
                (term.itProcessingPhase == dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA &&
                    term.itMarkers.isEmpty())
        } || (context.stage >= DerivationStage.ANGAKARYA &&
            context.terms.any { it.deferredItDesignations.isNotEmpty() })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val pendingTargets = context.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }
        val newTerms = context.terms.map { term ->
            if (pendingTargets.isNotEmpty() && term.id !in pendingTargets) return@map term
            if (term.itProcessingPhase == dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA &&
                term.itMarkers.isEmpty() && term.itDesignations.isEmpty()
            ) {
                return@map term.copy(itProcessingPhase = dev.panini.derivation.ItProcessingPhase.PROCESSED)
            }
            val exactDesignations = term.itDesignations + term.deferredItDesignations
            if (exactDesignations.isNotEmpty()) {
                val designatedMarkers = exactDesignations.mapTo(mutableSetOf()) { it.marker }
                val processed = exactDesignations.sortedByDescending { it.start }.fold(term.surface) { surface, designation ->
                    val recordedText = designation.designatedText
                    val recordedSpanStillExists = designation.endExclusive <= surface.length &&
                        (recordedText == null || surface.substring(designation.start, designation.endExclusive) == recordedText)
                    val relocatedStart = if (!recordedSpanStillExists && recordedText != null) {
                        val first = surface.indexOf(recordedText)
                        if (first >= 0 && first == surface.lastIndexOf(recordedText)) first else -1
                    } else {
                        designation.start
                    }
                    if (relocatedStart >= 0 && (recordedSpanStillExists || recordedText != null)) {
                        val relocatedEnd = if (recordedText == null) designation.endExclusive else relocatedStart + recordedText.length
                        surface.replaceRange(relocatedStart, relocatedEnd, designation.replacementAfterLopa)
                    } else {
                        surface
                    }
                }
                return@map term.copy(
                    surface = processed,
                    itMarkers = emptySet(),
                    itDesignations = emptyList(),
                    deferredItDesignations = emptyList(),
                    itProcessingPhase = dev.panini.derivation.ItProcessingPhase.PROCESSED,
                    sthaniProps = dev.panini.derivation.SthaniProperties(
                        upadesha = term.sthaniProps?.upadesha ?: term.upadesha,
                        itMarkers = term.sthaniProps?.itMarkers.orEmpty() + term.itMarkers + designatedMarkers,
                    ),
                )
            }
            term
        }

        val nextStage = if (context.stage.ordinal > DerivationStage.IT_PROCESSED.ordinal) {
            context.stage
        } else {
            DerivationStage.IT_PROCESSED
        }

        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = nextStage
            ),
            explanation = "1.3.9: Performed lopa of it-marked sounds."
        )
    }
}
