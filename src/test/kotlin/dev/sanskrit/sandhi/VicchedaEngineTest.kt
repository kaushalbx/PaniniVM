package dev.sanskrit.sandhi

import kotlin.test.Test
import kotlin.test.assertTrue

class VicchedaEngineTest {
    private val engine = VicchedaEngine()

    @Test
    fun `splits guna sandhi`() {
        val result = engine.split("रामेति")

        assertTrue(result.applications.any { it.sutra == "6.1.87" && it.words == listOf("राम", "इति") })
    }

    @Test
    fun `splits yan sandhi from i`() {
        val result = engine.split("हर्यत्र")

        assertTrue(result.applications.any { it.sutra == "6.1.77" && it.words == listOf("हरि", "अत्र") })
    }

    @Test
    fun `splits yan sandhi from u matra`() {
        val result = engine.split("गुर्वस्ति")

        assertTrue(result.applications.any { it.sutra == "6.1.77" && it.words == listOf("गुरु", "अस्ति") })
    }

    @Test
    fun `splits savarna dirgha`() {
        val result = engine.split("देवालय")

        assertTrue(result.applications.any { it.sutra == "6.1.101" && it.words == listOf("देव", "आलय") })
    }

    @Test
    fun `splits visarga before khar`() {
        val result = engine.split("रामस्करोति")

        assertTrue(result.applications.any { it.sutra == "8.3.34" && it.words == listOf("रामः", "करोति") })
    }
}
