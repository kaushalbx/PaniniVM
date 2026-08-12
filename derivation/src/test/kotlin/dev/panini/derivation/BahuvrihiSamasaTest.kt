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
