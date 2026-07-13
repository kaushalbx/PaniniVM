package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.4.148: yasyeti ca.
 * Deletion of final 'i' or 'a' of a 'bha' stem before 'ī' or a Taddhita affix.
 */
object YasyetiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.148",
    text = "यस्येति च",
    hindiExplanation = "भ-संज्ञक अङ्ग के अन्त्य 'इ' या 'अ' का लोप होता है, 'ई' या तद्धित प्रत्यय परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640148,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DHATU,
    dependencies = setOf("6.4.1", "1.4.18")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must be 'bha'
        val isBha = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.BHA }
        if (!isBha) return false

        // 2. Stem must end in 'i' or 'a'
        val endsInIOrA = stem.surface.endsWith('इ') || stem.surface.endsWith('ि') ||
                         stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        
        // 3. Affix must start with 'ī' (Simplified: Taddhita check omitted for now)
        val startsWithII = affix.surface.startsWith('ई') || affix.surface.startsWith('ी')
        
        return endsInIOrA && startsWithII
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = stem.surface.dropLast(1)
        
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.148: Deleted final vowel of 'bha' stem before 'ī'."
        )
    }
}
