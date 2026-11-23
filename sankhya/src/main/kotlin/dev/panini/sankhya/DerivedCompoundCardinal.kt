package dev.panini.sankhya

import dev.panini.derivation.TermKind

/**
 * One-word compound cardinals (एकादशन्, त्रयोदशन्, पञ्चविंशति), recognized so that they can be rewritten.
 *
 * PVM writes compound numbers segmented (`एक + दशन्`, `पञ्चन् + विंशति`); the numeral engine then derives
 * the compound itself. A one-word stem is identified only when [SankhyaGenerator.cardinal] derives exactly that
 * prātipadika for some n, with its final member in the form a primitive numeral is written in
 * (दश → दशन्, षोडश → षोडशन्: `PrimitiveSankhya.inflectionPratipadika`). Nothing is matched by spelling alone.
 */
object DerivedCompoundCardinal {
    /** Below eleven every cardinal is primitive; from one hundred, PVM writes अधिक constructions. */
    val range: LongRange = 11L..99L

    private val byStem: Map<String, Long> by lazy(LazyThreadSafetyMode.PUBLICATION) {
        val generator = SankhyaGenerator()
        buildMap {
            for (value in range) {
                val stem = canonicalStem(generator, value) ?: continue
                if (PrimitiveSankhya.fromAnnotatedPratipadika(stem) != null) continue
                check(putIfAbsent(stem, value) == null) { "Two cardinals derive the same stem $stem." }
            }
        }
    }

    /** The value a derived one-word compound stem denotes, or null for any other spelling. */
    fun valueOf(stem: String): Long? = byStem[stem]

    /**
     * The segmented spelling of a derived one-word compound: units before their ten, as the compound itself is
     * formed (एकादशन् → `एक + दशन्`, षोडशन् → `षष् + दशन्`, पञ्चविंशति → `पञ्चन् + विंशति`).
     */
    fun segmented(stem: String): String? {
        val value = valueOf(stem) ?: return null
        val unit = value % 10
        val ten = if (value < 20) 10L else value - unit
        if (unit == 0L) return null
        val unitStem = PrimitiveSankhya.fromValue(unit)?.pratipadika ?: return null
        val tenStem = PrimitiveSankhya.fromValue(ten)?.pratipadika ?: return null
        return "$unitStem + $tenStem"
    }

    /** The derived compound with its final member restored to the primitive's written prātipadika. */
    private fun canonicalStem(generator: SankhyaGenerator, value: Long): String? {
        val members = generator.cardinal(value).final.allEffectiveTerms
            .filter { it.kind == TermKind.PRATIPADIKA && it.surface.isNotEmpty() }
            .map { it.surface }
        if (members.isEmpty()) return null
        val last = members.last()
        val head = PrimitiveSankhya.entries.firstOrNull { it.uttarapada == last || it.pratipadika == last }
            ?: return null
        return members.dropLast(1).joinToString("") + head.inflectionPratipadika
    }
}
