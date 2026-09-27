package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 4.1.73: शार्ङ्गरवाद्यञो ङीन्.
 *
 * The traditional gaṇa instruction `नृनरयोर्वृद्धिश्च` extends the ङीन्
 * prescription to नृ and नर and prescribes their vṛddhi. It is represented
 * as part of this grammatical application, not as a fictitious 4.1.8 rule.
 */
object SharngaravadyanyoNginSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.73", text = "शार्ङ्गरवाद्यञो ङीन्",
    hindiExplanation = "स्त्रीत्व में शार्ङ्गरवादि प्रातिपदिकों से ङीन् होता है; नृनरयोर्वृद्धिश्च से नृ और नर की वृद्धि भी होती है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410073,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) &&
            (context.terms.any { it.kind == TermKind.PRATIPADIKA && it.varnas in nrForms } ||
                context.samjnas.any { it.samjna == Samjna.NIN } ||
                context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(51, it.surface, it.lexicalUses) }) &&
            context.allEffectiveTerms.none { it.upadesha == "ङीन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val nrOrNara = context.terms.singleOrNull {
            it.kind == TermKind.PRATIPADIKA && it.varnas in nrForms
        }
        val withVrddhi = when (nrOrNara?.varnas) {
            nr -> context.substituteTermVarnas(nrOrNara.id, naar, Svara.R, listOf(Svara.AA, Vyanjana.RA), number)
            nara -> context.substituteTermVarnas(nrOrNara.id, naar, Svara.A, listOf(Svara.AA, Vyanjana.RA), number)
            else -> context
        }
        val result = withVrddhi.addTerm(
            DerivationTerm(
                "ngin-suffix", "ङीन्", TermKind.PRATYAYA, upadesha = "ङीन्",
                createdBySutra = number,
                itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            ),
        ).copy(stage = DerivationStage.PRATYAYA_SELECTED)
        val explanation = if (nrOrNara != null) {
            "4.1.73 with नृनरयोर्वृद्धिश्च introduces ङीन् and prescribes vṛddhi of ${nrOrNara.surface}."
        } else {
            "4.1.73 introduces ङीन् after an eligible शार्ङ्गरवादि term in the feminine."
        }
        return DerivationChange(result, explanation)
    }

    private val nr: List<Varna> = listOf(Vyanjana.NA, Svara.R)
    private val nara: List<Varna> = listOf(Vyanjana.NA, Svara.A, Vyanjana.RA, Svara.A)
    private val naar: List<Varna> = listOf(Vyanjana.NA, Svara.AA, Vyanjana.RA)
    private val nrForms = setOf(nr, nara)
}
