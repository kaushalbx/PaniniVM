package dev.panini.ashtadhyayi.adhyaya1.pada2

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
object PrathamanirdistamSamasaUpasarjanamSutra : Sutra<String, String>(
    number = "1.2.43", text = "प्रथमानिर्दिष्टं समास उपसर्जनम्",
    hindiExplanation = "समास-शास्त्र में प्रथमा विभक्ति द्वारा निर्दिष्ट पद की 'उपसर्जन' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 2, optional = false, kramaValue = 120043,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotEmpty()
    override fun apply(context: String): String = "उपसर्जनम्"
}
