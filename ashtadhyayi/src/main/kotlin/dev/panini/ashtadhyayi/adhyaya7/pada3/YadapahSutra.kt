package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.113: yāḍāpaḥ.
 * The augment yāṭ is added to a ṅit case-affix when it follows an aṅga ending in āp.
 */
object YadapahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.113",
    text = "याडापः",
    hindiExplanation = "आप्-प्रत्यान्त अङ्ग के बाद ङित् विभक्ति को याट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730113,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isNadiGenitivePlural = context.samjnas.any { it.targetId == stem.id && it.samjna == dev.panini.shiksha.Samjna.NADI } &&
            affix.upadesha == "आम्"
        if (isNadiGenitivePlural) return false

        // 1. The stem must be an actual āp formation, not merely end in long ā.
        if ((!stem.surface.endsWith('ा') && !stem.surface.endsWith('आ')) ||
            !stem.hasEffectiveMarker(ItMarker.P)
        ) return false

        // 2. Affix must be ṅit
        val isNgit = affix.hasEffectiveMarker(ItMarker.NGIT) ||
            affix.upadesha in setOf("ङि", "टा") || affix.surface == "आम्"

        // Prevent infinite loop by checking if we already applied 'yā'
        val alreadyApplied = affix.surface.startsWith("या") || affix.surface.startsWith("यै")

        return isNgit && !alreadyApplied
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (affix.upadesha == "टा") {
            return DerivationChange(
                state = context.replaceTerm(stem.id, stem.copy(surface = stem.surface.dropLast(1)))
                    .replaceTerm(affix.id, affix.copy(surface = "या"))
                    .blockSutra(sutra, sutra)
                    .copy(stage = DerivationStage.PADA_FORMED),
                explanation = "7.3.113: Formed the instrumental singular -या after an āp stem.",
            )
        }
        val newSurface = when (affix.upadesha) {
            "ङसि", "ङस्" -> "याः"
            else -> when (affix.surface) {
            "ङे" -> "यै"
            "अे" -> "यै"
            "अस्" -> "यास्"
            "आम्" -> "याम्"
            else -> "या" + affix.surface
            }
        }

        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = newSurface))
                .blockSutra(sutra, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.113: Added 'yāṭ' augment before ṅit affix and merged."
        )
    }
}
