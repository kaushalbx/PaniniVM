package dev.panini.plugin

import java.awt.Font
import kotlin.test.Test
import kotlin.test.assertEquals

class PvmUiFontsTest {
    @Test
    fun `selected inlay font renders complete devanagari clusters`() {
        val samples = listOf(
            "कर्तृ = कर्मन् = करण = क्रिया =",
            "प्रक्रिया-मान-सन्दर्भः",
            "दक्षिणे द्वि वामे पञ्च",
        )
        samples.forEach { sample ->
            val font = PvmUiFonts.forText(Font(Font.MONOSPACED, Font.PLAIN, 13), sample, 11f)
            assertEquals(-1, font.canDisplayUpTo(sample), "${font.family} cannot display '$sample'")
        }
    }
}
