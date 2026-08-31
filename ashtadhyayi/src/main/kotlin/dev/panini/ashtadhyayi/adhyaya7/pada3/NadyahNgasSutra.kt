package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.121: nadyaḥ ṅas. The nadī genitive singular realizes ṅas as āḥ. */
object NadyahNgasSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.121",
    text = "नद्याः ङस्",
    hindiExplanation = "नदी संज्ञक अङ्ग के बाद ङस्-प्रत्यय का आः आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730121,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    dependencies = setOf("6.4.1", "1.4.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.NADI } &&
            affix.upadesha == "ङस्" && affix.surface != "आः"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceWholeAffix(affix.id, "आः", sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume),
            explanation = "7.3.121: Replaced genitive ङस् with आः after a nadī aṅga.",
        )
    }
}
