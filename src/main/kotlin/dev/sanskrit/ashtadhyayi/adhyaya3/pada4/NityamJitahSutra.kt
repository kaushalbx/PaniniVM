package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 3.4.99: nityaṃ jitaḥ.
 * In a Nit lakāra, the final 's' of a Parasmaipada ending is dropped.
 */
object NityamJitahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.99",
    text = "नित्यं जितः",
    hindiExplanation = "ङित् लकार के परस्मैपद उत्तम पुरुष के प्रत्ययों तस्, वस्, मस् आदि के अन्त्य सकार का नित्य लोप होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340099,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        
        val isNit = lastTerm.matchesUpadesha("लङ्") || lastTerm.matchesUpadesha("लृङ्") || lastTerm.matchesUpadesha("लुङ्")
        val isUpadeshaS = lastTerm.upadesha?.endsWith("स्") == true
        val endsWithS = lastTerm.surface.endsWith("स्")
        
        return isNit && isUpadeshaS && endsWithS
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val newSurface = lastTerm.surface.dropLast(2)
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.99: Dropped final 's' of Parasmaipada suffix."
        )
    }
}
