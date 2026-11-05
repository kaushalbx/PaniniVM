package dev.panini.analysis

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

enum class ParticipialTime { PRIOR, SIMULTANEOUS }

/**
 * How a declined participle's action stands to the finite action, read from morphology alone.
 * Indices point into the frame's nominal occurrences. An absolute participle (2.3.37) has its own bearer (भाववत्) in the locative,
 * which is its object in गोषु दुह्यमानासु; otherwise [bearer] is the agreeing noun it is said of, if written.
 */
data class ParticipialRelation(
    val participle: Int,
    val time: ParticipialTime,
    val absolute: Boolean,
    val bearer: Int?,
    /** The bearer is the participle's kartā unless a past participle with no object of its own says what was done to it. */
    val bearerRole: dev.panini.core.Karaka? = null,
)

val KriyaFrame.participialRelations: List<ParticipialRelation>
    get() {
        val nominals = nominalRelations?.nominalPadas.orEmpty()
        fun affix(index: Int) = ((nominals[index].pada as? SubantaPada)?.pratipadika as? KridantaPratipadika)?.krtPratyaya
        fun agrees(first: Int, second: Int) = nominals[first].supCandidates.any { one ->
            nominals[second].supCandidates.any { other -> one.vibhakti == other.vibhakti && one.vacana == other.vacana } }
        fun isCase(index: Int, case: Vibhakti) = nominals[index].supCandidates.isNotEmpty() &&
            nominals[index].supCandidates.all { it.vibhakti == case }
        return nominals.indices.mapNotNull { index ->
            val present = affix(index) in setOf("शतृ", "शानच्")
            if (!present && affix(index) != "क्त") return@mapNotNull null
            val nouns = nominals.indices.filter { it != index && affix(it) !in setOf("शतृ", "शानच्", "क्त") && agrees(index, it) }
            val locative = isCase(index, Vibhakti.SAPTAMI)
            when {
                locative && nouns.size == 1 -> ParticipialRelation(index,
                    if (present) ParticipialTime.SIMULTANEOUS else ParticipialTime.PRIOR, true, nouns.single(),
                    if (present || (index > 0 && isCase(index - 1, Vibhakti.DVITIYA))) dev.panini.core.Karaka.KARTR else dev.panini.core.Karaka.KARMAN)
                present && !locative -> ParticipialRelation(index, ParticipialTime.SIMULTANEOUS, false, nouns.singleOrNull())
                else -> null
            }
        }
    }
