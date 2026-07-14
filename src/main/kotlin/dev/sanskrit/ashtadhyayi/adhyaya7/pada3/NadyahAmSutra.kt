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

/** 7.3.122: nadyaḥ ām. The nadī genitive plural realizes ām as nām. */
object NadyahAmSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.122",
    text = "नद्याः आम्",
    hindiExplanation = "नदी संज्ञक अङ्ग के बाद आम्-प्रत्यय का नाम् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730122,
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
            affix.upadesha == "आम्" && affix.surface != "नाम्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "नाम्", itMarkers = emptySet())),
            explanation = "7.3.122: Replaced genitive-plural आम् with नाम् after a nadī aṅga.",
        )
    }
}
