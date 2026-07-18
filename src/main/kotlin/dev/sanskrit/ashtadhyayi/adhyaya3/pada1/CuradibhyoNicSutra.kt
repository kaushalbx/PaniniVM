package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.25: सत्यापपाशरूपवीणातूलश्लोकसेनालोमत्वचवर्मवर्णचूर्णचुरादिभ्यो णिच्. */
object CuradibhyoNicSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.25",
    text = "सत्यापपाशरूपवीणातूलश्लोकसेनालोमत्वचवर्मवर्णचूर्णचुरादिभ्यो णिच्",
    hindiExplanation = "चुरादि-गण के धातुओं से परे स्वार्थ में णिच् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310025,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.CURADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "णिच्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        // Effective अय् reflects it-lopa and 6.1.77 before the following शप् vowel.
        val nic = DerivationTerm("nic", "अय्", TermKind.PRATYAYA, upadesha = "णिच्")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(nic),
            explanation = "3.1.25 introduces णिच् after a Curādi root.",
        )
    }
}
