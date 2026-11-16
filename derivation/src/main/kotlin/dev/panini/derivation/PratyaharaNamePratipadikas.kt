package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.pratyahara.PratyaharaEngine
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/**
 * The Aṣṭādhyāyī's names for sounds, built from varṇas, never typed letters. Each names its own
 * form (1.1.68 स्वं रूपं शब्दस्याशब्दसंज्ञा) and is masculine, as the sūtras' agreement shows.
 */
object PratyaharaNamePratipadikas {
    /** 1.1.71 आदिरन्त्येन सहेता, from the Māheśvara sūtras: अच्, हल्. Masculine by 1.1.7 हलोऽनन्तराः संयोगः. */
    fun pratyaharas(engine: PratyaharaEngine = PratyaharaEngine()): List<List<Varna>> = engine.names()

    /** A consonant names itself (हः, नः); masculine by 8.2.31 हो ढः. A short vowel declines as a vowel stem: एः, ओः. */
    fun letters(): List<List<Varna>> = Vyanjana.entries.map { listOf(it) } +
        listOf(Svara.I, Svara.U, Svara.R).map { listOf(it) }

    /** 1.1.70 तपरस्तत्कालस्य: a vowel with त् names that vowel of its length (अत्, ऋत्); masculine by 7.3.101 अतो दीर्घो यञि. */
    fun taparas(): List<List<Varna>> = listOf(Svara.A, Svara.AA, Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR,
        Svara.E, Svara.AI, Svara.O, Svara.AU).map { listOf(it, Vyanjana.TA) }

    fun entries(engine: PratyaharaEngine = PratyaharaEngine()): List<PratipadikaEntry> =
        (pratyaharas(engine) + letters() + taparas()).map { varnas ->
            PratipadikaEntry(varnas.toDevanagari(), setOf(Linga.PUMS), categories = setOf(NominalCategory.TECHNICAL_SAMJNA))
        }.distinctBy { it.text }
}
