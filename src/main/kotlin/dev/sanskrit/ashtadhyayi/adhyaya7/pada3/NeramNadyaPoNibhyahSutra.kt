package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.3.116: ṅerāmnadyāpo-nībhyaḥ.
 * The locative singular case affix ṅi is replaced by ām after nadī-designated terms,
 * terms ending in āp, or the term nī.
 */
object NeramNadyaPoNibhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.116",
    text = "ङेराम्नद्याम्नीभ्यः",
    hindiExplanation = "नदी संज्ञक, आप्-प्रत्यान्त और नी शब्दों के बाद ङि के स्थान पर आम् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730116,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // The rule covers nadī, āp, and nī.  A nadī stem need not be ā-final.
        val isNadi = context.samjnas.any { it.targetId == stem.id && it.samjna == dev.sanskrit.shiksha.Samjna.NADI }
        val isAp = stem.surface.endsWith('ा') || stem.surface.endsWith('आ')
        if (!isNadi && !isAp && stem.surface != "नी") return false

        // 2. Affix must be 'ṅi' (Locative Singular)
        return affix.upadesha == "ङि" && affix.surface != "आम्" && !affix.surface.startsWith("या")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "आम्", itMarkers = affix.itMarkers + ItMarker.NGIT))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.116: Replaced Locative Singular 'ṅi' with 'ām'."
        )
    }
}
