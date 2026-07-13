package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
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
 * 7.1.17: jasaḥ śī. 
 * Replaces the nominative plural affix 'jas' with 'śī' after a pronoun (sarvanāma) ending in 'a'.
 * This is an Apavāda to the general rule 6.1.102 (prathamayoḥ pūrvasavarṇaḥ).
 */
object JasahShiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.17",
    text = "जसः शी",
    hindiExplanation = "अकारान्त सर्वनाम के बाद 'जस' के स्थान पर 'शी' आदेश होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710017,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.27", "1.1.55")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false
        
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isSarvanama = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        val endsInA = stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        
        return isSarvanama && endsInA && affix.upadesha == "जस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        // 1.1.55: śī is śit, so it replaces the whole jas.
        // Surface becomes 'ī' after it-processing of 'ś'.
        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = "ई", upadesha = "शी")),
            explanation = "7.1.17: Substituted 'śī' for 'jas' after pronoun stem."
        )
    }
}
