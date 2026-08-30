package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.113: ato roraplūtādaplute.
 * After a short 'a' (at), the substitute 'ru' (from 8.2.66) becomes 'u'
 * when followed by another short 'a' (at).
 */
object AtoRorAplutadSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.113",
    text = "अतो रोरप्लुतादप्लुते",
    hindiExplanation = "अप्लुतः अकार के बाद 'रु' को 'उ' आदेश होता है यदि बाद में भी अप्लुतः अकार हो।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610113,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    nimittaScope = NimittaScope.BOTH
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left term must end in repha produced from ru by इत्-processing.
        // 2. Preceded by short 'a'
        val surface = left.surface
        if (!surface.endsWith("र्")) return false
        if (!Varnamala.endsWithA(surface.dropLast(2))) return false

        // 3. Followed by short 'a'
        return right.surface.startsWith('अ')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val left = context.terms[context.terms.size - 2]
        // Replace 'r' with 'u'
        val newSurface = left.surface.dropLast(2) + "ु"

        return DerivationChange(
            state = context.replaceTerm(left.id, left.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.1.113: Substituted 'u' for 'ru' between two short 'a's."
        )
    }
}
