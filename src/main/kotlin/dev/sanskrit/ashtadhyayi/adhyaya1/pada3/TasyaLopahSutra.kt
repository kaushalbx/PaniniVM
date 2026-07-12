package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.shiksha.Varnamala
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
        context.stage == DerivationStage.PRATYAYA_SELECTED && 
        context.terms.any { it.itMarkers.isNotEmpty() }

    override fun apply(context: DerivationState): DerivationChange {
        val newTerms = context.terms.map { term ->
            if (term.itMarkers.isEmpty()) return@map term
            
            var newSurface = term.surface
            
            // This is a simplified logic: if it has markers, we process the surface.
            // Traditionally, it-markers are always at the boundaries (except vowels in roots).
            
            // 1. Handle final markers (Halantyam)
            while (newSurface.isNotEmpty() && Varnamala.isConsonant(newSurface.last())) {
                newSurface = newSurface.dropLast(1)
                // In Devanagari strings, a consonant might be followed by a virama, or be a full syllable.
                // If it ends in a virama, we need to drop that too.
                if (newSurface.endsWith('्')) {
                    newSurface = newSurface.dropLast(1)
                }
            }
            
            // 2. Handle initial markers (Chutu, Lashakvataddhite)
            // (Similar logic could be added for startsWith)
            
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
