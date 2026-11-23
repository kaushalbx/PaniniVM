package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.3.14 लृटः सद् वा — in place of लृट्, the sat affixes शतृ and शानच् (3.2.127) optionally come, as they do for लट्:
 * करिष्यन्तं पश्य, करिष्यमाणं पश्य. Kāśikā calls it a व्यवस्थितविभाषा, so it applies where a participle is asked for
 * and the finite लृट् remains otherwise.
 */
object LrtahSadVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.14", text = "लृटः सद् वा",
    hindiExplanation = "लृट् के स्थान पर विकल्प से सत्संज्ञक शतृ और शानच् होते हैं।",
    type = SutraType.VIBHASHA, chapter = 3, pada = 3, optional = true, kramaValue = 330014,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        return context.effectiveContext.rupa.lakara == Lakara.LRT &&
            context.samjnas.any { it.samjna == Samjna.SATR || it.samjna == Samjna.SANAC } &&
            context.terms.any { it.kind == TermKind.DHATU } && context.terms.none { it.kind == TermKind.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sanac = context.samjnas.any { it.samjna == Samjna.SANAC }
        val upadesha = if (sanac) "शानच्" else "शतृँ"
        val affix = DerivationTerm(if (sanac) "sanac_pratyaya" else "satr_pratyaya", upadesha, TermKind.PRATYAYA,
            upadesha = upadesha, createdBySutra = sutra, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(context.copy(terms = context.terms + affix, stage = DerivationStage.PRATYAYA_SELECTED),
            "3.3.14 puts the sat affix $upadesha in place of लृट्.")
    }
}
