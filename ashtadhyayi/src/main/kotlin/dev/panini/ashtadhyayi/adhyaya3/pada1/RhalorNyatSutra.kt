package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.124: ऋहलोर्ण्यत्.
 * Introduces the kṛtya suffix 'ण्यत्' (ṇyat) after roots ending in 'ऋ' or a consonant.
 */
object RhalorNyatSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.124",
    text = "ऋहलोर्ण्यत्",
    hindiExplanation = "ऋवर्णान्त तथा हलन्त धातुओं से परे ण्यत् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310124,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("3.1.97"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isNyatRequested = context.samjnas.any { it.samjna == Samjna.NYAT } || context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isNyatRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nyatTerm = DerivationTerm(
            id = "nyat_pratyaya",
            surface = "ण्यत्",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.NIT, ItMarker.T),
            upadesha = "ण्यत्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + nyatTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.124 introduces suffix ण्यत्."
        )
    }
}
