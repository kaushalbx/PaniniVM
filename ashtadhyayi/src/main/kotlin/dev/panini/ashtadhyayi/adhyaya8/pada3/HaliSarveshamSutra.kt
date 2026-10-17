package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.22: hali sarveṣām.
 * Pada-final y following bhoḥ, bhagoḥ, aghoḥ or a/ā is elided before hal within inherited aś.
 */
object HaliSarveshamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.22",
    text = "हलि सर्वेषाम्",
    hindiExplanation = "भोः, भगोः, अघोः तथा अ/आ पूर्व वाले पदान्त य् का अश् में आने वाले हल् परे लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830022,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
    blocks = setOf("8.3.19", "8.3.20"),
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@any false
            context.samjnas.any { it.targetId == context.terms[i].id && it.samjna == Samjna.PADA } &&
                eligibleFinal(curr) != null && next is Vyanjana &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next) &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@first false
            context.samjnas.any { it.targetId == context.terms[i].id && it.samjna == Samjna.PADA } &&
                eligibleFinal(curr) != null && next is Vyanjana &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next) &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next)
        }

        val targetTerm = context.terms[targetIndex]
        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, targetTerm.varnas.lastIndex, emptyList(), sutra),
            explanation = "8.3.22: Elided 'y' (hali sarveṣām) before hal consonant."
        )
    }

    private fun eligibleFinal(varnas: List<Varna>): Vyanjana? {
        if (varnas.lastOrNull() != Vyanjana.YA) return null
        val base = varnas.dropLast(1)
        return Vyanjana.YA.takeIf { base in eligiblePrefixes || base.lastOrNull() in setOf(Svara.A, Svara.AA) }
    }

    private val eligiblePrefixes: List<List<Varna>> = listOf(
        listOf(Vyanjana.BHA, Svara.O),
        listOf(Vyanjana.BHA, Svara.A, Vyanjana.GA, Svara.O),
        listOf(Svara.A, Vyanjana.GHA, Svara.O),
    )
}
