package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.54: hrasva-nadī-āpo nuṭ.
 * Adds the augment 'nuṭ' before the genitive plural affix 'ām' after a short vowel,
 * a nadī-designated term, or an āp-ending term.
 */
object HrasvanadyapoNutSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.54",
    text = "ह्रस्वनद्यापो नुट्",
    hindiExplanation = "ह्रस्व स्वरांत, नदी संज्ञक या आप् प्रत्ययांत अङ्ग के बाद आम् को नुट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710054,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val governedBySatCatur = stem.upadesha in
            setOf("चतुर्", "षट्", "पञ्चन्", "सप्तन्", "अष्टन्", "नवन्", "दशन्") ||
            stem.surface in setOf("चतुर्", "षट्") ||
            context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SHAT }
        if (governedBySatCatur) return false

        // Match any short vowel or ā-stem (āp-stem)
        val lastChar = stem.surface.lastOrNull() ?: return false
        val isShortVowel = lastChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks ||
                lastChar in setOf('इ', 'ि', 'उ', 'ु', 'ऋ', 'ृ', 'ऌ', 'ॢ')
        val isApStem = lastChar == 'ा' || lastChar == 'आ'

        return (isShortVowel || isApStem) && affix.upadesha == "आम्" &&
            sutra !in affix.establishedBySutras &&
            context.allEffectiveTerms.none { it.upadesha == "नुँट्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val nut = DerivationTerm(
            id = "${affix.id}-nut",
            surface = "नुँट्",
            kind = TermKind.AGAMA,
            upadesha = "नुँट्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = affix.id,
            mergeIntoAugmentTarget = true,
        )

        return DerivationChange(
            state = context.addTerm(nut),
            explanation = "7.1.54 introduces raw नुँट् for placement before आम्."
        )
    }
}
