package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.Vibhakti
import dev.panini.sutra.*

/**
 * 1.1.66: tasminniti nirdiṣṭe pūrvasya.
 * Interpretative rule: An operation specified with a Locative term (tasmin) applies to the sound immediately preceding it.
 */
object TasminnitiNirdishtePurvasyaSutra : Sutra<RuleOperandReference, RuleOperandRelation>(
    number = "1.1.66",
    text = "तस्मिन्निति निर्दिष्टे पूर्वस्य",
    hindiExplanation = "सप्तमीनिर्देशेन विधीयमानं कार्यं व्यवहितानन्तरस्य पूर्वस्य बोध्यम्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110066,
    role = SutraRole.Paribhasha( targetScope = ParibhashaScope.LOCATIVE_TRIGGER),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.VARNA,
) {
    override fun matches(context: RuleOperandReference): Boolean = context.vibhakti == Vibhakti.SAPTAMI
    override fun apply(context: RuleOperandReference): RuleOperandRelation {
        require(matches(context)) { "1.1.66 requires an explicitly locative rule operand." }
        return RuleOperandRelation.IMMEDIATELY_PRECEDING
    }
}
