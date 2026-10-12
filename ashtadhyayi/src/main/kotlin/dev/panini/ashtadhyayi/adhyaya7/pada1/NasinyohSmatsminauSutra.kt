package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.ashtadhyayi.adhyaya1.pada3.YathasamkhyamSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.core.SupAffix
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.hasCurrentAffix
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
    role = SutraRole.Apavada,
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

        val isTyadadi = stem.upadesha in setOf("त्यद्", "तद्", "यद्", "एतद्", "किम्", "इदम्")
        val isSarvanama = isTyadadi || context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        val endsInA = isTyadadi || stem.varnas.lastOrNull() == Svara.A

        if (affix.varnas in targets) return false

        return isSarvanama && endsInA && source(affix) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val lookupKey = source(affix)
        val replacement = requireNotNull(YathasamkhyamSutra.map(lookupKey, sources, targets))

        return DerivationChange(
            state = context.replaceWholeAffix(affix.id, replacement, sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume),
            explanation = "7.1.15: Substituted ${if (lookupKey == SupAffix.NGASI) "स्मात्" else "स्मिन्"} for ${affix.upadesha} after pronoun stem."
        )
    }

    private val sources = listOf(SupAffix.NGASI, SupAffix.NGI)
    private val targets = listOf(SupSubstitutionVarnas.smat, SupSubstitutionVarnas.smin)

    private fun source(affix: DerivationTerm): SupAffix? =
        affix.sourceSupAffix?.takeIf { it in sources } ?: sources.singleOrNull { affix.hasCurrentAffix(it) }
}
