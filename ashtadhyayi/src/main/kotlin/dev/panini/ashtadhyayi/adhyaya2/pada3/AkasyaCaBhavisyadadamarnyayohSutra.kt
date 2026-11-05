package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.70 अकेनोर्भविष्यदाधमर्ण्ययोः.
 * Prohibits kṛt genitive with aka in the specified future derivation,
 * and in with future or debt meaning.
 */
object AkasyaCaBhavisyadadamarnyayohSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.70", text = "अकेनोर्भविष्यदाधमर्ण्ययोः",
    hindiExplanation = "भविष्यदधिकारविहित अक तथा भविष्यत् अथवा आधमर्ण्य अर्थ में विहित इन् के योग में कृद्योगषष्ठी का निषेध होता है।",
    type = SutraType.NISHEDHA, chapter = 2, pada = 3, optional = false, kramaValue = 230070,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.PRATYAYA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.50", "2.3.69"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean = false

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult =
        throw UnsupportedOperationException("2.3.70 is evaluated through NishedhaRuleEngine; it does not assign prathamā.")
}
