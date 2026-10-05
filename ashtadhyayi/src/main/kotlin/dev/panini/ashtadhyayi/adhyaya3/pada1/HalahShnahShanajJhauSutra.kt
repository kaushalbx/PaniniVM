package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** 3.1.83: consonant-final root + śnā becomes śānac before hi. */
object HalahShnahShanajJhauSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.83", text = "हलः श्नः शानज्झौ",
    hindiExplanation = "हलन्त धातु से परे श्ना को हि परे शानच् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 3, pada = 1, optional = false,
    kramaValue = 310083, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("6.4.112", "6.4.113"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (index < 0) return false
        return context.terms[index].varnas.lastOrNull() is Vyanjana &&
            context.terms.getOrNull(index + 1)?.matchesUpadesha("श्ना") == true &&
            context.terms.getOrNull(index + 2)?.surface == "हि" &&
            sutra !in context.appliedSutras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.first { it.matchesUpadesha("श्ना") }
        return DerivationChange(
            context.replaceWholeAffix(affix.id, "शानच्", sutra, WholeAffixDesignationPolicy.FreshUpadesha),
            "3.1.83 introduces raw शानच् in place of श्ना before हि after a consonant-final root.",
        )
    }
}
