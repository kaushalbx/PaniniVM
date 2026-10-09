package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
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
 * The 'y' following bhoḥ, bhagoḥ, aghoḥ or ā-pūrva is elided before any consonant (hal).
 */
object HaliSarveshamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.22",
    text = "हलि सर्वेषाम्",
    hindiExplanation = "भोः, भगोः, अघोः तथा आकार पूर्व वाले य्-कार का हल् परे होने पर लोप होता है (उदा. भो देवाः, देवा हसन्ति)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830022,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@any false
            eligibleFinal(curr) != null && next is Vyanjana &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas.firstOrNull() ?: return@first false
            eligibleFinal(curr) != null && next is Vyanjana &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next)
        }

        val targetTerm = context.terms[targetIndex]
        val dropCount = requireNotNull(eligibleFinal(targetTerm.varnas)).first

        return DerivationChange(
            state = context.deleteTermVarnas(targetTerm.id, targetTerm.varnas.size - dropCount, dropCount, sutra),
            explanation = "8.3.22: Elided 'y' (hali sarveṣām) before hal consonant."
        )
    }

    private fun eligibleFinal(varnas: List<Varna>): Pair<Int, Varna>? {
        val hasEligibleBase = eligiblePrefixes.any { prefix -> varnas.take(prefix.size) == prefix } ||
            varnas.dropLast(if (varnas.takeLast(2) == listOf(Vyanjana.YA, Svara.A)) 2 else 1).lastOrNull() == Svara.AA
        if (!hasEligibleBase) return null
        return when {
            varnas.takeLast(2) == listOf(Vyanjana.YA, Svara.A) -> 2 to Vyanjana.YA
            varnas.lastOrNull() == Vyanjana.YA -> 1 to Vyanjana.YA
            varnas.lastOrNull() == Ayogavaha.VISARGA -> 1 to Ayogavaha.VISARGA
            varnas.lastOrNull() == Vyanjana.SA -> 1 to Vyanjana.SA
            else -> null
        }
    }

    private val eligiblePrefixes: List<List<Varna>> = listOf(
        listOf(Vyanjana.BHA, Svara.O),
        listOf(Vyanjana.BHA, Svara.A, Vyanjana.GA, Svara.O),
        listOf(Svara.A, Vyanjana.GHA, Svara.O),
    )
}
