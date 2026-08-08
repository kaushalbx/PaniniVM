package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmBlockBoundaryTest {

    @Test
    fun `requires double danda at the end of the line`() {
        assertTrue(PvmBlockBoundary.closes("इति ॥"))
        assertTrue(PvmBlockBoundary.closes("कृ + लोट् + सिप् ॥  "))
        assertFalse(PvmBlockBoundary.closes("इति ॥ कृ + लोट् + सिप्"))
    }

    @Test
    fun `distinguishes marker-only boundaries from terminal body sentences`() {
        assertFalse(PvmBlockBoundary.carriesBody("॥"))
        assertFalse(PvmBlockBoundary.carriesBody("इति ॥"))
        assertTrue(PvmBlockBoundary.carriesBody("कृ + लोट् + सिप् ॥"))
    }

    @Test
    fun `standalone double danda closes a definition without iti body`() {
        val definition = assertIs<PvmScriptStatement.SamjnaDefinition>(
            PvmScript.parse(
                """
                गणित + सुँ ।
                एक + अम् कृ + लोट् + सिप् ।
                ॥
                """.trimIndent(),
            ).single(),
        )

        assertEquals(1, definition.body.size)
        assertEquals("एक + अम् कृ + लोट् + सिप् ।", definition.body.single().text)
    }
}
