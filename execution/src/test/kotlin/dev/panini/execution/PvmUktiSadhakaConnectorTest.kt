package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PvmUktiSadhakaConnectorTest {
    private val sadhaka = PvmUktiSadhaka()

    @Test
    fun `surface rendering preserves sequence connectors instead of inserting dandas`() {
        val rendered = sadhaka.sadhayaLine(
            "राम + सुँ भू + लट् + तिप् च फल + अम् खाद् + लट् + तिप् ।",
        )

        assertTrue(" च " in rendered, rendered)
        assertFalse(" । " in rendered.removeSuffix(" ।"), rendered)
        assertEquals(1, rendered.count { it == '।' }, rendered)
    }

    @Test
    fun `conditional result pipeline does not show a danda before tatah`() {
        val rendered = sadhaka.sadhayaLine(
            "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                "तर्हि लघु अन्यथा गुरु ततः मुद्र् + लोट् + सिप् ।",
        )

        assertTrue(" ततः " in rendered, rendered)
        assertFalse("। ततः" in rendered, rendered)
    }
}
