package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.core.SupAffix
import dev.panini.shiksha.Accent
import dev.panini.sutra.SutraStage

data class AccentedVowel(val vowel: Char, val accent: AccentType, val positionIndex: Int)

data class SvaraResult(
    val word: String,
    val udattaVowelIndex: Int,
    val vowels: List<AccentedVowel>,
    val formattedDevanagari: String,
    val rulesApplied: List<String>,
)

enum class SvaraTriggerKind { PRATYAYA, NIT_OR_NGIT, PIT_OR_SUP, EXPLICIT_UDATTA }

data class SvaraTrigger(
    val kind: SvaraTriggerKind,
    val termId: String,
    val marker: ItMarker? = null,
    val designationSutra: String? = null,
    val vowelIndex: Int? = null,
    val lexicalSource: String? = null,
)

data class SvaraContext(val triggers: List<SvaraTrigger> = emptyList()) {
    companion object {
        fun from(state: DerivationState): SvaraContext = SvaraContext(buildList {
            state.terms.forEach { term ->
                val prefixVowels = DevanagariVowelLoci.positions(state.surfaceBeforeTerm(term.id)).size
                if (term.kind == TermKind.PRATYAYA && DevanagariVowelLoci.positions(term.surface).isNotEmpty()) {
                    add(SvaraTrigger(SvaraTriggerKind.PRATYAYA, term.id, vowelIndex = prefixVowels))
                }
                term.itMarkerProvenance.filter { it.marker == ItMarker.NIT || it.marker == ItMarker.NGIT }.forEach {
                    add(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, term.id, it.marker, it.designationSutra, prefixVowels))
                }
                term.itMarkerProvenance.filter { it.marker == ItMarker.P }.forEach {
                    add(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, term.id, it.marker, it.designationSutra, prefixVowels))
                }
                if (SupAffix.entries.any { affix -> term.matchesUpadesha(affix.upadesha) }) {
                    add(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, term.id, vowelIndex = prefixVowels))
                }
                if (term.lexicalAccent == Accent.UDATTA) {
                    add(SvaraTrigger(SvaraTriggerKind.EXPLICIT_UDATTA, term.id, vowelIndex = prefixVowels, lexicalSource = term.lexicalAccentSource))
                }
            }
        })
    }
}

data class SvaraDerivation(
    val state: DerivationState,
    val applications: List<DerivationApplication>,
    val events: List<DerivationEvent>,
    val result: SvaraResult?,
)

object SvaraEngine {
    fun derive(state: DerivationState, context: SvaraContext = SvaraContext.from(state)): SvaraDerivation {
        val positions = DevanagariVowelLoci.positions(state.surface)
        if (positions.isEmpty()) return SvaraDerivation(state, emptyList(), emptyList(), null)
        val explicit = context.triggers.firstOrNull { it.kind == SvaraTriggerKind.EXPLICIT_UDATTA }
        val prepared = state.copy(
            svaraNimittas = context.triggers.map { SvaraNimitta(SvaraNimittaKind.valueOf(it.kind.name), it.termId, it.vowelIndex) },
            svaraAssignments = explicit?.let {
                val index = requireNotNull(it.vowelIndex) { "An explicit udātta trigger requires a vowel index." }
                listOf(SvaraAssignment(index, AccentType.UDATTA, SvaraAssignmentSource.Lexical(it.lexicalSource ?: it.termId)))
            }.orEmpty(),
        )
        val derivation = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutrasAt(SutraStage.SVARA))
            .derive(prepared, DerivationConfig(validateFinalItProcessing = false, computeSvara = false))
        if (derivation.final.svaraAssignments.none { it.accent == AccentType.UDATTA }) {
            return SvaraDerivation(derivation.final, derivation.applications, derivation.events.filterNot { it is DerivationEvent.Completed }, null)
        }
        val assignments = derivation.final.svaraAssignments.associateBy { it.vowelIndex }
        val udatta = derivation.final.svaraAssignments.single { it.accent == AccentType.UDATTA }.vowelIndex
        val vowels = positions.mapIndexed { index, position -> AccentedVowel(state.surface[position], assignments.getValue(index).accent, position) }
        val result = SvaraResult(
            state.surface, udatta, vowels, format(state.surface, positions, udatta),
            derivation.applications.map { it.trace + ": " + it.explanation },
        )
        return SvaraDerivation(derivation.final, derivation.applications, derivation.events.filterNot { it is DerivationEvent.Completed }, result)
    }

    fun computeSvara(word: String, context: SvaraContext = SvaraContext()): SvaraResult {
        val state = DerivationState(listOf(DerivationTerm("svara-pada", word, TermKind.PRATIPADIKA)), stage = DerivationStage.FINAL)
        return requireNotNull(derive(state, context).result) { "No grammatical or lexical rule established an udātta for $word." }
    }

    private fun format(word: String, positions: List<Int>, udatta: Int): String = buildString {
        var vowel = 0
        word.indices.forEach { index ->
            append(word[index])
            if (index in positions) {
                if (vowel != udatta) append('\u0952')
                vowel++
            }
        }
    }
}
