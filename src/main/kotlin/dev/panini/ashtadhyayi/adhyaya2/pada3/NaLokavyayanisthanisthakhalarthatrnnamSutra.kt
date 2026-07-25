package dev.panini.ashtadhyayi.adhyaya2.pada3

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
 * Sūtra 2.3.66 न लोकाव्ययनिष्ठानिष्ठाखलर्थतृनाम्.
 * Prohibits Ṣaṣṭhī of 2.3.65 (Kartṛkarmaṇoḥ kṛti) before la, u, uka, avyaya, niṣṭhā, khalartha, and tṛn affixes.
 */
object NaLokavyayanisthanisthakhalarthatrnnamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.66", text = "न लोकाव्ययनिष्ठानिष्ठाखलर्थतृनाम्",
    hindiExplanation = "ल, उ, उक, अव्यय, निष्ठा, खलर्थ तथा तृन् प्रत्ययों के योग में षष्ठी का निषेध होता है।",
    type = SutraType.NISHEDHA, chapter = 2, pada = 3, optional = false, kramaValue = 230066,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.PRATYAYA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean = false
    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        throw UnsupportedOperationException("Nishedha sutra applied via ProhibitionEngine.")
    }
}
