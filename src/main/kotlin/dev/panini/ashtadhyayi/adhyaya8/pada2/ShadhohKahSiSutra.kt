package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 8.2.41: षढोः कः सि. ष् or ढ् is replaced by क् before स्. */
object ShadhohKahSiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.41",
    text = "षढोः कः सि",
    hindiExplanation = "स् परे होने पर ष् अथवा ढ् के स्थान पर क् होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820041,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = targetIndex(context) >= 0

    override fun apply(context: DerivationState): DerivationChange {
        val index = targetIndex(context)
        val source = context.terms[index].surface.dropLast(1).last()
        val target = context.terms[index]
        val replacement = target.surface.dropLast(2) + "क्"
        return DerivationChange(
            context.replaceTerm(target.id, target.copy(surface = replacement))
                .addSubstitution(VarnaSubstitution(target.id, source, "क", sutra)),
            "8.2.41 substitutes क् for $source before स्.",
        )
    }

    private fun targetIndex(context: DerivationState): Int =
        (0 until context.terms.lastIndex).firstOrNull { index ->
            val left = context.terms[index].surface
            val right = context.terms[index + 1].surface
            left.length >= 2 && left.endsWith('्') && left[left.length - 2] in setOf('ष', 'ढ') && right.startsWith('स')
        } ?: -1
}
