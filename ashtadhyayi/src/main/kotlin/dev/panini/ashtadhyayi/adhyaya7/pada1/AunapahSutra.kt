package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.18: aunaḥ āpaḥ.
 * After a stem ending in the feminine suffix 'āp' (ā-stem),
 * the dual affixes 'au' and 'auṭ' are replaced by 'śī'.
 */
object AunapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.18",
    text = "औङ आपः",
    hindiExplanation = "आप्-प्रत्यान्त अङ्ग के बाद 'औ' और 'औट्' के स्थान पर 'शी' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710018,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val endsInAA = stem.surface.endsWith('ा') || stem.surface.endsWith('आ')
        val isEligibleAffix = affix.upadesha == "औ" || affix.upadesha == "औट्"

        return endsInAA && isEligibleAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceWholeAffix(affix.id, "शी", sutra, dev.panini.derivation.WholeAffixDesignationPolicy.FreshUpadesha, upadesha = "शी"),
            explanation = "7.1.18: Substituted 'śī' for dual 'au' after an ā-stem."
        )
    }
}
