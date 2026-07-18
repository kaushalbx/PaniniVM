package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Linga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Neuter s-stem before bhy- endings: form the o-grade oblique base. */
object StoBhyadiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.152",
    text = "सतो भ्यादिषु",
    hindiExplanation = "नपुंसकलिङ्ग सकारान्त में भ्यादि प्रत्यय परे होने पर ओकारादेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730152,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.effectiveContext.rupa.linga != Linga.NAPUMSAKA) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return stem.surface.endsWith("स्") && affix.upadesha in setOf("भ्याम्", "भिस्", "भ्यस्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = stem.surface.dropLast(2) + "ो"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.152: Formed the neuter s-stem ओ-grade before a bhy- ending.",
        )
    }
}
