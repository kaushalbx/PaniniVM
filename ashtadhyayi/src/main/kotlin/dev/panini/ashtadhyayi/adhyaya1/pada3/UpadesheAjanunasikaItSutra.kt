package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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
        term.phonologicalText.effectiveVarnas.any { it.varna is Svara && it.nasalized } &&
            (term.kind != dev.panini.derivation.TermKind.DHATU || term.varnas.takeLast(2) == listOf(Vyanjana.RA, Svara.U)) &&
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
                        designation.copy(designatedText = it.orthographicDesignationText(designation.start, designation.endExclusive))
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
        term.phonologicalText.effectiveVarnas.mapIndexedNotNull { index, token ->
            if (token.varna !is Svara || !token.nasalized) return@mapIndexedNotNull null
            val designation = term.designateVarnaIt(index, ItMarker.U, sutra)
            if ((term.itDesignations + term.deferredItDesignations)
                    .any { it.start == designation.start && it.endExclusive == designation.endExclusive }) {
                null
            } else {
                designation
            }
        }
}
