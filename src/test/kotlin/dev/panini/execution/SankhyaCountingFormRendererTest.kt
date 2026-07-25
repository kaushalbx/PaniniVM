package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaCountingFormRendererTest {
    @Test
    fun `renders VM counting forms without pretending to derive a sup paradigm`() {
        val renderer = SankhyaCountingFormRenderer()
        val expected = mapOf(
            0L to "शून्यम्",
            2L to "द्वे",
            3L to "त्रीणि",
            4L to "चत्वारि",
            5L to "पञ्च",
            6L to "षट्",
            10L to "दश",
            16L to "षोडश",
            20L to "विंशतिः",
            30L to "त्रिंशत्",
            42L to "द्वाचत्वारिंशत्",
            60L to "षष्टिः",
            82L to "द्व्यशीतिः",
            100L to "शतम्",
            124L to "चतुर्विंशत्यधिकशतम्",
            10_000_000L to "कोटिः",
        )

        expected.forEach { (value, surface) ->
            assertEquals(surface, renderer.render(value), "$value")
        }
    }
}
