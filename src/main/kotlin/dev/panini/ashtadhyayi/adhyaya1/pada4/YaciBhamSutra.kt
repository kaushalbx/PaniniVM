package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.4.18: yaci bham.
 * Before a suffix starting with 'y' or a vowel (ac),
 * the preceding stem is called 'bha'.
 * This rule belongs to the 'Ekā Saṃjñā' (1.4.1) section,
 * meaning it overrides 'pada' status (1.4.14).
 */
object YaciBhamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.18",
    text = "यचि भम्",
    hindiExplanation = "यकार या अच् (स्वर) से शुरू होने वाले स्वादि प्रत्ययों के परे पूर्व शब्द-स्वरूप की 'भ' संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140018,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("1.4.14") // Overrides Pada status
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Check if affix starts with 'y' or Ac
        val firstChar = affix.surface.firstOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        val isYOrAc = firstChar == 'य' || engine.contains(Pratyahara.AC, firstChar)

        if (!isYOrAc) return false

        // 2. Affix must be a 'svadi' affix (sup, etc.)
        val isSvadi = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }

        return isSvadi && context.samjnas.none { it.targetId == stem.id && it.samjna == Samjna.BHA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val assignment = SamjnaAssignment(stem.id, Samjna.BHA)

        return DerivationChange(
            state = context.withSamjnas(setOf(assignment)),
            explanation = "1.4.18 assigns 'bha' संज्ञा to the stem before a y/ac-initial suffix."
        )
    }
}
