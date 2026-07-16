package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.94: लेटोऽडाटौ. Uses the attested आट् branch for the canonical LET derivation. */
object LetodatauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.94",
    text = "लेटोऽडाटौ",
    hindiExplanation = "लेट् के तिङ् प्रत्यय के आदि में अट् अथवा आट् आगम होता है; यहाँ आट्-पक्ष लिया गया है।",
    type = SutraType.VIBHASHA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340094,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LET ||
            context.stage in setOf(DerivationStage.INITIAL, DerivationStage.PRATYAYA_SELECTED, DerivationStage.FINAL)
        ) return false
        val ending = context.terms.lastOrNull() ?: return false
        if (ending.kind != TermKind.PRATYAYA || ending.surface.startsWith("आ")) return false
        if (ending.matchesUpadesha("तिप्") && ending.surface == "तिप्") return false
        if (ending.matchesUpadesha("सिप्") && ending.surface == "सिप्") return false
        if (ending.matchesUpadesha("झि") && ending.surface.startsWith("झ")) return false
        if (ending.matchesUpadesha("मिप्") && ending.surface != "नि") return false
        return ending.id.startsWith("ting-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val augmentedSurface = if (ending.surface.startsWith("अ")) {
            "आ${ending.surface.drop(1)}"
        } else {
            "आ${ending.surface}"
        }
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = augmentedSurface)),
            "3.4.94 attaches the आट् augment at the beginning of the LET ending.",
        )
    }
}
