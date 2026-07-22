package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.LetAugment
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.94: लेटोऽडाटौ. Selects either the अट् or आट् branch requested by the caller. */
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
            context.stage in setOf(DerivationStage.INITIAL, DerivationStage.FINAL)
        ) return false
        val ending = context.terms.lastOrNull() ?: return false
        if (ending.kind != TermKind.PRATYAYA || context.substitutions.any { it.sutra == "3.4.94" }) return false
        if (ending.matchesUpadesha("तिप्") && ending.surface == "तिप्") return false
        if (ending.matchesUpadesha("सिप्") && ending.surface == "सिप्") return false
        if (ending.matchesUpadesha("झि") && ending.surface.startsWith("झ")) return false
        if (ending.matchesUpadesha("मिप्") && ending.surface != "नि") return false
        val atmanepadaUpadeshas = setOf("त", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्")
        if (ending.upadesha in atmanepadaUpadeshas && ending.surface == ending.upadesha) return false
        if (ending.upadesha in setOf("आताम्", "आथाम्") && ending.surface !in setOf("ऐते", "ऐथे")) return false
        if (context.effectiveContext.letEOption == dev.panini.derivation.LetEOption.AI &&
            ending.upadesha in atmanepadaUpadeshas && ending.surface.endsWith("े")) return false
        return ending.id.startsWith("ting-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val augment = context.effectiveContext.letAugment
        val augmentedSurface = when (augment) {
            LetAugment.AT -> when {
                ending.surface.startsWith("अ") -> "आ${ending.surface.drop(1)}"
                ending.surface.startsWith("ऐ") -> "ै${ending.surface.drop(1)}"
                else -> "अ${ending.surface}"
            }
            LetAugment.AAT -> if (ending.surface.startsWith("अ")) "आ${ending.surface.drop(1)}" else "आ${ending.surface}"
        }
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = augmentedSurface))
                .addSubstitution(dev.panini.derivation.VarnaSubstitution(ending.id, ending.surface.first(), augmentedSurface, sutra)),
            "3.4.94 attaches the ${if (augment == LetAugment.AT) "अट्" else "आट्"} augment at the beginning of the LET ending.",
        )
    }
}
