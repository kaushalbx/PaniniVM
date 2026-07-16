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

/** 3.1.78: रुधादिभ्यः श्नम्. The nasal vikaraṇa is infixed after the root's final vowel. */
object RudhadibhyahShnamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.78",
    text = "रुधादिभ्यः श्नम्",
    hindiExplanation = "रुधादि-गण के धातुओं से परे श्नम् विकरण होता है और उसका नकार अन्तिम स्वर के बाद आता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310078,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DHATU,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    private val strongAffixes = setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)

    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.RUDHADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्नम्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val affix = TingAffix.entries.single { it.upadesha == context.terms.last().upadesha }
        val nasal = if (affix in strongAffixes) "न" else "न्"
        val insertionIndex = requireNotNull(finalVowelEnd(dhatu.surface)) {
            "3.1.78 requires a vowel in ${dhatu.surface}."
        }
        val infixed = dhatu.surface.substring(0, insertionIndex) + nasal + dhatu.surface.substring(insertionIndex)
        val shnam = DerivationTerm("shnam", "", TermKind.PRATYAYA, upadesha = "श्नम्")
        return DerivationChange(
            context.replaceTerm(dhatu.id, dhatu.copy(surface = infixed))
                .copy(droppedTerms = context.droppedTerms + shnam),
            "3.1.78 infixes the surviving $nasal of श्नम् after the final vowel of ${dhatu.surface}.",
        )
    }

    private fun finalVowelEnd(surface: String): Int? {
        val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')
        val index = surface.indexOfLast { it in vowels }
        return index.takeIf { it >= 0 }?.plus(1)
    }
}
