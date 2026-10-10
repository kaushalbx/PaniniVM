package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara.*
import dev.panini.shiksha.Vyanjana.*

/** Current pronounced forms; lexical compound-head identities remain in PuranaNumeralClasses. */
internal object PuranaNumeralVarnas {
    private val dasha: List<Varna> = listOf(DA, A, SHA, A)
    val elevenToNineteen: Set<List<Varna>> = setOf(
        listOf(E, KA, AA) + dasha,
        listOf(DA, VA, AA) + dasha,
        listOf(TA, RA, A, YA, O) + dasha,
        listOf(CA, A, TA, U, RA) + dasha,
        listOf(PA, A, NYA, CA, A) + dasha,
        listOf(SSA, O, DDA, A, SHA, A),
        listOf(SA, A, PA, TA, A) + dasha,
        listOf(A, SSA, TTA, AA) + dasha,
        listOf(NA, A, VA, A) + dasha,
    )
    val shashtyadi: Set<List<Varna>> = setOf(
        listOf(SSA, A, SSA, TTA, I),
        listOf(SA, A, PA, TA, A, TA, I),
        listOf(A, SHA, II, TA, I),
        listOf(NA, A, VA, A, TA, I),
    )
}
