package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
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
 * Sūtra 3.1.35 कासप्रत्ययादाममन्त्रे लिटि.
 * Prescribes ām periphrastic affix in Liṭ for kāsa root and derived roots.
 */
object KasPratyayadAmAmantreLitSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.35", text = "कासप्रत्ययादाममन्त्रे लिटि",
    hindiExplanation = "लिट् लकार परे रहते कास् धातु तथा प्रत्ययान्त धातुओं से 'आम्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310035,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT &&
        context.allEffectiveTerms.any { it.upadesha == "कास्" || it.surface == "कास्" } &&
        context.allEffectiveTerms.none { it.upadesha == "आम्" }

    override fun apply(context: DerivationState): DerivationChange {
        val am = DerivationTerm("am", "आम्", TermKind.PRATYAYA, upadesha = "आम्")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(am),
            explanation = "3.1.35 prescribes आम् affix in Liṭ.",
        )
    }
}
