package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.SamjnaVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.2.43 प्रथमानिर्दिष्टं समास उपसर्जनम्.
 * Assigns upasarjana saṃjñā to the compound component designated in nominative case (Prathamā) within a compound rule.
 */
object PrathamanirdistamSamasaUpasarjanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.43", text = "प्रथमानिर्दिष्टं समास उपसर्जनम्",
    hindiExplanation = "समास-शास्त्र में प्रथमा विभक्ति द्वारा निर्दिष्ट पद की 'उपसर्जन' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 2, optional = false, kramaValue = 120043,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA),
), DerivationSutra, SamjnaVidhayakaSutra {
    override val artha = SamjnaDefinitionArtha(
        samjni = Samjni.PRATHAMA_DESIGNATED_COMPOUND_TERM,
        samjna = dev.panini.shiksha.Samjna.UPASARJANA,
    )

    override fun matches(context: DerivationState): Boolean =
        "1.2.43" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.2.43"),
            explanation = "1.2.43 assigns upasarjana saṃjñā to Prathamā-designated compound terms.",
        )
}
