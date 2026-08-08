package dev.panini.execution

import dev.panini.core.Linga
import kotlin.test.Test
import kotlin.test.assertEquals

class PvmNominalLexiconTest {

    @Test
    fun `provides nominal gender compatibility metadata`() {
        listOf("हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्").forEach {
            assertEquals(Linga.NAPUMSAKA, PvmNominalLexicon.gender(it))
        }
        assertEquals(Linga.PUMS, PvmNominalLexicon.gender("राम"))
    }

    @Test
    fun `preserves configured surfaces after derivation`() {
        assertEquals("क्षीप्", PvmNominalLexicon.surface("क्षीप्", "क्षीः"))
        assertEquals("क्षिप्", PvmNominalLexicon.surface("क्षिप्", "क्षिः"))
        assertEquals("रामः", PvmNominalLexicon.surface("राम", "रामः"))
    }
}
