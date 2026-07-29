package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SamjnaDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.65 अलोऽन्त्यात्पूर्व उपधा.
 * Assigns upadhā saṃjñā to the penultimate sound (the sound immediately preceding the last sound).
 */
object AloAntyatPurvaUpadhaSutra : Sutra<String, Char?>(
    number = "1.1.65", text = "अलोऽन्त्यात्पूर्व उपधा",
    hindiExplanation = "अन्तिम वर्ण से ठीक पूर्व स्थित वर्ण की 'उपधा' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110065,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    adhikara = emptySet(),
    artha = SamjnaDefinitionArtha(
        samjni = Samjni.PENULTIMATE_SOUND,
        samjna = dev.panini.shiksha.Samjna.UPADHA,
    ),
) {
    override fun matches(context: String): Boolean = context.length >= 2
    override fun apply(context: String): Char? = context.getOrNull(context.length - 2)
}
