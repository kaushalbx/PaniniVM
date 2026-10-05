package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class PrefixBoundarySandhiTest {
    @Test
    fun `homogeneous prefix vowels coalesce without reopening verbal phonology`() {
        val engine = SandhiEngine()
        assertEquals("नीहि", engine.joinPrefix("नि", "इहि"))
        assertEquals("सूपविश", engine.joinPrefix("सु", "उपविश"))
        assertEquals("उपागच्छ", engine.joinPrefix("उप", "आगच्छ"))
        assertEquals("प्रक्षिप", engine.joinPrefix("प्र", "क्षिप"))
    }
}
