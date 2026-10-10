package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.114: haśi ca.
 * After a short 'a' (at), the substitute 'ru' (from 8.2.66) becomes 'u'
 * when followed by a voiced consonant (haś).
 */
object HashiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.114",
    text = "हशि च",
    hindiExplanation = "अप्लुतः अकार के बाद 'रु' को 'उ' आदेश होता है यदि बाद में हश् (सघोष व्यञ्जन) हो।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610114,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = dev.panini.sutra.SutraStage.POST_RUTVA,
    nimittaScope = NimittaScope.BOTH
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (internalSankhyaIndex(context) >= 0) return true
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left term ends in repha (from ru) preceded by 'a'
        if (left.varnas.takeLast(2) != listOf(Svara.A, Vyanjana.RA)) return false
        if (context.substitutions.none { it.targetId == left.id && it.sutra == "8.2.66" }) return false

        // 2. Followed by a voiced consonant (haś)
        val first = right.varnas.firstOrNull() as? Vyanjana ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAS, first)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val internalIndex = internalSankhyaIndex(context)
        val left = if (internalIndex >= 0) context.terms[internalIndex] else context.terms[context.terms.size - 2]
        val newSurface = (left.varnas.dropLast(2) + Svara.U).toDevanagari()

        return DerivationChange(
            state = context.substituteTermSurface(left.id, newSurface, Vyanjana.RA, listOf(Svara.U), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.114: Substituted 'u' for 'ru' before a voiced consonant."
        )
    }

    private fun internalSankhyaIndex(context: DerivationState): Int = context.terms.indices.firstOrNull { index ->
        index < context.terms.lastIndex && context.terms[index].varnas.takeLast(2) == listOf(Svara.A, Vyanjana.RA) &&
            context.substitutions.any { it.targetId == context.terms[index].id && it.sutra == "8.2.66" } &&
            context.samjnas.any { it.targetId == context.terms[index].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == context.terms[index + 1].id && it.samjna == Samjna.SANKHYA } &&
            (context.terms[index + 1].varnas.firstOrNull() as? Vyanjana)?.let {
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAS, it)
            } == true
    } ?: -1
}
