package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.27")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Jurisdictional check
        if ("6.4.1" !in context.activeAdhikaras) return false

        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must be a Sarvanāma
        val isSarvanama = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        if (!isSarvanama) return false

        // 2. Stem must end in 'a'
        val endsInA = stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        
        // 3. Affix must be 'ṅe'
        return endsInA && affix.upadesha == "ङे"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "स्मै")),
            explanation = "7.1.14 substitutes 'smai' for dative-singular 'ṅe' after a pronoun stem."
        )
    }
}
