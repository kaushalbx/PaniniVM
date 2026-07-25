package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.61 प्रत्ययस्य लुप्श्लुलोपाः.
 * Classifies the deletion/elision of an affix into lup, ślu, or lopa.
 */
object PratyayasyaLupSlulopahSutra : Sutra<String, String>(
    number = "1.1.61", text = "प्रत्ययस्य लुप्श्लुलोपाः",
    hindiExplanation = "प्रत्यय के अदर्शन/लोप की सञ्ज्ञा 'लुप्', 'श्लु' अथवा 'लोप' होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110061,
    role = SutraRole.Samjna, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
    inputs = setOf(SutraInput.PRATYAYA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context == "लुप्" || context == "श्लु" || context == "लोप"
    override fun apply(context: String): String = context
}
