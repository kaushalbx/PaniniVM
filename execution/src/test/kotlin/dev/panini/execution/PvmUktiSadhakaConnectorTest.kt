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
        assertFalse(" दा" in rendered, rendered)
        assertEquals(1, Regex("ततः").findAll(rendered).count(), rendered)
        assertTrue("लघु" in rendered && "गुरु" in rendered, rendered)
    }

    @Test
    fun `script rendering parses a conditional split across physical lines`() {
        val rendered = sadhaka.sadhayaScript(
            """
            यदि सर्वजय + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
                तर्हि जय + अम् मुद्र् + लोट् + सिप्
                अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(
            "यदि सर्वजयः एकेन समः अस्ति तर्हि जयं मुद्रय अन्यथा पराजयं मुद्रय ।",
            rendered,
        )
        assertFalse("+" in rendered, rendered)
    }

    @Test
    fun `explicit feminine predicate suffix is preserved in readable Sanskrit`() {
        val rendered = sadhaka.sadhayaLine(
            "यदि स्वसङ्ख्याप्राप्ति + सुँ सत्य + टा सम + टाप् + सुँ असँ + लट् + तिप् " +
                "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
        )

        assertEquals(
            "यदि स्वसङ्ख्याप्राप्तिः सत्येन समा अस्ति तर्हि जयं मुद्रय अन्यथा पराजयं मुद्रय ।",
            rendered,
        )
    }

    @Test
    fun `script rendering parses a pipeline split across physical lines`() {
        val rendered = sadhaka.sadhayaScript(
            """
            क्रमाङ्क + अम् एक + अम् च युज् + णिच् + लोट् + सिप्
                ततः क्रमाङ्क + ङे दा + लोट् + सिप्
                ततः मुद्र् + लोट् + सिप् फल + अम् ।
            """.trimIndent(),
        )

        assertEquals(
            "क्रमाङ्कम् एकं च योजय ततः क्रमाङ्काय देहि ततः मुद्रय फलम् ।",
            rendered,
        )
        assertFalse("+" in rendered, rendered)
    }
}
