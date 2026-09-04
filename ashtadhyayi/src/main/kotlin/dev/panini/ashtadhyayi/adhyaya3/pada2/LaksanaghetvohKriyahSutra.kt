package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.126 लक्षणहेत्वोः क्रियायाः.
 * Prescribes śatṛ / śānac in symptom and cause meanings.
 */
object LaksanaghetvohKriyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.126", text = "लक्षणहेत्वोः क्रियायाः",
    hindiExplanation = "लक्षण तथा हेतु अर्थों में क्रियावाचक धातु से शतृ तथा शानच् प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320126,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LAT &&
        context.effectiveContext.requestedMeaning != null &&
        context.allEffectiveTerms.none { it.upadesha in setOf("शतृ", "शानच्") }

    override fun apply(context: DerivationState): DerivationChange {
        val satri = DerivationTerm(
            "satri", "शतृँ", TermKind.PRATYAYA,
            upadesha = "शतृ", createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.addTerm(satri),
            explanation = "3.2.126 prescribes शतृ / शानच् in lakṣaṇa/hetu.",
        )
    }
}
