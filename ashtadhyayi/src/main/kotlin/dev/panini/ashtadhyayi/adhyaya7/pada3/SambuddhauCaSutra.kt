package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Samjna
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.106: sambuddhau ca.
 * The final 'ā' of an aṅga ending in 'āp' is replaced by 'e'
 * before the sambuddhi (vocative singular) affix.
 */
object SambuddhauCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.106",
    text = "सम्बुद्धौ च",
    hindiExplanation = "सम्बुद्धि (सम्बोधन एकवचन) परे होने पर आप्-प्रत्यान्त अङ्ग के अन्त्य 'आ' का एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730106,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in 'ā' (representing Āp)
        if (!stem.surface.endsWith('ा') && !stem.surface.endsWith('आ')) return false

        // 2. Affix must be Sambuddhi
        val isSambuddhi = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SAMBUDDHI }
        return isSambuddhi
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val replacement = "े"

        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.3.106: Replaced final 'ā' with 'e' before Sambuddhi."
        )
    }
}
