package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.2.41 उपसर्जनं पूर्वम्.
 * Position rule: The element designated as upasarjana is placed first in a compound (samāsa).
 */
object UpasarjanamPurvamSutra : Sutra<Pair<String, String>, Pair<String, String>>(
    number = "1.2.41", text = "उपसर्जनं पूर्वम्",
    hindiExplanation = "समास विधान में उपसर्जन-संज्ञक पद का पूर्व प्रयोग (पहले प्रयोग) होता है।",
    type = SutraType.NITYA, chapter = 1, pada = 2, optional = false, kramaValue = 120041,
    role = SutraRole.Niyama, action = SutraAction.NIYAMA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATIPADIKA),
    adhikara = emptySet(),
) {
    override fun matches(context: Pair<String, String>): Boolean = context.first.isNotEmpty()
    override fun apply(context: Pair<String, String>): Pair<String, String> = context
}
