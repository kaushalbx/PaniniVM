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

        // 1. Stem must end in 'ā' (representing Āp)
        if (!stem.surface.endsWith('ा') && !stem.surface.endsWith('आ')) return false

        // 2. Affix must be ṅit
        val isNgit = affix.hasEffectiveMarker(ItMarker.NGIT) || affix.upadesha == "ङि" || affix.surface == "आम्"
        
        // Prevent infinite loop by checking if we already applied 'yā'
        val alreadyApplied = affix.surface.startsWith("या") || affix.surface.startsWith("यै")
        
        return isNgit && !alreadyApplied
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val newSurface = when (affix.surface) {
            "अे" -> "यै"
            "अस्" -> "यास्"
            "आम्" -> "याम्"
            else -> "या" + affix.surface
        }
        
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.113: Added 'yāṭ' augment before ṅit affix and merged."
        )
    }
}
