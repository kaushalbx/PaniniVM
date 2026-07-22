package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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

        val isNit = lastTerm.matchesUpadesha("लङ्") || lastTerm.matchesUpadesha("लृङ्") ||
            lastTerm.matchesUpadesha("लुङ्") || context.effectiveContext.rupa.lakara == Lakara.LING
        // The rule concerns the Parasmaipada tiṅ endings introduced here.
        // The Ātmanepada थास् ending must retain its स् so that 8.2.66 and
        // 8.3.15 can derive the final visarga in forms such as लभेथाः.
        val isParasmaipadaEnding = lastTerm.id in setOf(
            "ting-tas",
            "ting-thas",
            "ting-vas",
            "ting-mas",
        )
        val isUpadeshaS = lastTerm.upadesha?.endsWith("स्") == true
        val endsWithS = lastTerm.surface.endsWith("स्")

        return isNit && isParasmaipadaEnding && isUpadeshaS && endsWithS
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
