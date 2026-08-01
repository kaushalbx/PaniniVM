package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.14: ḍho ḍhe lopaḥ.
 * The sound 'ḍh' is elided when immediately followed by another 'ḍh'.
 */
object DhoDheLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.14",
    text = "ढो ढे लोपः",
    hindiExplanation = "ढ-कार का ढ-कार परे रहते लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830014,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val currEndsWithDha = curr.endsWith("ढ") || curr.endsWith("ढ्")
            val nextStartsWithDha = next.startsWith("ढ") || next.startsWith("ढ्")

            currEndsWithDha && nextStartsWithDha
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val currEndsWithDha = curr.endsWith("ढ") || curr.endsWith("ढ्")
            val nextStartsWithDha = next.startsWith("ढ") || next.startsWith("ढ्")

            currEndsWithDha && nextStartsWithDha
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("ढ्") -> surface.dropLast(2)
            surface.endsWith("ढ") -> surface.dropLast(1)
            else -> surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.3.14: Elided 'ḍh' before another 'ḍh'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'ढ', "", sutra))) }
    }
}
