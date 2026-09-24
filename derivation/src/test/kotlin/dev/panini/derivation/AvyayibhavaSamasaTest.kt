package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Avyayībhāva compound derivations (Pāṇini 2.1.5 - 2.1.21).
 */
class AvyayibhavaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test upakrsnam derivation via 2 1 6`() {
        val result = engine.derive(
            listOf(
                SamasaPada("उप", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("कृष्ण", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("उपकृष्णम्", result.final.surface)
        assertTrue(result.applications.map { it.sutra }.containsAll(listOf("2.1.6", "1.1.41", "2.4.18", "2.4.83", "7.1.24")))
    }

    @Test
    fun `test anugangam derivation via 2 1 6`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अनु", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("गङ्गा", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("अनुगङ्गम्", result.final.surface, result.applications.joinToString("\n") { "${it.sutra}: ${it.before.surface} -> ${it.after.surface}" })
        assertTrue(result.applications.map { it.sutra }.containsAll(listOf("1.2.47", "2.4.83")))
    }

    @Test
    fun `test yathasakti derivation via 2 1 6`() {
        val result = engine.derive(
            listOf(
                SamasaPada("यथा", Vibhakti.PRATHAMA, samjnas = setOf(Samjna.AVYAYA)),
                SamasaPada("शक्ति", Vibhakti.PRATHAMA),
            ),
            SamasaType.AVYAYIBHAVA,
        )
        assertEquals("यथाशक्ति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.4.82" })
    }
}
