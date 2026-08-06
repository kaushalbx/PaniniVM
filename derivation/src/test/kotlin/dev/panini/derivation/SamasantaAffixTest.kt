package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Dedicated test suite for Samāsānta affixes (5.4.91, 5.4.125, etc.).
 */
class SamasantaAffixTest {

    private val engine = SamasaEngine()

    @Test
    fun `test 5 4 91 Tac affix after rajan in Karmadharaya`() {
        val result = engine.derive(
            listOf(
                SamasaPada("महत्", Vibhakti.PRATHAMA),
                SamasaPada("राजन्", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("महाराजः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "5.4.91" })
    }

    @Test
    fun `test 5 4 91 Tac affix after sakhi in Karmadharaya`() {
        val result = engine.derive(
            listOf(
                SamasaPada("परम", Vibhakti.PRATHAMA),
                SamasaPada("सखि", Vibhakti.PRATHAMA),
            ),
            SamasaType.KARMADHARAYA,
        )
        assertEquals("परमसखः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "5.4.91" })
    }

    @Test
    fun `test 5 4 151 Kap affix after urah-prabhrti in Bahuvrihi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("व्यूढ", Vibhakti.PRATHAMA),
                SamasaPada("उरस्", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("व्यूढोरस्कः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "5.4.151" })
    }

    @Test
    fun `test 5 4 154 Kap affix after negative Bahuvrihi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("अ", Vibhakti.PRATHAMA),
                SamasaPada("पुत्र", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("अपुत्रकः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "5.4.154" })
    }

    @Test
    fun `test 5 4 153 Kap affix after nadi ending Bahuvrihi`() {
        val result = engine.derive(
            listOf(
                SamasaPada("बहु", Vibhakti.PRATHAMA),
                SamasaPada("कुमारी", Vibhakti.PRATHAMA),
            ),
            SamasaType.BAHUVRIHI,
        )
        assertEquals("बहुकुमारीकः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "5.4.153" })
    }
}
