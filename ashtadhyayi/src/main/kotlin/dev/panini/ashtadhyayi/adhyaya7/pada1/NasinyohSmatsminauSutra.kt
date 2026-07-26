package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.ashtadhyayi.adhyaya1.pada3.YathasamkhyamSutra
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
 * 7.1.15: ṅasīṅyoḥ smātsminau.
 * Replaces ablative-singular 'ṅasi' and locative-singular 'ṅi' with 'smāt' and 'smin'
 * respectively, after a pronoun (sarvanāma) ending in 'a'.
 * Uses 1.3.10 (Yathāsaṃkhyam) logic.
 */
object NasinyohSmatsminauSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.15",
    text = "ङसिङ्योः स्मात्स्मिनौ",
    hindiExplanation = "अकारान्त सर्वनाम के बाद 'ङसि' और 'ङि' के स्थान पर क्रमशः 'स्मात्' और 'स्मिन्' आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710015,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.27", "1.3.10")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isSarvanama = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        if (!isSarvanama) return false

        val endsInA = stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        return endsInA && affix.upadesha in sources
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = requireNotNull(YathasamkhyamSutra.map(affix.upadesha, sources, targets))

        return DerivationChange(
            state = context.replaceTerm(affix.id, affix.copy(surface = replacement)),
            explanation = "7.1.15: Substituted $replacement for ${affix.upadesha} after pronoun stem."
        )
    }

    private val sources = listOf("ङसि", "ङि")
    private val targets = listOf("स्मात्", "स्मिन्")
}
