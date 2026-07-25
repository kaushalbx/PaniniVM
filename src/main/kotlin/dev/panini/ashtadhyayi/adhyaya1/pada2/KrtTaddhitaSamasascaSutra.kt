package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.2.46 कृत्तद्धितसमासाश्च.
 * Assigns Prātipadika saṃjñā to kṛt-affixed, taddhita-affixed, and compound (samāsa) words.
 */
object KrtTaddhitaSamasascaSutra : Sutra<String, String>(
    number = "1.2.46", text = "कृत्तद्धितसमासाश्च",
    hindiExplanation = "कृदन्त, तद्धितान्त तथा समास शब्दों की भी प्रातिपदिक संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 2, optional = false, kramaValue = 120046,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA, SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotEmpty()
    override fun apply(context: String): String = "प्रातिपदिकम्"
}
