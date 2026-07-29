package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.ashtadhyayi.runtime.ContextualSamjnaSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.ContextualSamjnaAssignmentArtha
import dev.panini.sutra.SamjnaAssignmentTarget
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
), DerivationSutra, ContextualSamjnaSutra {
    override val artha = ContextualSamjnaAssignmentArtha(
        target = SamjnaAssignmentTarget.UPADESHA_FINAL_CONSONANT,
        samjna = Samjna.IT,
    )

    override fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED) return false

        return state.terms.any { term ->
            if (term.kind == TermKind.PRATIPADIKA) return@any false
            // These āgamas are already resolved to their effective surfaces;
            // their surviving final consonants are not new it-markers.
            if (term.id in setOf("siyut", "yasut", "vuk", "nic")) return@any false
            val last = term.surface.lastOrNull() ?: return@any false
            if (last != '्' || term.surface.length < 2) return@any false
            val lastChar = term.surface[term.surface.length - 2]
            Varnamala.isConsonant(lastChar) && term.itMarkers.isEmpty()
        }
    }

    override fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATIPADIKA) return@map term
            if (term.id in setOf("siyut", "yasut", "vuk", "nic")) return@map term
            val last = term.surface.lastOrNull()
            if (last == '्' && term.surface.length >= 2) {
                val lastChar = term.surface[term.surface.length - 2]
                if (Varnamala.isConsonant(lastChar) && term.itMarkers.isEmpty()) {
                    term.copy(itMarkers = term.itMarkers + ItMarker.KIT)
                } else {
                    term
                }
            } else {
                term
            }
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.3: Assigned it-status to final consonants."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)
}
