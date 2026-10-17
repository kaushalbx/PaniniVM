package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Vyanjana
import dev.panini.core.KrtAffix
import dev.panini.derivation.hasCurrentAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Implemented kvip branch of 6.1.67: deletes the single operative v, not a vi syllable. */
object VerAprktasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.67",
    text = "वेरपृक्तस्य",
    hindiExplanation = "अपृक्त वकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610067,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? = context.terms.firstOrNull {
        it.hasCurrentAffix(KrtAffix.KVIP) && it.varnas == listOf(Vyanjana.VA) &&
            it.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            it.itDesignations.isEmpty() && it.deferredItDesignations.isEmpty()
    }
    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val kvip = requireNotNull(target(context))
        return DerivationChange(
            state = context.deleteTermVarnas(kvip.id, 0, 1, sutra),
            explanation = "6.1.67 deletes kvip's processed single operative v (apṛkta).",
        )
    }
}
