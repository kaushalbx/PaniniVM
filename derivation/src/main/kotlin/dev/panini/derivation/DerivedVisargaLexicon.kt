package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import java.text.Normalizer

/** Forward-verified candidates under an explicit lexicon, not an exhaustive inverse analyzer.
 * The legacy visarga view and full nominal view share the same source derivations.
 * No expected sandhi output participates in construction or lookup. */
class DerivedVisargaLexicon private constructor(
    private val bySurface: Map<String, List<Candidate>>,
    private val nominalBySurface: Map<String, List<NominalCandidate>>,
    val failures: List<Failure>,
    val entriesWithoutGender: List<PratipadikaEntry>,
    val unresolvedSources: List<UnresolvedSource>,
) {
    data class Candidate(val entry: PratipadikaEntry, val request: SubantaDerivationRequest,
        val derivation: DerivationResult, val pada: SandhiEngine.Pada, val source: VisargaSource)
    /** A complete forward nominal proof. Non-visarga words have no visarga source. */
    data class NominalCandidate(val entry: PratipadikaEntry, val request: SubantaDerivationRequest,
        val derivation: DerivationResult, val pada: SandhiEngine.Pada)
    data class Failure(val request: SubantaDerivationRequest, val errorType: String, val message: String?)
    data class UnresolvedSource(val request: SubantaDerivationRequest, val derivation: DerivationResult)

    /** Homographic cases/genders retain separate derivations; no first-match source guess. */
    fun candidates(surface: String): List<Candidate> = bySurface[normalized(surface)].orEmpty()
    val surfaces: Set<String> get() = bySurface.keys.toSet()
    fun nominalCandidates(surface: String): List<NominalCandidate> = nominalBySurface[normalized(surface)].orEmpty()
    val nominalSurfaces: Set<String> get() = nominalBySurface.keys.toSet()

    companion object {
        private fun normalized(text: String) = Normalizer.normalize(text, Normalizer.Form.NFC)

        fun build(entries: Collection<PratipadikaEntry>,
            nominalEngine: SubantaEngine = SubantaEngine(), sandhiEngine: SandhiEngine = SandhiEngine()): DerivedVisargaLexicon {
            val index = mutableMapOf<String, MutableList<Candidate>>()
            val nominalIndex = mutableMapOf<String, MutableList<NominalCandidate>>()
            val failures = mutableListOf<Failure>()
            val unresolved = mutableListOf<UnresolvedSource>()
            for (entry in entries.distinct()) {
                for (linga in entry.linga) for (affix in SupAffix.entries) {
                    val request = SubantaDerivationRequest(entry.text, affix.vibhakti, affix.vacana, linga,
                        stemFormation = entry.stemFormation, pratipadikaUpadesha = entry.upadesha)
                    try {
                        for (result in nominalEngine.deriveAll(request)) {
                            val word = sandhiEngine.padaFromDerivation(result, "lexical-candidate")
                            if (word.term.varnas.lastOrNull() != Ayogavaha.VISARGA) {
                                nominalIndex.getOrPut(normalized(word.term.surface)) { mutableListOf() } +=
                                    NominalCandidate(entry, request, result, word)
                                continue
                            }
                            val source = if (word.term.hasRutvaAt(word.term.varnas.lastIndex)) VisargaSource.ESTABLISHED_RU else {
                                val tail = result.final.terms.last { it.varnas.isNotEmpty() }
                                val change = result.final.substitutions.lastOrNull { it.targetId == tail.id }
                                if (change?.sutra == "8.3.15" &&
                                    change.sourceVarnaIndex == tail.varnas.lastIndex &&
                                    change.originalVarnas?.getOrNull(tail.varnas.lastIndex) == Vyanjana.RA &&
                                    change.replacementVarnas == listOf(Ayogavaha.VISARGA)) VisargaSource.R else null
                            }
                            if (source == null) {
                                unresolved += UnresolvedSource(request, result)
                                continue
                            }
                            val candidate = Candidate(entry, request, result, word.copy(visargaSource = source), source)
                            index.getOrPut(normalized(word.term.surface)) { mutableListOf() } += candidate
                            nominalIndex.getOrPut(normalized(word.term.surface)) { mutableListOf() } +=
                                NominalCandidate(entry, request, result, candidate.pada)
                        }
                    } catch (error: RuntimeException) {
                        failures += Failure(request, error::class.simpleName ?: "RuntimeException", error.message)
                    }
                }
            }
            return DerivedVisargaLexicon(index.mapValues { it.value.toList() },
                nominalIndex.mapValues { it.value.toList() }, failures.toList(),
                entries.filter { it.linga.isEmpty() }.distinct(), unresolved.toList())
        }
    }
}
