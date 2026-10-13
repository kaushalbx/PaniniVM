package dev.panini.vyakaranam.ast

import kotlin.test.*

class CoordinationBoundaryTest {
    private fun noun(name: String) = SubantaPada(name, MulaPratipadika(name, name), SupPratyaya("अम्", "अम्"))

    @Test fun `boundaries preserve occurrences and must cover every member`() {
        val first = noun("मान")
        val repeated = first.copy()
        val last = noun("फल")
        val group = SamuccitaSubanta("", listOf(first, repeated, last), listOf(2, 1))
        assertSame(first, group.conjuncts()[0][0])
        assertSame(repeated, group.conjuncts()[0][1])
        assertSame(last, group.conjuncts()[1][0])
        for (invalid in listOf(listOf(1, 1), listOf(0, 3), listOf(-1, 4), listOf(Int.MAX_VALUE, Int.MAX_VALUE, 5))) {
            assertFailsWith<IllegalArgumentException> { group.copy(conjunctSizes = invalid) }
        }
        assertFailsWith<IllegalArgumentException> { group.copy(members = listOf(first)) }
    }
}
