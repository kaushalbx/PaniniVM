package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 1.3.2 assigns it-status to a nasalized vowel in upadeśa. */
object UpadesheAjanunasikaItSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.2",
    text = "उपदेशेऽजनुनासिक इत्",
    hindiExplanation = "उपदेश में अनुनासिक अच् इत्संज्ञक होता है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130002,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    private fun targets(state: DerivationState) = state.terms.filter { term ->
        'ँ' in term.surface &&
            (term.kind != dev.panini.derivation.TermKind.DHATU || term.surface.endsWith("रुँ")) &&
            (term.itProcessingPending ||
                (state.stage == DerivationStage.PRATYAYA_SELECTED && term.kind == dev.panini.derivation.TermKind.PRATYAYA)) &&
            nasalVowelDesignations(term).isNotEmpty()
    }

    fun hasSamjnaTarget(state: DerivationState): Boolean = targets(state).isNotEmpty()

    fun assignSamjna(state: DerivationState): DerivationChange {
        val targets = targets(state)
        return DerivationChange(
            state.copy(terms = state.terms.map {
                if (it in targets) {
                    val designations = nasalVowelDesignations(it).map { designation ->
                        designation.copy(designatedText = it.surface.substring(designation.start, designation.endExclusive))
                    }
                    it.copy(
                        itMarkers = it.itMarkers + ItMarker.U,
                        itProcessingPhase = if (it.itProcessingPending) dev.panini.derivation.ItProcessingPhase.DESIGNATED else it.itProcessingPhase,
                        itDesignations = if (it.itProcessingPending) it.itDesignations + designations else it.itDesignations,
                        deferredItDesignations = if (it.itProcessingPending) it.deferredItDesignations else it.deferredItDesignations + designations,
                    )
                } else it
            }),
            "1.3.2 assigns इत्-saṃjñā to the nasalized vowel of ${targets.joinToString { it.surface }}.",
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)

    private fun nasalVowelDesignations(term: dev.panini.derivation.DerivationTerm): List<ItDesignation> =
        term.surface.indices.filter { term.surface[it] == 'ँ' }.mapNotNull { chandrabindu ->
            val vowel = chandrabindu - 1
            if (vowel < 0 || (term.itDesignations + term.deferredItDesignations)
                    .any { it.start == vowel && it.endExclusive == chandrabindu + 1 }) {
                null
            } else {
                val isDependentVowel = term.surface[vowel] in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')
                ItDesignation(
                    start = vowel,
                    endExclusive = chandrabindu + 1,
                    replacementAfterLopa = if (isDependentVowel) "्" else "",
                    marker = ItMarker.U,
                    sutra = sutra,
                    designatedText = term.surface.substring(vowel, chandrabindu + 1),
                )
            }
        }
}
