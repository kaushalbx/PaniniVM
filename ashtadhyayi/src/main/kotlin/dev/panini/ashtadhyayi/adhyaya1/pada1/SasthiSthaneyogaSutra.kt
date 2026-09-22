package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.core.Vibhakti
import dev.panini.sutra.RuleOperandReference
import dev.panini.sutra.RuleOperandRelation
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.49 षष्ठी स्थानेयोगा.
 * Paribhāṣā: A genitive case in a sūtra expresses the relation 'in place of' (sthāne).
 */
object SasthiSthaneYogaSutra : Sutra<RuleOperandReference, RuleOperandRelation>(
    number = "1.1.49", text = "षष्ठी स्थानेयोगा",
    hindiExplanation = "अनिर्धारित-सम्बन्धा षष्ठी विभक्ति 'स्थान पर' (स्थाने) अर्थ का बोध कराती है।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110049,
    role = SutraRole.Paribhasha(ParibhashaScope.GENITIVE_RELATION), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    adhikara = emptySet(),
) {
    override fun matches(context: RuleOperandReference): Boolean = context.vibhakti == Vibhakti.SASTHI
    override fun apply(context: RuleOperandReference): RuleOperandRelation {
        require(matches(context)) { "1.1.49 requires an explicitly genitive rule operand." }
        return RuleOperandRelation.SUBSTITUTION_TARGET
    }
}
