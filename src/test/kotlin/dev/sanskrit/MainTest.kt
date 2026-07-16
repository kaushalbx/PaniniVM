package dev.sanskrit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MainTest {
    @Test
    fun `derive command returns the form and its sutra trace`() {
        val output = runCli(arrayOf("--derive", "राम", "SASTHI", "BAHUVACANA"))

        assertEquals("SASTHI BAHUVACANA: रामाणाम्", output.first())
        assertTrue(output.any { it.startsWith("7.1.54 ") })
        assertTrue(output.any { it.startsWith("6.4.3 ") })
    }

    @Test
    fun `sutra command reads direct base sutra fields`() {
        val output = runCli(arrayOf("--sutra", "7.1.54"))

        assertEquals("7.1.54 ह्रस्वनद्यापो नुट्", output.first())
        assertTrue(output.any { it.contains("action=AGAMA") })
    }

    @Test
    fun `derive command accepts Sanskrit vibhakti and vacana labels`() {
        val output = runCli(arrayOf("--derive", "राम", "षष्ठी", "बहुवचन"))

        assertEquals("SASTHI BAHUVACANA: रामाणाम्", output.first())
    }

    @Test
    fun `verb command returns bhavati with its sutra trace`() {
        val output = runCli(arrayOf("--verb", "भू"))

        assertEquals("भू: भवति", output.first())
        assertTrue(output.any { it.startsWith("7.3.84 —") })
    }

    @Test
    fun `verb command accepts Sanskrit number labels`() {
        val output = runCli(arrayOf("--verb", "भू", "बहुवचन"))

        assertEquals("भू: भवन्ति", output.first())
    }

    @Test
    fun `verb command derives the loṭ imperative`() {
        val output = runCli(arrayOf("--verb", "भू", "LOT", "बहुवचन"))

        assertEquals("भू: भवन्तु", output.first())
        assertTrue(output.any { it.startsWith("3.3.162 —") })
        assertTrue(output.any { it.startsWith("3.4.90 —") })
    }

    @Test
    fun `verb command derives the vidhi ling`() {
        val output = runCli(arrayOf("--verb", "भू", "LING"))

        assertEquals("भू: भवेत्", output.first())
        assertTrue(output.any { it.startsWith("3.3.161 —") })
        assertTrue(output.any { it.startsWith("3.4.103 —") })
    }
}
