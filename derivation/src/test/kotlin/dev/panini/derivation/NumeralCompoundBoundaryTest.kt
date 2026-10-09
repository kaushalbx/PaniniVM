package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class NumeralCompoundBoundaryTest {
    @Test
    fun `consonant boundary keeps jash and khar derivation trace`() {
        val result = SandhiEngine().joinConsonantBoundary("षष्", "पर्यन्त")
        assertEquals("षट्पर्यन्त", result.final.surface)
        assertEquals(listOf("8.2.39", "8.4.55"), result.applications.map { it.sutra })
        assertEquals(result.applications.map { it.sutra }, result.final.appliedSutras)
    }

    @Test
    fun `voiced follower does not trigger khar devoicing`() {
        val result = SandhiEngine().joinConsonantBoundary("षष्", "गुण")
        assertEquals("षड्गुण", result.final.surface)
        assertEquals(listOf("8.2.39"), result.applications.map { it.sutra })
    }
}
