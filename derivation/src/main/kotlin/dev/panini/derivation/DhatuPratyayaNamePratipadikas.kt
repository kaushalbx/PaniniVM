package dev.panini.derivation

import dev.panini.core.KrtAffix
import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.SanadiAffix
import dev.panini.core.TaddhitaAffix
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/**
 * Dhātus and pratyayas as the sūtras name them, each its own name by 1.1.68 स्वं रूपं
 * शब्दस्याशब्दसंज्ञा. Every stem comes from an existing inventory, never a typed list:
 * - a dhātu by its marker-free form (ब्रू → ब्रुवः, दिव् → दिवः, क्रीड् → क्रीडः), or by an upadeśa that
 *   closes on a consonant it-letter without a nasalised vowel (कृञ् → कृञः);
 * - an affix by its upadeśa (क्त → क्तः, यङ् → यङः).
 * They are masculine, as 2.4.53 ब्रुवो वचिः shows (वचिः, a masculine nominative). A name that is also
 * a svarādi member (यम्, रम्) is left out: 1.1.37 would make it that indeclinable, not the name.
 */
object DhatuPratyayaNamePratipadikas {
    private const val NASALISED = 'ँ'

    fun dhatuNames(): List<String> = DhatuPatha.all.flatMap { dhatu ->
        listOf(dhatu.sourceSurface) + listOf(dhatu.upadesha).filter { NASALISED !in it && it.endsWith("्") }
    }.filter { it.isNotBlank() }.distinct()

    fun pratyayaNames(): List<String> = (KrtAffix.entries.map { it.upadesha } + TaddhitaAffix.entries.map { it.upadesha } +
        SanadiAffix.entries.map { it.upadesha }).filter { it.isNotBlank() && NASALISED !in it }.distinct()

    /**
     * Dhātus named with इक् (vārttika इक्श्तिपौ धातुनिर्देशे), each derived by the kṛdanta engine.
     * Only consonant-final roots: the grammar names a vowel-final root with श्तिप् (जयति, not जी).
     */
    fun ikNames(): List<String> = DhatuPatha.all.filter { it.derivationalSurface.removeSuffix("्").lastOrNull()?.let(dev.panini.shiksha.Varnamala::isConsonant) == true }.mapNotNull { dhatu ->
        runCatching { KrdantaEngine().deriveSourceStem(dhatu.upadesha, KrtAffix.IK.upadesha) }.getOrNull()
            ?.let { it as? KrdantaSourceStem.Productive }?.surface
    }.distinct()

    /** Upasargas by their upadeśa (नि → नेः, आङ् → आङः, उद् → उदः in 1.3.17, 1.3.20, 1.3.24), masculine likewise. */
    fun upasargaNames(): List<String> = dev.panini.core.Upasarga.entries.map { it.upadesha }

    fun entries(): List<PratipadikaEntry> = (dhatuNames() + pratyayaNames() + ikNames() + upasargaNames()).distinct()
        .filterNot { dev.panini.ganapatha.SvaradiGana.contains(it) }.map { name ->
        PratipadikaEntry(name, setOf(Linga.PUMS), categories = setOf(NominalCategory.TECHNICAL_SAMJNA))
    }
}
