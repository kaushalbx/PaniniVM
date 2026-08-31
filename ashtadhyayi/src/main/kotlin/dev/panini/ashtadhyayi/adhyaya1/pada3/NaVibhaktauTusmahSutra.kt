package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.4: na vibhaktau tusmāḥ.
 */
object NaVibhaktauTusmahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.4",
    text = "न विभक्तौ तुस्माः",
    hindiExplanation = "विभक्ति के अन्त में आने वाले त-वर्ग, स् और म् इत् संज्ञक नहीं होते।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130004,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("1.3.3"),
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.PRATYAYA_SELECTED && context.terms.none { it.itProcessingPending }) return false
        val pendingIds = context.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }

        return context.terms.any { term ->
            if (pendingIds.isNotEmpty() && term.id !in pendingIds) return@any false
            val isVibhakti = isVibhaktiTerm(context, term)
            isVibhakti && isTuSMa(term.surface) && term.id !in context.halantyamExemptTermIds
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val pendingIds = context.terms.filter { it.itProcessingPending }.mapTo(mutableSetOf()) { it.id }
        val protectedIds = context.terms.filter { term ->
            (pendingIds.isEmpty() || term.id in pendingIds) &&
            isVibhaktiTerm(context, term) && isTuSMa(term.surface)
        }.mapTo(mutableSetOf()) { it.id }
        val state = context.copy(halantyamExemptTermIds = context.halantyamExemptTermIds + protectedIds)

        return DerivationChange(
            state = state,
            explanation = "1.3.4: Protected each vibhakti-final dental, 's', or 'm' from 1.3.3."
        )
    }

    private fun isTuSMa(surface: String): Boolean {
        return surface.endsWith("त्") || surface.endsWith("थ्") || surface.endsWith("द्") ||
               surface.endsWith("ध्") || surface.endsWith("न्") || surface.endsWith("स्") ||
               surface.endsWith("म्") || surface.endsWith("त") || surface.endsWith("थ") ||
               surface.endsWith("द") || surface.endsWith("ध") || surface.endsWith("न") ||
               surface.endsWith("स") || surface.endsWith("म")
    }

    private fun isVibhaktiTerm(context: DerivationState, term: dev.panini.derivation.DerivationTerm): Boolean =
        context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.PRATYAYA } ||
            TingAffix.entries.any { it.upadesha == term.upadesha } ||
            SupAffix.entries.any { it.upadesha == term.upadesha }
}
