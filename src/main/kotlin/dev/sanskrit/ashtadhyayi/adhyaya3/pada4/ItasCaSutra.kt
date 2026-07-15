package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 3.4.100: itaśca.
 * In a Nit lakāra, the final short 'i' of a Parasmaipada suffix is dropped.
 */
object ItasCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.100",
    text = "इतश्च",
    hindiExplanation = "ङित् लकार (लङ् आदि) के परस्मैपद प्रत्ययों के अन्त्य इकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340100,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        if (lastTerm.matchesUpadesha("मिप्")) return false // 3.4.101 takes priority
        
        val isNit = lastTerm.matchesUpadesha("लङ्") || lastTerm.matchesUpadesha("लृङ्") ||
            lastTerm.matchesUpadesha("लुङ्") || context.effectiveContext.rupa.lakara == Lakara.LING
        val isParasmaipada = true
        
        return isNit && isParasmaipada && lastTerm.surface.endsWith('ि')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val newSurface = lastTerm.surface.dropLast(1) + '्'
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.100: Dropped final short 'i' of Parasmaipada suffix."
        )
    }
}
