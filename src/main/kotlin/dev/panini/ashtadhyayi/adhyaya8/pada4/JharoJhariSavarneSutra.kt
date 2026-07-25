package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.65: jharo jhari savarṇe.
 * A jhar consonant (stops, sibilants) after a consonant (hal) is optionally elided
 * when followed by a homogeneous jhar consonant (savarṇa jhar).
 */
object JharoJhariSavarneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.65",
    text = "झरो झरि सवर्णे",
    hindiExplanation = "हल् से उत्तर झर् वर्ण का अपने सवर्ण झर् परे रहते विकल्प से लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840065,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    private data class Match(val termIndex: Int)

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("त्") || surface.endsWith("द्") || surface.endsWith("क्") || surface.endsWith("ग्") -> surface.dropLast(2)
            surface.endsWith("त") || surface.endsWith("द") || surface.endsWith("क") || surface.endsWith("ग") -> surface.dropLast(1)
            else -> surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.65: Elided redundant jhar consonant before homogeneous jhar."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, surface.last(), "", sutra))) }
    }

    private fun findMatch(context: DerivationState): Match? {
        val terms = context.terms
        for (i in 0 until terms.size - 1) {
            val curr = terms[i].surface
            val next = terms[i + 1].surface

            val doubleStop = curr.endsWith("त्") && next.startsWith("त") ||
                    curr.endsWith("द्") && next.startsWith("द") ||
                    curr.endsWith("क्") && next.startsWith("क")

            if (doubleStop) {
                return Match(i)
            }
        }
        return null
    }
}
