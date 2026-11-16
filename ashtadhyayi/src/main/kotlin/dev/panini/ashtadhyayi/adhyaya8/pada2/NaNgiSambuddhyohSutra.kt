package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.core.Vacana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.*

/**
 * 8.2.8 न ङिसम्बुद्ध्योः — 8.2.7 does not delete a prātipadika's final न् before ङि or a सम्बुद्धि:
 * हे राजन्. Only the सम्बुद्धि half is modelled (the ङि half is Vedic in practice: चर्मन् तिलम्).
 * The सम्बुद्धि comes from the requested address (2.3.47, 2.3.49), which survives 6.1.68's deletion of सुँ.
 * It changes nothing by itself; 8.2.7 consults [protects].
 */
object NaNgiSambuddhyohSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.8", text = "न ङिसम्बुद्ध्योः",
    hindiExplanation = "ङि और सम्बुद्धि परे होने पर प्रातिपदिकान्त नकार का लोप नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 8, pada = 2, optional = false, kramaValue = 820008,
    role = SutraRole.Nishedha, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
    blocks = setOf("8.2.7"),
), DerivationSutra {
    override val advancesTripadiFrontier = false

    fun protects(context: DerivationState): Boolean = context.effectiveContext.rupa.let {
        it.isVocative && it.vacana == Vacana.EKAVACANA
    }

    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState): DerivationChange =
        error("8.2.8 restricts 8.2.7 and does not apply on its own")
}
