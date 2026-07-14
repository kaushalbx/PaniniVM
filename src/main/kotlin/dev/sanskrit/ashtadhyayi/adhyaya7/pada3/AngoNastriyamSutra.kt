package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.3.120: āṅo nā'strīyām.
 * The instrumental singular affix 'ṭā' (referred to as 'āṅ') is replaced by 'nā'
 * after a 'ghi' stem that is not feminine.
 */
object AngoNastriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.120",
    text = "आङ्ो नाऽस्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग से भिन्न घि संज्ञक अङ्ग के बाद टा (आङ्) को ना आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730120,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
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

        // 2. Stem must not be feminine (a-strīyām)
        val isFeminine = context.effectiveContext.rupa.linga == Linga.STRI
        if (isFeminine) return false

        // 3. Affix must be ṭā
        return affix.upadesha == "टा"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val newAffixSurface = "ना"

        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = newAffixSurface, upadesha = "ना"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.120: Replaced 'ṭā' affix with 'nā' after non-feminine ghi stem."
        )
    }
}
