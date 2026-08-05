package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sankhya.SankhyaResolver
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.2.25: संख्याया आसन्नादूराधिकसंख्याः संख्येये.
 * Prescribes Bahuvrīhi compound when words indicating approximation/excess ('āsanna', 'adūra', 'adhika', 'upa')
 * or a numeral compound with another numeral stem to express approximate or exceeded quantity.
 * Examples: उपपञ्चाशत् (upapañcāśat - nearly 50), आसन्नपञ्चाशत्, द्वित्राः (dvitrāḥ - 2 or 3).
 */
object SamkhyayaAsannaduradhikaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.25",
    text = "संख्याया आसन्नादूराधिकसंख्याः संख्येये",
    hindiExplanation = "संख्यावाचक सुबन्त का आसन्न, अदूर, अधिक, उप तथा संख्यावाचक सुबन्त के साथ बहुव्रीहि समास होता है (उदा. उपपञ्चाशत्, द्वित्राः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220025,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    private val approxPrefixes = setOf("आसन्न", "अदूर", "अधिक", "उप")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada

        val isPurvaMatch = purva.upadesha in approxPrefixes || SankhyaResolver.isSankhya(purva.upadesha, purva.samjnas)
        val isUttaraSankhya = SankhyaResolver.isSankhya(uttara.upadesha, uttara.samjnas)

        return (context.samasaType == SamasaType.BAHUVRIHI || context.samasaType == SamasaType.TATPURUSA) &&
                isPurvaMatch && isUttaraSankhya
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.25 forms Approximate Numeral Bahuvrīhi compound '$compoundStem'.",
        )
    }
}
