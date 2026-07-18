package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.shiksha.Linga
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        if (!HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context)) return false

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
