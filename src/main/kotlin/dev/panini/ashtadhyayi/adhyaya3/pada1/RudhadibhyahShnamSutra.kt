package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.core.Lakara
import dev.panini.derivation.TermKind
import dev.panini.derivation.TingAffix
import dev.panini.core.DhatuGana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    /** Strong affixes for LAT and other lakāras (TIP, SIP, MIP). */
    private val latStrongAffixes = setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)

    /**
     * Strong affixes for LOT — same expanded set as Kryādi: the 1st-person
     * Ātmanepada affixes that receive ā-initial substitutes (3.4.92–3.4.93)
     * also condition guṇa/strong nasal in the imperative.
     */
    private val lotStrongAffixes = setOf(
        TingAffix.TIP, TingAffix.MIP,
        TingAffix.VAS, TingAffix.MAS,
        TingAffix.IT, TingAffix.VAHI, TingAffix.MAHING,
    )

    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == DhatuGana.RUDHADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्नम्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val affix = TingAffix.entries.single { it.upadesha == context.terms.last().upadesha }
        val strongAffixes = when (context.effectiveContext.rupa.lakara) {
            Lakara.LOT -> lotStrongAffixes
            Lakara.LING -> emptySet()
            else -> latStrongAffixes
        }
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
