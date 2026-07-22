package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.119: acca gheḥ.
 * Before the locative singular affix 'ṅi', 'au' is substituted for 'ṅi',
 * and 'a' is substituted for the final 'i/u' of a 'ghi' stem.
 */
object AccaGhehSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.119",
    text = "अच्च घेः",
    hindiExplanation = "ङि परे होने पर घि संज्ञक अङ्ग के अन्त्य को अकार होता है और ङि के स्थान पर औ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730119,
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

        val isGhi = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI }
        if (!isGhi) return false

        return affix.upadesha == "ङि"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val newStemSurface = stem.surface.dropLast(1) + "अ"
        val newAffixSurface = "औ"

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newStemSurface))
                .replaceTerm(affix.id, affix.copy(surface = newAffixSurface, upadesha = "औ"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.119: Substituted 'a' for ghi-stem vowel and 'au' for 'ṅi'."
        )
    }
}
