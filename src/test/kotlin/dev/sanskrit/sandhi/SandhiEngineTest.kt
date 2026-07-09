package dev.sanskrit.sandhi

import kotlin.test.Test
import kotlin.test.assertEquals

class SandhiEngineTest {
    private val engine = SandhiEngine()

    @Test
    fun `applies savarna dirgha`() {
        val result = engine.join(listOf("देव", "आलय"))

        assertEquals("देवालय", result.output)
        assertEquals("6.1.101", result.applications.single().sutra)
    }

    @Test
    fun `applies guna after a or A`() {
        val result = engine.join(listOf("राम", "इति"))

        assertEquals("रामेति", result.output)
        assertEquals("6.1.87", result.applications.single().sutra)
        assertEquals("आद्गुणः", result.applications.single().sutraText)
        assertEquals(
            "अ या आ के बाद इ/ई, उ/ऊ, ऋ/ॠ आदि स्वर आएं तो गुणादेश होता है।",
            result.applications.single().hindiVyakhya,
        )
    }

    @Test
    fun `applies yan before svara`() {
        val result = engine.join(listOf("हरि", "अत्र"))

        assertEquals("हर्यत्र", result.output)
        assertEquals("6.1.77", result.applications.single().sutra)
    }

    @Test
    fun `applies yan from final matra before svara`() {
        val result = engine.join(listOf("गुरु", "अस्ति"))

        assertEquals("गुर्वस्ति", result.output)
        assertEquals("6.1.77", result.applications.single().sutra)
    }

    @Test
    fun `applies ec replacement before svara`() {
        val result = engine.join(listOf("गो", "अत्र"))

        assertEquals("गवत्र", result.output)
        assertEquals("6.1.78", result.applications.single().sutra)
    }

    @Test
    fun `applies visarga to s before khar`() {
        val result = engine.join(listOf("रामः", "करोति"))

        assertEquals("रामस्करोति", result.output)
        assertEquals("8.3.34", result.applications.single().sutra)
        assertEquals("विसर्जनीयस्य सः", result.applications.single().sutraText)
    }
}
