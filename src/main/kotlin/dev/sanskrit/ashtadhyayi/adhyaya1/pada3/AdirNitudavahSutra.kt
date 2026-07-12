package dev.sanskrit.ashtadhyayi.adhyaya1.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.3.5: ādir ñitūḍavaḥ.
 * Initial ñi, ṭu, and ḍu syllables in an upadeśa (usually dhātus) are it-markers.
 */
object AdirNitudavahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.5",
    text = "आदिर्ञिटूडवः",
    hindiExplanation = "उपदेश के आदि में स्थित ञि, टु और डु की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130005,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.INITIAL && context.terms.any { term ->
            term.kind == TermKind.DHATU && (
                term.surface.startsWith("ञि") || 
                term.surface.startsWith("टु") || 
                term.surface.startsWith("डु")
            )
        }

    override fun apply(context: DerivationState): DerivationChange {
        val newTerms = context.terms.map { term ->
            if (term.kind == TermKind.DHATU) {
                when {
                    term.surface.startsWith("ञि") -> term.copy(itMarkers = term.itMarkers + ItMarker.KIT) // Using KIT as proxy
                    term.surface.startsWith("टु") -> term.copy(itMarkers = term.itMarkers + ItMarker.T)
                    term.surface.startsWith("डु") -> term.copy(itMarkers = term.itMarkers + ItMarker.KIT)
                    else -> term
                }
            } else term
        }
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "1.3.5: Assigned it-status to initial ñi/ṭu/ḍu."
        )
    }
}
