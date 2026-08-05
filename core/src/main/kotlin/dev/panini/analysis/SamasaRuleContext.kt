package dev.panini.analysis

import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna

/**
 * A single member (pada) of a compound, carrying its base stem, upadesha, vibhakti, and saṃjñās.
 *
 * @param upadesha  Canonical base form of the stem (e.g. "राज", "पुरुष", "उप").
 * @param vibhakti  The grammatical case this pada carries in the laukika vigraha.
 * @param samjnas   Saṃjñās assigned to this pada (e.g. PRATIPADIKA, AVYAYA, UPASARGA).
 */
data class SamasaPada(
    val upadesha: String,
    val vibhakti: Vibhakti = Vibhakti.PRATHAMA,
    val samjnas: Set<Samjna> = emptySet(),
)

/**
 * The input context provided to every Samāsa Sūtra (Adhyāyas 2.1–2.2).
 * Carries all information needed for principled Pāṇinian matching
 * without surface-string heuristics.
 *
 * @param padas       Ordered list of compound members (pūrvapada first).
 * @param samasaType  The macro compound type declared by the caller.
 */
data class SamasaRuleContext(
    val padas: List<SamasaPada>,
    val samasaType: SamasaType,
) {
    val purvaPada: SamasaPada get() = padas.first()
    val uttaraPada: SamasaPada get() = padas.last()
    val purvaPadaVibhakti: Vibhakti get() = purvaPada.vibhakti
}
