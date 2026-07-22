package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.Samjna
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.111: gher ṅiti.
 * A 'ghi' designated stem gets guṇa substitution when followed by a ṅit affix.
 */
object GherNitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.111",
    text = "घेर्ङिति",
    hindiExplanation = "ङित् प्रत्यय परे होने पर घि संज्ञक अङ्ग के अन्त्य को गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730111,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.4.7")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must be 'ghi'
        val isGhi = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI }
        if (!isGhi) return false

        val lastChar = stem.surface.lastOrNull() ?: return false
        if (lastChar != 'इ' && lastChar != 'ि' && lastChar != 'उ' && lastChar != 'ु') return false

        // 2. Affix must be 'ṅit' (marked with ṅ)
        // 1.1.56: Check effective markers
        val isNgit = affix.hasEffectiveMarker(ItMarker.NGIT)

        return isNgit
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val replacement = requireNotNull(Varnamala.getGuna(lastChar))

        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.3.111: Applied guna ($replacement) to 'ghi' stem before ṅit affix."
        )
    }
}
