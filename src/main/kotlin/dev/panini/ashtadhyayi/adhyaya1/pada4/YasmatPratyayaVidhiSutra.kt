package dev.panini.ashtadhyayi.adhyaya1.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.derivation.SamjnaAssignment
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.4.13: yasmāt pratyayavidhis tadādi pratyaye'ṅgam.
 * That after which an affix is ordained is called an 'aṅga' (stem) with respect to that affix.
 */
object YasmatPratyayaVidhiSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.4.13",
    text = "यस्मात्प्रत्ययविधिस्तदादि प्रत्ययेऽङ्गम्",
    hindiExplanation = "जिस शब्द-स्वरूप के बाद प्रत्यय का विधान किया गया हो, उस प्रत्यय के परे होने पर उस आदि शब्द-स्वरूप की अङ्ग संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 4,
    optional = false,
    kramaValue = 140013,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // Check if affix is designated as a PRATYAYA
        val isPratyaya = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }

        return isPratyaya && context.samjnas.none { it.targetId == stem.id && it.samjna == Samjna.ANGA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val assignment = SamjnaAssignment(stem.id, Samjna.ANGA)

        return DerivationChange(
            state = context.withSamjnas(setOf(assignment)),
            explanation = "1.4.13 assigns 'aṅga' संज्ञा to the stem before the affix."
        )
    }
}
