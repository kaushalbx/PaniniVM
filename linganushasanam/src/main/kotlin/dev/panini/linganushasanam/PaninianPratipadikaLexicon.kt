package dev.panini.linganushasanam

import dev.panini.ganapatha.GanaPatha
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.PratipadikaLexicon
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon

/** Composes lexical metadata from Gaṇapāṭha and Liṅgānuśāsanam authorities. */
object PaninianPratipadikaLexicon : PratipadikaLexicon {
    private val lingaEngine = LinganushasanamEngine()

    override fun findPratipadika(text: String): PratipadikaEntry? {
        val normalized = text.trim()
        val base = StandardPratipadikaLexicon.findPratipadika(normalized)
        val ganas = GanaPatha.ganasContaining(normalized)
        val linga = lingaEngine.resolveOrNull(LingaRuleContext(normalized,
            pratyaya = base?.lingaPratyaya,
            semanticClasses = base?.lingaSemanticClasses.orEmpty()))
        if (base == null && ganas.isEmpty() && linga == null) return null

        return (base ?: PratipadikaEntry(normalized, emptySet())).copy(
            // An explicitly sourced lexical gender is stronger evidence than a
            // broad shape-based rule; the latter fills only undeclared gender.
            linga = base?.linga?.takeIf { it.isNotEmpty() } ?: linga?.lingas.orEmpty(),
            ganaNames = base?.ganaNames.orEmpty() + ganas.map { it.name },
            lingaRuleIds = base?.lingaRuleIds.orEmpty() + listOfNotNull(linga?.ruleId),
        )
    }

    /** Declared stems enriched with Liṅgānuśāsanam and sourced lexical gender. */
    fun declaredPratipadikas(): List<PratipadikaEntry> =
        StandardPratipadikaLexicon.declaredPratipadikas().map { requireNotNull(findPratipadika(it.text)) }
}
