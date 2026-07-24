package dev.panini.ashtadhyayi.adhyaya8.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.1.16: padasya.
 * Adhikāra rule governing operations on a completed word (pada) from 8.1.16 to 8.3.119.
 */
object PadasyaAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.1.16",
    text = "पदस्य",
    hindiExplanation = "यह एक अधिकार सूत्र है। यहाँ से ८.३.११९ तक पद का अधिकार चलता है।",
    type = SutraType.ADHIKARA,
    chapter = 8,
    pada = 1,
    optional = false,
    kramaValue = 810016,
    role = SutraRole.Adhikara,
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
    endKrama = 830119,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        "8.1.16" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.activateAdhikara("8.1.16"),
        explanation = "8.1.16 (Padasya) adhikāra activated."
    )
}
