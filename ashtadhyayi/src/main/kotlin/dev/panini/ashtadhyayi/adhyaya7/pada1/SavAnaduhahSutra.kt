package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.MitAugment
import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/** 7.1.82: num after the final a-vowel of anaḍuh before su, following ām/am. */
object SavAnaduhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.82", text = "सावनडुहः",
    hindiExplanation = "सुँ परे अनडुह् के अन्त्य अवर्ण के बाद नुम् आगम होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false, kramaValue = 710082,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    blocks = setOf("6.1.68"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false
        val ending = context.terms.last()
        if (stem.kind != TermKind.PRATIPADIKA || stem.upadesha != "अनडुह्" ||
            ending.sourceSupAffix != SupAffix.SU || stem.itProcessingPending) return false
        val finalVowel = stem.varnas.lastOrNull { it is Svara }
        if (finalVowel != Svara.A && finalVowel != Svara.AA) return false
        return context.allEffectiveTerms.none { it.createdBySutra == sutra && it.augmentTargetId == stem.id }
    }
    override fun apply(context: DerivationState): DerivationChange {
        require(matches(context))
        val stem = context.terms[context.terms.lastIndex - 1]
        return DerivationChange(context.addTerm(
            MitAugment.NUM.rawTerm("num:${stem.id}", sutra).copy(augmentTargetId = stem.id)),
            "7.1.82 introduces num; u is pronunciation-only, m awaits independent it designation.")
    }
}
