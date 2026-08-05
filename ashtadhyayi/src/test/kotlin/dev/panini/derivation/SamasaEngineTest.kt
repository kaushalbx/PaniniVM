package dev.panini.derivation

import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaEngineTest {

    private val engine = SamasaEngine()

    @Test
    fun `derives Avyayibhava compound upakrsnam`() {
        val result = engine.derive(listOf("उप", "कृष्ण"), SamasaType.AVYAYIBHAVA)
        assertEquals("उपकृष्णम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.1.6" })
    }

    @Test
    fun `derives Tatpurusa compound rajapurusah`() {
        val result = engine.derive(listOf("राज", "पुरुष"), SamasaType.TATPURUSA)
        assertEquals("राजपुरुषः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.8" || it.sutra == "2.1.24" })
    }

    @Test
    fun `derives Bahuvrihi compound pitambarah`() {
        val result = engine.derive(listOf("पीत", "अम्बर"), SamasaType.BAHUVRIHI)
        assertEquals("पीताम्बरः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `derives Dvandva compound ramalaksmanau`() {
        val result = engine.derive(listOf("राम", "लक्ष्मण"), SamasaType.DVANDVA)
        assertEquals("रामलक्ष्मणौ", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.29" })
    }
}
