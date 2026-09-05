package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.26 क्तक्तवतू निष्ठा.
 * Assigns niṣṭhā saṃjñā to kta and ktavatu affixes.
 */
object KtaKtavatuNisthaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.26", text = "क्तक्तवतू निष्ठा",
    hindiExplanation = "'क्त' तथा 'क्तवतु' प्रत्ययों की 'निष्ठा' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110026,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("क्त", "क्तवतुँ") } &&
        "1.1.26" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.26"),
            explanation = "1.1.26 assigns निष्ठा saṃjñā to kta and ktavatu affixes.",
        )
}
