package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.28 गुप्तूपविच्छिपणिपणिभ्य आयः.
 * Prescribes āya affix for gup, dhūp, vicch, paṇ, pan roots.
 */
object GupDhoopVichchhiPaniPanibhyOyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.28", text = "गुप्तूपविच्छिपणिपणिभ्य आयः",
    hindiExplanation = "गुप्, धूप, विच्छ्, पण् तथा पन् धातुओं से 'आय' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310028,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.any { it.upadesha in setOf("गुप्", "धूप", "विच्छ्", "पण्", "पन्") || it.surface in setOf("गुप्", "धूप", "विच्छ्", "पण्", "पन्") } &&
        context.allEffectiveTerms.none { it.upadesha == "आय" }

    override fun apply(context: DerivationState): DerivationChange {
        val aya = DerivationTerm("aya", "आय", TermKind.PRATYAYA, upadesha = "आय")
        return DerivationChange(
            state = context.addTerm(aya),
            explanation = "3.1.28 prescribes आय affix.",
        )
    }
}
