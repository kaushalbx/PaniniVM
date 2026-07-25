package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.68 स्वं रूपं शब्दस्याशब्दसंज्ञा.
 * Technical rule: A word in a sūtra represents its own literal form unless it is a technical saṃjñā.
 */
object SvamRupamSabdasyasabdasamjnaSutra : Sutra<String, String>(
    number = "1.1.68", text = "स्वं रूपं शब्दस्याशब्दसंज्ञा",
    hindiExplanation = "व्याकरण में शब्द-स्वरूप अपने ही रूप (स्वरूप) का बोधक होता है, यदि वह शास्त्र-प्रसिद्ध संज्ञा न हो।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110068,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.SAMJNA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context.isNotEmpty()
    override fun apply(context: String): String = context
}
