package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.4.125: सुहृद्दुहृदौ मित्रामित्रयोः.
 * Nipātana formation of suhṛd (friend) and durhṛd (enemy).
 * Example: सुहृत् (suhṛd), दुर्हृत् (durhṛd).
 */
object SuhrdDurhrdauMitramitrayohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.125",
    text = "सुहृद्दुहृदौ मित्रामित्रयोः",
    hindiExplanation = "मित्र अर्थ में 'सुहृद्' तथा अमित्र (शत्रु) अर्थ में 'दुर्हृद्' शब्द निपातन से सिद्ध होते हैं।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540125,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "सु" || first == "दुर्") && (last == "हृदय" || last == "हृद्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.first().upadesha
        val stem = if (first == "सु") "सुहृद्" else "दुर्हृद्"
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "5.4.125 derives nipātana form '$stem'.",
        )
    }
}
