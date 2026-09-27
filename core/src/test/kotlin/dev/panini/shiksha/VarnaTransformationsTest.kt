package dev.panini.shiksha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VarnaTransformationsTest {
    @Test
    fun `replaces only an exact phonological ending`() {
        val source = listOf(Vyanjana.RA, Svara.A, Vyanjana.MA, Svara.A, Vyanjana.NA)

        assertEquals(
            listOf(Vyanjana.RA, Svara.A, Vyanjana.MA, Svara.AA),
            source.replaceExactEnding(listOf(Svara.A, Vyanjana.NA), listOf(Svara.AA)),
        )
    }

    @Test
    fun `rejects a broad or absent ending`() {
        val source = listOf(Vyanjana.RA, Svara.A)

        assertFailsWith<IllegalArgumentException> { source.replaceExactEnding(emptyList(), emptyList()) }
        assertFailsWith<IllegalArgumentException> {
            source.replaceExactEnding(listOf(Svara.I), listOf(Svara.II))
        }
    }
}
