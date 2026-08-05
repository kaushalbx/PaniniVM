package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SamasaEngineTest {
    private val engine = SamasaEngine()

    @Test
    fun `test Avyayibhava compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("उपकृष्णम्", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.1.6" })
    }

    @Test
    fun `test Shashthi Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राज", Vibhakti.SASTHI),
                SamasaPada("पुरुष", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("राजपुरुषः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "1.2.46" })
        assertTrue(result.applications.any { it.sutra == "2.4.71" })
        assertTrue(result.applications.any { it.sutra == "2.2.8" })
    }

    @Test
    fun `test Dvitiya Tatpurusha compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("कृष्ण", Vibhakti.DVITIYA),
                SamasaPada("श्रित", Vibhakti.PRATHAMA),
            ),
            SamasaType.TATPURUSA,
        )
        assertEquals("कृष्णश्रितः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.1.24" })
    }

    @Test
    fun `test Bahuvrihi compound derivation with Sandhi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("पीताम्बरः", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `test Dvandva compound derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("रामलक्ष्मणौ", result.final.terms.last().surface)
        assertTrue(result.applications.any { it.sutra == "2.2.29" })
    }
}
