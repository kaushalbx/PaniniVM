package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Bahuvrīhi compound derivations (Pāṇini 2.2.23 - 2.2.28).
 */
class BahuvrihiSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test pitambarah derivation via 2 2 24`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पीत", Vibhakti.PRATHAMA),
                SamasaPada("अम्बर", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("पीताम्बरः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
        val parasavarna = result.applications.filter { it.sutra == "8.4.58" }
        // Constituent inflection and completed-compound derivation are separate
        // Tripādī runs. Each real anusvāra occurrence must still be transformed.
        assertTrue(parasavarna.isNotEmpty())
        assertTrue(parasavarna.all { it.delta.changedTerms.isNotEmpty() })
        val positions = result.applications.indices.filter { result.applications[it].sutra == "8.4.58" }
        for ((first, second) in positions.zipWithNext()) {
            assertTrue(result.applications.subList(first + 1, second).any { it.sutra == "8.3.24" })
        }
    }

    @Test
    fun `test dasananah derivation via 2 2 24`() {
        val result = engine.derive(
            listOf(
                SamasaPada("दश", Vibhakti.PRATHAMA),
                SamasaPada("आनन", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("दशाननः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `test praptodakah derivation via 2 2 24`() {
        val result = engine.derive(
            listOf(
                SamasaPada("प्राप्त", Vibhakti.PRATHAMA),
                SamasaPada("उदक", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("प्राप्तोदकः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.24" })
    }

    @Test
    fun `test saputrah derivation via 2 2 28`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सह", Vibhakti.TRTIYA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("सपुत्रः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.28" })
    }

    @Test
    fun `test saparivarah derivation via 2 2 28`() {
        val result = engine.derive(
            listOf(
                SamasaPada("सह", Vibhakti.TRTIYA),
                SamasaPada("परिवार", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("सपरिवारः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "2.2.28" })
    }
}
