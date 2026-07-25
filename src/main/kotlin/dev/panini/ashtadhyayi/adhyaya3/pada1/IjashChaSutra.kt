package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.36 इजादेश्च गुरुमतोऽनृच्छः.
 * Prescribes ām pratyaya for vowel-initial heavy roots in Lit.
 */
object IjashChaSutra : Sutra<String, String>(
    number = "3.1.36", text = "इजादेश्च गुरुमतोऽनृच्छः",
    hindiExplanation = "इच् (इ, उ, ऋ, ऌ, ए, ओ, ऐ, औ) से प्रारम्भ होने वाले तथा गुरुमान् धातुओं से लिट् में 'आम' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310036,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context in setOf("ईक्ष्", "एध्", "उक्ष्", "ईध्", "ऐध्")
    override fun apply(context: String): String = "आम"
}
