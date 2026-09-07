package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.core.SupAffix

data class AccentedVowel(
    val vowel: Char,
    val accent: AccentType,
    val positionIndex: Int,
)

data class SvaraResult(
    val word: String,
    val udattaVowelIndex: Int,
    val vowels: List<AccentedVowel>,
    val formattedDevanagari: String,
    val rulesApplied: List<String>,
)

enum class SvaraTriggerKind { NIT_OR_NGIT, PIT_OR_SUP, EXPLICIT_UDATTA }

/** Grammatical evidence used by svara rules; never inferred from the final spelling. */
data class SvaraTrigger(
    val kind: SvaraTriggerKind,
    val termId: String,
    val marker: ItMarker? = null,
    val designationSutra: String? = null,
    val vowelIndex: Int? = null,
)

data class SvaraContext(val triggers: List<SvaraTrigger> = emptyList()) {
    companion object {
        fun from(state: DerivationState): SvaraContext = SvaraContext(buildList {
            state.allEffectiveTerms.forEach { term ->
                term.itMarkerProvenance
                    .filter { it.marker == ItMarker.NIT || it.marker == ItMarker.NGIT }
                    .forEach { provenance ->
                        add(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, term.id, provenance.marker, provenance.designationSutra))
                    }
                term.itMarkerProvenance
                    .filter { it.marker == ItMarker.P }
                    .forEach { provenance ->
                        add(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, term.id, provenance.marker, provenance.designationSutra))
                    }
                if (SupAffix.entries.any { affix -> term.matchesUpadesha(affix.upadesha) }) {
                    add(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, term.id))
                }
            }
        })
    }
}

object SvaraEngine {

    private val VOWELS = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ')
    private val MATRAS = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'े', 'ै', 'ो', 'ौ')

    /** Computes the Pāṇinian Svara (Udātta, Anudātta, Svarita) assignment for a word. */
    fun computeSvara(
        word: String,
        context: SvaraContext = SvaraContext(),
    ): SvaraResult {
        val vowelPositions = findVowelPositions(word)

        if (vowelPositions.isEmpty()) {
            return SvaraResult(word, -1, emptyList(), word, emptyList())
        }

        val explicit = context.triggers.firstOrNull { it.kind == SvaraTriggerKind.EXPLICIT_UDATTA }
        var state = DerivationState(
            terms = listOf(DerivationTerm("svara-pada", word, TermKind.PRATIPADIKA)),
            stage = DerivationStage.FINAL,
            svaraNimittas = context.triggers.map { trigger ->
                SvaraNimitta(SvaraNimittaKind.valueOf(trigger.kind.name), trigger.termId, trigger.vowelIndex)
            },
            svaraAssignments = explicit?.let { trigger ->
                val index = requireNotNull(trigger.vowelIndex) { "An explicit udātta trigger requires a vowel index." }
                listOf(SvaraAssignment(index.coerceIn(0, vowelPositions.lastIndex), AccentType.UDATTA, SvaraAssignmentSource.Lexical(trigger.termId)))
            }.orEmpty(),
        )
        val rules = mutableListOf<String>()
        val svaraSutras = dev.panini.ashtadhyayi.Ashtadhyayi.executableSutrasAt(dev.panini.sutra.SutraStage.SVARA)
            .sortedByDescending { it.krama }
        do {
            val applicable = svaraSutras.firstOrNull { it.matches(state) }
            if (applicable == null) break
            val change = applicable.apply(state)
            require(change.state != state) { "Svara sūtra ${applicable.sutra} matched without assigning an accent." }
            state = change.state.recordAppliedSutra(applicable.sutra)
            val text = (applicable as? dev.panini.sutra.Sutra<*, *>)?.text ?: applicable.sutra
            rules += "${applicable.sutra} [$text]: ${change.explanation}"
        } while (true)

        val assignments = state.svaraAssignments.associateBy { it.vowelIndex }
        val udattaVowelIndex = state.svaraAssignments.single { it.accent == AccentType.UDATTA }.vowelIndex
        val accentedVowels = vowelPositions.mapIndexed { index, position ->
            AccentedVowel(word[position], assignments.getValue(index).accent, position)
        }

        val formatted = formatDevanagariAccents(word, vowelPositions, udattaVowelIndex)

        return SvaraResult(
            word = word,
            udattaVowelIndex = udattaVowelIndex,
            vowels = accentedVowels,
            formattedDevanagari = formatted,
            rulesApplied = rules,
        )
    }

    private fun findVowelPositions(word: String): List<Int> {
        val positions = mutableListOf<Int>()
        for (i in word.indices) {
            val ch = word[i]
            if (ch in VOWELS || ch in MATRAS) {
                positions.add(i)
            }
        }
        return positions
    }

    private fun formatDevanagariAccents(word: String, vowelPositions: List<Int>, udattaIdx: Int): String {
        val sb = StringBuilder()
        var vCount = 0
        for (i in word.indices) {
            val ch = word[i]
            sb.append(ch)
            if (ch in VOWELS || ch in MATRAS) {
                if (vCount != udattaIdx) {
                    // Anudātta underbar \u0952
                    sb.append("\u0952")
                }
                vCount++
            }
        }
        return sb.toString()
    }
}
