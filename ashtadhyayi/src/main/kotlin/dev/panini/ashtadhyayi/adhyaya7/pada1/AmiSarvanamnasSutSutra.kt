package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    dependencies = setOf("6.4.1", "1.1.27", "1.1.46"),
    blocks = setOf("7.1.54"),
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
        val endsInA = stem.varnas.lastOrNull() in setOf(Svara.A, Svara.AA)

        // 3. Affix must be 'ām' and 'suṭ' not already added
        return endsInA && affix.sourceSupAffix == dev.panini.core.SupAffix.AM_6 &&
            affix.upadesha == "आम्" && "7.1.52" !in affix.establishedBySutras &&
            context.allEffectiveTerms.none { it.createdBySutra == sutra && it.augmentTargetId == affix.id }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // 1.1.46: Tit (marked with T) goes to the beginning (Adi) of the term it is attached to.
        val sutAugment = DerivationTerm(
            id = "sut-augment",
            surface = "सुँट्",
            kind = TermKind.AGAMA,
            upadesha = "सुँट्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = context.terms.last().id,
            mergeIntoAugmentTarget = true,
        )

        // Insert 'sut' before 'am'
        val newTerms = context.terms + sutAugment

        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "7.1.52: Added 'suṭ' augment before 'ām' for pronoun stem."
        )
    }
}
