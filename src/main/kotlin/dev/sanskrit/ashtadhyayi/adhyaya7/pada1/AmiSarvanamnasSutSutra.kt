package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.52: āmi sarvanāmnas suṭ. 
 * Adds the augment 'suṭ' before the genitive plural affix 'ām' after a pronoun (sarvanāma) ending in 'a'.
 * This is an Apavāda to 7.1.54 (hrasva-nadī-āpo nuṭ).
 */
object AmiSarvanamnasSutSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.52",
    text = "आमि सर्वनाम्नः सुट्",
    hindiExplanation = "अकारान्त सर्वनाम के बाद 'आम्' को 'सुट्' आगम होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710052,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "1.1.27", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false
        
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must be a Sarvanāma
        val isSarvanama = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
        if (!isSarvanama) return false

        // 2. Stem must end in 'a'
        val endsInA = stem.surface.endsWith('अ') || stem.surface.endsWith('ा')
        
        // 3. Affix must be 'ām' and 'suṭ' not already added
        return endsInA && affix.upadesha == "आम्" && context.allEffectiveTerms.none { it.upadesha == "सुट्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // 1.1.46: Tit (marked with T) goes to the beginning (Adi) of the term it is attached to.
        val sutAugment = DerivationTerm(
            id = "sut-augment",
            surface = "स्", // 's' remains after it-processing (1.3.3, 1.3.2, 1.3.9)
            kind = TermKind.AGAMA,
            upadesha = "सुट्"
        )
        
        // Insert 'sut' before 'am'
        val newTerms = context.terms.dropLast(1) + sutAugment + context.terms.last()
        
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "7.1.52: Added 'suṭ' augment before 'ām' for pronoun stem."
        )
    }
}
