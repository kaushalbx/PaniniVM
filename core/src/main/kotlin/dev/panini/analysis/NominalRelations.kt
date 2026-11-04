package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti

/** Lexical possibilities, independent of the grammatical role in this clause. */
enum class NominalUse { ENTITY, MODIFIER, ENTITY_OR_MODIFIER }
enum class NominalMeaning { PROPERTY, CARDINAL, ORDINAL, QUANTITY, REFERENCE, ORIGIN }
enum class NominalRelationKind { ATTRIBUTIVE, PREDICATIVE }

data class NominalAgreement(val linga: Linga, val vacana: Vacana, val vibhakti: Vibhakti)

/** Indices address the frame's nominalPadas, retaining repeated identical words. */
data class NominalAttachment(
    val modifier: Int,
    val head: Int,
    val kind: NominalRelationKind,
    val agreements: Set<NominalAgreement>,
    val meanings: Set<NominalMeaning>,
    val explanation: String,
)

data class NominalRelationAnalysis(
    val nominalPadas: List<SubantaAnalysis>,
    /** Candidates, not asserted dependencies: matching morphology alone is insufficient. */
    val candidates: List<NominalAttachment>,
    val unattachedModifiers: Set<Int>,
    /** A substantivized adjective/pronoun may be an independent participant. */
    val independentReadings: Set<Int> = emptySet(),
)

object NominalRelationAnalyzer {
    fun analyze(padas: List<SubantaAnalysis>, permitsPredication: Boolean = false): NominalRelationAnalysis {
        val candidates = buildList {
            padas.forEachIndexed { modifierIndex, modifier ->
                val lexical = modifier.lexicalEntry ?: return@forEachIndexed
                if (lexical.nominalUse == NominalUse.ENTITY) return@forEachIndexed
                padas.forEachIndexed headLoop@{ headIndex, head ->
                    if (headIndex == modifierIndex || head.lexicalEntry?.nominalUse == NominalUse.MODIFIER) return@headLoop
                    val genders = modifier.linga.intersect(head.linga)
                    val agreements = buildSet {
                        modifier.supCandidates.forEach { m -> head.supCandidates.forEach { h ->
                            if (m.vibhakti == h.vibhakti && m.vacana == h.vacana) {
                                genders.forEach { add(NominalAgreement(it, m.vacana, m.vibhakti)) }
                            }
                        } }
                    }
                    if (agreements.isEmpty()) return@headLoop
                    add(NominalAttachment(
                        modifierIndex, headIndex, NominalRelationKind.ATTRIBUTIVE, agreements,
                        lexical.nominalMeanings,
                        "Lexically licensed qualification with compatible liṅga, vacana and vibhakti; position is unrestricted.",
                    ))
                    val nominative = agreements.filterTo(mutableSetOf()) { it.vibhakti == Vibhakti.PRATHAMA }
                    if (permitsPredication && nominative.isNotEmpty()) add(NominalAttachment(
                        modifierIndex, headIndex, NominalRelationKind.PREDICATIVE, nominative,
                        lexical.nominalMeanings,
                        "A nominative qualification may predicate a property in a nominal or copular clause.",
                    ))
                }
            }
        }
        val attached = candidates.mapTo(mutableSetOf()) { it.modifier }
        return NominalRelationAnalysis(padas, candidates, padas.indices.filterTo(mutableSetOf()) {
            padas[it].lexicalEntry?.nominalUse == NominalUse.MODIFIER && it !in attached
        }, padas.indices.filterTo(mutableSetOf()) {
            padas[it].lexicalEntry?.nominalUse == NominalUse.ENTITY_OR_MODIFIER
        })
    }
}
