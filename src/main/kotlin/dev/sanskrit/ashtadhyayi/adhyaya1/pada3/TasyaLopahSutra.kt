package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.PRATYAYA_SELECTED || context.terms.any { it.itMarkers.isNotEmpty() }

    override fun apply(context: DerivationState): DerivationChange {
        val newTerms = context.terms.map { term ->
            if (term.itMarkers.isEmpty()) return@map term
            // A dhātu enters the derivation with its normalized mūla already
            // separated from the Dhātupāṭha upadeśa. Its recorded it-status
            // must not delete actual root sounds from that normalized surface.
            if (term.kind == TermKind.DHATU) return@map term.copy(itMarkers = emptySet())
            
            var newSurface = term.surface
            
            // 0. Handle vowel U marker (anunasika U in supi/ting)
            if (term.itMarkers.contains(ItMarker.U)) {
                newSurface = newSurface.replace("ुँ", "्").replace("ु", "्").replace("ँ", "")
                    .replace("ि", "्").replace("इ", "्")
                if (newSurface.endsWith("््")) {
                    newSurface = newSurface.dropLast(1)
                }
            }
            
            // 1. Handle final markers (Halantyam)
            if (term.itMarkers.contains(ItMarker.KIT)) {
                if (newSurface.endsWith('्') && newSurface.length >= 2) {
                    newSurface = newSurface.dropLast(2)
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
            
            term.copy(surface = newSurface, itMarkers = emptySet()) // Markers are consumed after lopa
        }
        
        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = DerivationStage.IT_PROCESSED
            ),
            explanation = "1.3.9: Performed lopa of it-marked sounds."
        )
    }
}
