package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
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
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PRATYAYA_SELECTED || context.terms.any { it.itMarkers.isNotEmpty() }

    override fun apply(context: DerivationState): DerivationChange {
        val pendingTargets = context.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }
        val newTerms = context.terms.map { term ->
            if (term.itMarkers.isEmpty()) return@map term
            if (pendingTargets.isNotEmpty() && term.id !in pendingTargets) return@map term
            if (term.itProcessingPending && term.itDesignations.isNotEmpty()) {
                val processed = term.itDesignations.sortedByDescending { it.start }.fold(term.surface) { surface, designation ->
                    surface.replaceRange(designation.start, designation.endExclusive, designation.replacementAfterLopa)
                }
                return@map term.copy(
                    surface = processed,
                    itMarkers = emptySet(),
                    itDesignations = emptyList(),
                    itProcessingPending = false,
                    sthaniProps = dev.panini.derivation.SthaniProperties(
                        upadesha = term.sthaniProps?.upadesha ?: term.upadesha,
                        itMarkers = term.sthaniProps?.itMarkers.orEmpty() + term.itMarkers,
                    ),
                )
            }
            // Newly introduced upadeśas are processed only from exact spans
            // assigned by 1.3.2–1.3.8. Never infer a deletion from a marker.
            if (term.itProcessingPending) return@map term
            // A dhātu enters the derivation with its normalized mūla already
            // separated from the Dhātupāṭha upadeśa. Its recorded it-status
            // must not delete actual root sounds from that normalized surface.
            if (term.kind == TermKind.DHATU && !term.itProcessingPending) {
                return@map term.copy(itMarkers = emptySet())
            }

            var newSurface = term.surface

            // 0. Handle vowel U marker (anunasika U in supi/ting)
            if (term.itMarkers.contains(ItMarker.U)) {
                newSurface = newSurface.replace("ुँ", "्").replace("ु", "्").replace("ँ", "")
                    .replace("ि", "्").replace("इ", "्")
                if (newSurface.endsWith("््")) {
                    newSurface = newSurface.dropLast(1)
                }
            }

            // 1. Handle final markers (Halantyam 1.3.3, respecting 1.3.4 Na vibhaktau tusmāḥ)
            if (term.itMarkers.any { it != ItMarker.U }) {
                val finalItEndings = listOf("क्", "प्", "ङ्", "ण्", "श्", "ट्", "ञ्", "र्", "ल्", "च्")
                for (ending in finalItEndings) {
                    if (newSurface.endsWith(ending)) {
                        newSurface = newSurface.dropLast(2)
                        break
                    }
                }
            }

            // 2. Handle initial markers (Chutu, Lashakvataddhite, etc.)
            if (term.itMarkers.any { it == ItMarker.J || it == ItMarker.T || it == ItMarker.SH || it == ItMarker.KIT || it == ItMarker.NGIT }) {
                if (newSurface.isNotEmpty() && (newSurface.first() in setOf('च', 'छ', 'ज', 'झ', 'ञ', 'ट', 'ठ', 'ड', 'ढ', 'ण', 'ल', 'श', 'क', 'ख', 'ग', 'घ', 'ङ'))) {
                    val hasVirama = newSurface.getOrNull(1) == '्'
                    if (hasVirama) {
                        newSurface = newSurface.drop(2)
        } else if (term.upadesha == "जस्" ||
            (term.upadesha == "शस्" && context.effectiveContext.rupa.linga == Linga.PUMS)
        ) {
                        // The initial it of जस्/शस् is removed; the following अ belongs to the
                        // surviving suffix and licenses the later यण् sandhi.
                        newSurface = "अ" + newSurface.drop(1)
                    } else {
                        // A non-virāma initial it consonant is removed; it does not
                        // contribute an inherent अ to the remaining pratyaya.
                        newSurface = newSurface.drop(1)
                    }
                }
            }

            term.copy(
                surface = newSurface,
                itMarkers = emptySet(),
                itDesignations = emptyList(),
                itProcessingPending = false,
            ) // Markers are consumed after lopa
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
