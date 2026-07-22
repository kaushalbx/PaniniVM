package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.Samjna

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
    nimittaScope = NimittaScope.BOTH
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (internalSankhyaIndex(context) >= 0) return true
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left term ends in 'r' (from ru) preceded by 'a'
        val surface = left.surface
        if (!surface.endsWith('र')) return false
        if (surface.length < 2 || surface[surface.length - 2] != 'अ') return false

        // 2. Followed by a voiced consonant (haś)
        val firstChar = right.surface.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAS, firstChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val internalIndex = internalSankhyaIndex(context)
        val left = if (internalIndex >= 0) context.terms[internalIndex] else context.terms[context.terms.size - 2]
        val newSurface = left.surface.dropLast(1) + "ु"

        return DerivationChange(
            state = context.replaceTerm(left.id, left.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.114: Substituted 'u' for 'ru' before a voiced consonant."
        )
    }

    private fun internalSankhyaIndex(context: DerivationState): Int = context.terms.indices.firstOrNull { index ->
        index < context.terms.lastIndex && context.terms[index].surface.endsWith("र") &&
            dev.panini.shiksha.Varnamala.endsWithA(context.terms[index].surface.dropLast(1)) &&
            context.samjnas.any { it.targetId == context.terms[index].id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == context.terms[index + 1].id && it.samjna == Samjna.SANKHYA } &&
            context.terms[index + 1].surface.firstOrNull()?.let {
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAS, it)
            } == true
    } ?: -1
}
