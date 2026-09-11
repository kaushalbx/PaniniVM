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
        val linga = lingaEngine.resolveOrNull(LingaRuleContext(normalized))
        if (base == null && ganas.isEmpty() && linga == null) return null

        return (base ?: PratipadikaEntry(normalized, emptySet())).copy(
            linga = base?.linga.orEmpty() + listOfNotNull(linga?.linga),
            ganaNames = base?.ganaNames.orEmpty() + ganas.map { it.name },
            lingaRuleIds = base?.lingaRuleIds.orEmpty() + listOfNotNull(linga?.ruleId),
        )
    }
}
