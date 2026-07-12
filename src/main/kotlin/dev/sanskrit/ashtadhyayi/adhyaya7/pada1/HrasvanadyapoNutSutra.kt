package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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

        // Match short 'a' for Rama
        val isShortVowel = stem.surface.endsWith('अ')
        
        return isShortVowel && affix.upadesha == "आम्" && context.terms.none { it.upadesha == "नुट्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // 1.1.46: Tit (marked with T) goes to the beginning (Adi) of the term it is attached to.
        val nut = DerivationTerm(
            id = "nut-augment",
            surface = "नुट्",
            kind = TermKind.AGAMA,
            upadesha = "नुट्"
        )
        
        // Insert 'nut' before 'am'
        val newTerms = context.terms.dropLast(1) + nut + context.terms.last()
        
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "7.1.54: Added 'nuṭ' augment before 'ām' (Ṭit = Ādi)."
        )
    }
}
