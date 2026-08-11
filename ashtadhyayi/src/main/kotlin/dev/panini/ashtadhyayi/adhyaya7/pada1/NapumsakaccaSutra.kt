package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.19: napuṃsakācca.
 * In neuter (napuṃsaka), the dual affixes 'au' and 'auṭ' are replaced by 'śī'.
 */
object NapumsakaccaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.19",
    text = "नपुंसकाच्च",
    hindiExplanation = "नपुंसकलिङ्ग में 'औ' और 'औट्' के स्थान पर 'शी' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710019,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (!HasMorphosyntax(linga = Linga.NAPUMSAKA, vacana = Vacana.DVIVACANA).matches(context)) return false

        val affix = context.terms.lastOrNull() ?: return false
        return affix.upadesha == "औ" || affix.upadesha == "औट्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        // 1.1.55: śī replaces the entire affix. Surface becomes 'ī' after it-processing.
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "ई", upadesha = "शी")),
            explanation = "7.1.19: Substituted 'śī' for neuter dual 'au/auṭ'."
        )
    }
}
