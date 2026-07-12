package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 1.3.3: hal antyam. The final consonant of an upadeśa is an it-marker. */
object HalantyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.3",
    text = "हलन्त्यम्",
    hindiExplanation = "उपदेश के अन्त में आने वाला हल् (व्यञ्जन) इत् संज्ञक होता है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130003,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        
        return context.terms.any { term ->
            val lastChar = term.surface.lastOrNull() ?: return@any false
            // Check if it's a consonant and not already marked (simplified marker logic)
            Varnamala.isConsonant(lastChar) && term.itMarkers.isEmpty() 
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val newTerms = context.terms.map { term ->
            val lastChar = term.surface.lastOrNull()
            if (lastChar != null && Varnamala.isConsonant(lastChar) && term.itMarkers.isEmpty()) {
                // Map the specific consonant to a generic marker for now, 
                // or extend ItMarker to store the actual character.
                term.copy(itMarkers = term.itMarkers + ItMarker.KIT) 
            } else {
                term
            }
        }
        
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "1.3.3: Assigned it-status to final consonants."
        )
    }
}
