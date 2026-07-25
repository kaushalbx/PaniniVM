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
 * Sūtra 3.1.36 इजादेश्च गुरुमतोऽनृच्छः.
 * Prescribes ām periphrastic affix in Liṭ for ijc-vowel initial heavy-syllable roots.
 */
object IjashChaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.36", text = "इजादेश्च गुरुमतोऽनृच्छः",
    hindiExplanation = "इच् (इ, उ, ऋ, ऌ, ए, ओ, ऐ, औ) से प्रारम्भ होने वाली गुरुमान धातुओं से लिट् लकार में 'आम्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310036,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT &&
        context.allEffectiveTerms.any { term -> term.kind == TermKind.DHATU && (term.upadesha in setOf("ईक्ष्", "ईक्षँ") || term.surface.startsWith("ई") || term.surface.startsWith("ऊ")) } &&
        context.allEffectiveTerms.none { it.upadesha == "आम्" }

    override fun apply(context: DerivationState): DerivationChange {
        val am = DerivationTerm("am", "आम्", TermKind.PRATYAYA, upadesha = "आम्")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(am),
            explanation = "3.1.36 prescribes आम् affix for ijc-initial roots in Liṭ.",
        )
    }
}
