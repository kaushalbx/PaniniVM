package dev.panini.derivation

import dev.panini.ganapatha.GanaPatha
import dev.panini.linganushasanam.PaninianPratipadikaLexicon
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/**
 * Prātipadikas attested as members of nominal gaṇas. A member enters only when its gender
 * comes from a Liṅgānuśāsana sūtra or a sourced lexical gender — never the default fallback.
 * Indeclinable gaṇas (avyaya, nipāta, gati) have no gender, and gaṇas under adhyāya 3 list
 * dhātus, so neither supplies a nominal stem. Whether each stem's paradigm forward-derives is
 * checked by the lexicon build, which reports the rest rather than using them.
 */
object GanaPratipadikas {
    private val indeclinable = setOf(Samjna.AVYAYA, Samjna.NIPATA, Samjna.GATI)

    fun entries(): List<PratipadikaEntry> {
        val indeclinableGanas = GanaPatha.all.filter { gana -> gana.resultSamjnas.any { it in indeclinable } }
        return GanaPatha.all
            .filter { gana -> gana.resultSamjnas.none { it in indeclinable } && !gana.sutra.startsWith("3.") }
            .flatMap { gana -> gana.members.filter { !it.isInstruction && !it.isVartika && it.condition == null }
                .map { (it.upadesha ?: it.text).trim() } }
            .filter { it.isNotEmpty() && it.none(Char::isWhitespace) && indeclinableGanas.none { gana -> GanaPatha.contains(gana.sourceIndex, it) } && !dev.panini.ganapatha.SvaradiGana.contains(it) }
            .distinct()
            .mapNotNull { PaninianPratipadikaLexicon.findPratipadika(it) }
            .filter { it.linga.isNotEmpty() && it.lingaRuleIds.isNotEmpty() }
    }
}
