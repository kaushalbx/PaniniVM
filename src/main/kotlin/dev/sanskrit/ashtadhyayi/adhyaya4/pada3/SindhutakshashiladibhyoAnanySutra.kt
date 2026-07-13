package dev.sanskrit.ashtadhyayi.adhyaya4.pada3

import dev.sanskrit.derivation.*
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.*

/** 4.3.93: सिन्धुतक्षशिलादिभ्योऽणञौ. */
object SindhutakshashiladibhyoAnanySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.93", text = "सिन्धुतक्षशिलादिभ्योऽणञौ",
    hindiExplanation = "अभिजन के अर्थ में सिन्ध्वादि से अण् और तक्षशिलादि से अञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430093,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private data class Selection(val gana: Int, val upadesha: String, val surface: String)
    private val selections = listOf(Selection(119, "अण्", "अ"), Selection(120, "अञ्", "अ"))
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.ABHIJANA).matches(context) && context.terms.any { term -> term.kind == TermKind.PRATIPADIKA && selections.any { GanaPatha.isEligibleMember(it.gana, term.surface, term.lexicalUses) } }
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context.copy(terms = context.terms.flatMap { term ->
        val selection = selections.firstOrNull { GanaPatha.isEligibleMember(it.gana, term.surface, term.lexicalUses) }
        if (term.kind == TermKind.PRATIPADIKA && selection != null && context.terms.none { it.id == "${term.id}-4-3-93" }) listOf(term, DerivationTerm("${term.id}-4-3-93", selection.surface, TermKind.PRATYAYA, upadesha = selection.upadesha)) else listOf(term)
    }), "4.3.93 selects अण् or अञ् for the corresponding abhijana Gaṇa.")
}
