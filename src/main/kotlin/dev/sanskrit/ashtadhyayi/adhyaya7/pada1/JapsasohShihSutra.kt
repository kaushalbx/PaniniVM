package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.SemanticFeature
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.20: jaś-śasoḥ śiḥ. 
 * In neuter (napuṃsaka), the plural affixes 'jas' and 'śas' are replaced by 'śi'.
 */
object JapsasohShihSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.20",
    text = "जश्शसोः शिः",
    hindiExplanation = "नपुंसकलिङ्ग में जस् और शस् के स्थान पर 'शि' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710020,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (SemanticFeature.NAPUMSAKA !in context.semanticFeatures) return false
        
        val affix = context.terms.lastOrNull() ?: return false
        return affix.upadesha == "जस्" || affix.upadesha == "शस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "शि", upadesha = "शि")),
            explanation = "7.1.20: Substituted 'śi' for neuter plural 'jas/śas'."
        )
    }
}
