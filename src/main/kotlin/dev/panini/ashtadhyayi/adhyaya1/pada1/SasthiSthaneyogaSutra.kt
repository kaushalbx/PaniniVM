package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.67 षष्ठी स्थानेयोग.
 * Paribhāṣā: A genitive case in a sūtra expresses the relation 'in place of' (sthāne).
 */
object SasthiSthaneYogaSutra : Sutra<String, Boolean>(
    number = "1.1.67", text = "षष्ठी स्थानेयोग",
    hindiExplanation = "अनिर्धारित-सम्बन्धा षष्ठी विभक्ति 'स्थान पर' (स्थाने) अर्थ का बोध कराती है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110067,
    role = SutraRole.Paribhasha(ParibhashaScope.GENITIVE_RELATION), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context.endsWith("स्य") || context.endsWith("ः") || context.endsWith("आः") || context.endsWith("ओः")
    override fun apply(context: String): Boolean = true
}
