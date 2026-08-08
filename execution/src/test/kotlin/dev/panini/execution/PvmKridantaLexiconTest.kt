package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmKridantaLexiconTest {

    @Test
    fun `resolves compatibility stems by dhatu and pratyaya`() {
        assertEquals("योग", PvmKridantaLexicon.stem("युज्", "घञ्"))
        assertEquals("योजन", PvmKridantaLexicon.stem("युज्", "ल्युट्"))
        assertEquals("धारण", PvmKridantaLexicon.stem("धृ", "अन"))
        assertEquals("हार", PvmKridantaLexicon.stem("हृ", "घञ्"))
        assertTrue(setOf("योग", "योजन", "धारण", "हार").all(PvmKridantaLexicon.declinableStems::contains))
    }

    @Test
    fun `preserves established fallback stems`() {
        assertEquals("हर", PvmKridantaLexicon.stem("हृ", "क्त"))
        assertEquals("पठ्", PvmKridantaLexicon.stem("पठ्", "क्त"))
    }
}
