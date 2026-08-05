package dev.panini.derivation

import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaEngineTest {
    private val engine = SamasaEngine()

    @Test
    fun `test Avyayibhava compound derivation`() {
        val result = engine.derive(listOf("उप", "कृष्ण"), SamasaType.AVYAYIBHAVA)

        assertEquals("उपकृष्णम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.1.6" })
    }

    @Test
    fun `test Shashthi Tatpurusha compound derivation`() {
        val result = engine.derive(listOf("राज", "पुरुष"), SamasaType.TATPURUSA)

        assertEquals("राजपुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.2.8" })
    }

    @Test
    fun `test Dvitiya Tatpurusha compound derivation`() {
        val result = engine.derive(listOf("कृष्ण", "श्रित"), SamasaType.TATPURUSA)

        assertEquals("कृष्णश्रितः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.24" })
    }

    @Test
    fun `test Bahuvrihi compound derivation with Sandhi`() {
        val result = engine.derive(listOf("पीत", "अम्बर"), SamasaType.BAHUVRIHI)

        assertEquals("पीताम्बरः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `test Dvandva compound derivation`() {
        val result = engine.derive(listOf("राम", "लक्ष्मण"), SamasaType.DVANDVA)

        assertEquals("रामलक्ष्मणौ", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.29" })
    }
}
