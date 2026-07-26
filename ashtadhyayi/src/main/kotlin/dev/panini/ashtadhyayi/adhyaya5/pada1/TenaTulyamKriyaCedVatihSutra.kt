package dev.panini.ashtadhyayi.adhyaya5.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 5.1.115 तेन तुल्यं क्रिया चेद् वतिः.
 * Prescribes vatiḥ (वत्) Taddhita affix after 3rd case nominal for action similarity (e.g. brāhmaṇavat).
 */
object TenaTulyamKriyaCedVatihSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.1.115", text = "तेन तुल्यं क्रिया चेद् वतिः",
    hindiExplanation = "तृतीयान्त पद से क्रिया-तुल्यता सादृश्य अर्थ में 'वतिः' (वत्) प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 5, pada = 1, optional = false, kramaValue = 510115,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.any { it.kind == TermKind.PRATIPADIKA } &&
        context.allEffectiveTerms.none { it.upadesha == "वतिः" || it.upadesha == "वत्" }

    override fun apply(context: DerivationState): DerivationChange {
        val vatTerm = DerivationTerm("vatih", "वत्", TermKind.PRATYAYA, upadesha = "वत्")
        return DerivationChange(
            state = context.addTerm(vatTerm),
            explanation = "5.1.115 prescribes वत् (वतिः) Taddhita similarity affix.",
        )
    }
}
