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
        val left = context.terms[context.terms.size - 2]
        val newSurface = left.surface.dropLast(1) + "ु"

        return DerivationChange(
            state = context.replaceTerm(left.id, left.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.114: Substituted 'u' for 'ru' before a voiced consonant."
        )
    }
}
