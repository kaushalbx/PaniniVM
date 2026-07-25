package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.70 अकस्य च भविष्यदाधमर्ण्ययोः.
 * Prohibits Ṣaṣṭhī for agent/object before -aka suffix expressing future or debt.
 */
object AkasyaCaBhavisyadadamarnyayohSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.70", text = "अकस्य च भविष्यदाधमर्ण्ययोः",
    hindiExplanation = "भविष्यत् काल अथवा ऋण (कर्ज) अर्थ वाले अक-प्रत्ययान्त शब्द के कर्ता तथा कर्म में षष्ठी विभक्ति का प्रतिषेध होता है।",
    type = SutraType.NISHEDHA, chapter = 2, pada = 3, optional = false, kramaValue = 230070,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.PRATYAYA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean = false

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PRATHAMA,
        KarakaEvidence(number, text, "Prohibits Ṣaṣṭhī before -aka suffix in future or debt sense (2.3.70)."),
    )
}
