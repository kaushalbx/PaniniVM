package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.14: sarvanāmnas smai.
 * Replaces the dative singular affix 'ṅe' with 'smai' after a pronoun (sarvanāma) ending in 'a'.
 * This is an Apavāda to 7.1.13 (ṅer yaḥ).
 */
object SarvanamnasSmaiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.14",
    text = "सर्वनाम्नः स्मै",
    hindiExplanation = "अकारान्त सर्वनाम के बाद 'ङे' के स्थान पर 'स्मै' आदेश होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710014,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.27")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isTyadadi = stem.upadesha in setOf("त्यद्", "तद्", "यद्", "एतद्", "किम्", "इदम्")
        val isSarvanama = isTyadadi || context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        val matras = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'े', 'ै', 'ो', 'ौ', '्')
        val endsInA = isTyadadi || (stem.surface.isNotEmpty() && stem.surface.last() !in matras)

        return isSarvanama && endsInA && (affix.upadesha == "ङे" || affix.id == "sup-nge")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isTyadadi = stem.upadesha in setOf("त्यद्", "तद्", "यद्", "एतद्", "किम्", "इदम्")
        var newState = context
        if (isTyadadi && stem.surface.endsWith("्")) {
            val aStemSurface = if (stem.surface.endsWith("्")) stem.surface.dropLast(2) else stem.surface
            newState = newState.replaceTerm(stem.id, stem.copy(surface = aStemSurface))
        }

        return DerivationChange(
            state = newState.replaceTerm(affix.id, affix.copy(surface = "स्मै")),
            explanation = "7.1.14 substitutes 'smai' for dative-singular 'ṅe' after a pronoun stem."
        )
    }
}
