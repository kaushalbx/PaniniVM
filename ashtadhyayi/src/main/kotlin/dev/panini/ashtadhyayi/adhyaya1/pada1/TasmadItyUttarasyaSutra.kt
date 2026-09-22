package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.Vibhakti
import dev.panini.sutra.*

/**
 * 1.1.67: tasmād ity uttarasya.
 * Interpretative rule: An operation specified with an Ablative term (tasmāt) applies to the sound immediately following it.
 */
object TasmadItyUttarasyaSutra : Sutra<RuleOperandReference, RuleOperandRelation>(
    number = "1.1.67",
    text = "तस्मादित्युत्तरस्य",
    hindiExplanation = "पञ्चमीनिर्देशेन विधीयमानं कार्यं व्यवहितानन्तरस्य परस्य बोध्यम्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110067,
    role = SutraRole.Paribhasha(targetScope = ParibhashaScope.ABLATIVE_TRIGGER),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.VARNA,
) {
    override fun matches(context: RuleOperandReference): Boolean = context.vibhakti == Vibhakti.PANCHAMI
    override fun apply(context: RuleOperandReference): RuleOperandRelation {
        require(matches(context)) { "1.1.67 requires an explicitly ablative rule operand." }
        return RuleOperandRelation.IMMEDIATELY_FOLLOWING
    }
}
