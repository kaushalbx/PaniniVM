package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.core.SupAffix

enum class AccentType {
    UDATTA,   // उदात्त (High pitch)
    ANUDATTA, // अनुदात्त (Low pitch)
    SVARITA,  // स्वरित (Circumflex)
}

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
        val rules = mutableListOf<String>()
        val vowelPositions = findVowelPositions(word)

        if (vowelPositions.isEmpty()) {
            return SvaraResult(word, -1, emptyList(), word, listOf("No vowels found in word"))
        }

        // Determine Udātta vowel index (0-indexed position among vowels in word)
        val explicit = context.triggers.firstOrNull { it.kind == SvaraTriggerKind.EXPLICIT_UDATTA }
        val nitOrNgit = context.triggers.firstOrNull { it.kind == SvaraTriggerKind.NIT_OR_NGIT }
        val pitOrSup = context.triggers.firstOrNull { it.kind == SvaraTriggerKind.PIT_OR_SUP }
        val udattaVowelIndex = when {
            explicit != null -> {
                val vowelIndex = requireNotNull(explicit.vowelIndex) { "An explicit udātta trigger requires a vowel index." }
                rules += "Explicit Udātta specified at vowel index $vowelIndex"
                vowelIndex.coerceIn(0, vowelPositions.size - 1)
            }
            nitOrNgit != null -> {
                rules += "6.1.197 [ञ्नित्यादिर्नित्यम्]: Ñ-it / N-it affix gives initial accent (आयुदात्त)"
                0 // First vowel is Udātta
            }
            pitOrSup != null -> {
                rules += "3.1.4 [अनुदात्तौ सुप्पितौ]: Sup/Pit affix is Anudātta, stem retains accent (अन्तोदात्त)"
                (vowelPositions.size - 2).coerceAtLeast(0)
            }
            else -> {
                rules += "3.1.3 [आयुदात्तश्च]: Default affix accent on final/suffix vowel (अन्तोदात्त)"
                vowelPositions.size - 1
            }
        }

        rules += "6.1.158 [अनुदात्तं पदमेकवर्जम्]: Word has 1 Udātta; all other ${vowelPositions.size - 1} vowels become Anudātta"

        val accentedVowels = vowelPositions.mapIndexed { idx, pos ->
            val accent = if (idx == udattaVowelIndex) AccentType.UDATTA else AccentType.ANUDATTA
            AccentedVowel(word[pos], accent, pos)
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
