package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.40 कृञ् चानुप्रयुज्यते लिटि.
 * Prescribes auxiliary root attachment (kṛ, bhū, as) after ām in Lit.
 */
object KrnChanuprayujyateLitSutra : Sutra<String, String>(
    number = "3.1.40", text = "कृञ् चानुप्रयुज्यते लिटि",
    hindiExplanation = "आमन्त के पश्चात् लिट् लकार में 'कृञ्' (तथा भू, अस्) अनुप्रयुक्त (सहायक धातु के रूप में) होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310040,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("आम") || context.endsWith("आम्") || context.endsWith("ाम") || context.endsWith("ाम्") || context.endsWith("आमन्त")
    override fun apply(context: String): String = context + " कृञ्"
}
