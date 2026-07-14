package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.13: ṅer yaḥ. 
 * Replaces the dative singular affix 'ṅe' with 'ya' after an a-ending stem.
 */
object NgeryahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.13",
    text = "ङेर्यः",
    hindiExplanation = "अकारान्त अङ्ग के बाद 'ङे' के स्थान पर 'य' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710013,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Jurisdictional check
        if ("6.4.1" !in context.activeAdhikaras) return false

        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in 'a'
        val endsInA = dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface) &&
            !dev.sanskrit.shiksha.Varnamala.endsWithAA(stem.surface)
        
        // 2. Affix must be 'ṅe' (upadesha)
        return endsInA && affix.upadesha == "ङे" && affix.surface != "य"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "य")),
            explanation = "7.1.13 substitutes 'ya' for the dative-singular 'ṅe' after a-stem."
        )
    }
}
