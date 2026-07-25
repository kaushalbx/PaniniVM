package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.7 धातोः कर्मणः समानकर्तृकादिच्छायां सन्.
 * Prescribes san desiderative pratyaya after a root in the sense of desire.
 */
object DhatohKarmanahSamanakartrkadIcchayamSanSutra : Sutra<String, String>(
    number = "3.1.7", text = "धातोः कर्मणः समानकर्तृकादिच्छायां सन्",
    hindiExplanation = "समानकर्तृक इच्छा अर्थ में धातु से 'सन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310007,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotBlank()
    override fun apply(context: String): String = "सन्"
}
