package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Dvandva compound derivations (Pāṇini 2.2.29).
 */
class DvandvaSamasaTest {

    private val engine = SamasaEngine()

    @Test
    fun `test ramalaksmanau derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("रामलक्ष्मणौ", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }

    @Test
    fun `test matarapitarau derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("मातृ", Vibhakti.PRATHAMA),
                SamasaPada("पितृ", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("मातरापितरौ", result.final.surface)
    }

    @Test
    fun `test panipadam samahara dvandva derivation`() {
        val result = engine.derive(
            listOf(
                SamasaPada("पाणि", Vibhakti.PRATHAMA),
                SamasaPada("पाद", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("पाणिपादम्", result.final.surface)
    }

    @Test
    fun `test multi-pada dvandva ramalaksmanabharatasatrughnah`() {
        val result = engine.derive(
            listOf(
                SamasaPada("राम", Vibhakti.PRATHAMA),
                SamasaPada("लक्ष्मण", Vibhakti.PRATHAMA),
                SamasaPada("भरत", Vibhakti.PRATHAMA),
                SamasaPada("शत्रुघ्न", Vibhakti.PRATHAMA),
            ),
            SamasaType.DVANDVA,
        )
        assertEquals("रामलक्ष्मणभरतशत्रुघ्णाः", result.final.surface)
        assertTrue(result.applications.isNotEmpty())
    }
}
