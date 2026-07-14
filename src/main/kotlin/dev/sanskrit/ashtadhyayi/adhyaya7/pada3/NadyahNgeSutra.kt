package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.3.117: nadyaḥ ṅe. The nadī dative singular realizes ṅe as ai. */
object NadyahNgeSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.117",
    text = "नद्याः ङे",
    hindiExplanation = "नदी संज्ञक अङ्ग के बाद ङे-प्रत्यय का ऐ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730117,
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
            affix.upadesha == "ङे" && affix.surface !in setOf("ऐ", "ै")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "ऐ", itMarkers = emptySet())),
            explanation = "7.3.117: Replaced dative ङे with ऐ after a nadī aṅga.",
        )
    }
}
