package dev.panini.sankhya

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Linga
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanskritSankhyaGeneratorTest {

    private val generator = SanskritSanskritGeneratorWrapper()
    private val parser = SanskritSanskritParserWrapper()

    @Test
    fun `generates primitives correctly`() {
        assertEquals("एक", generator.generateSurface(BigInteger.ONE))
        assertEquals("द्वि", generator.generateSurface(BigInteger.valueOf(2)))
        assertEquals("दशन्", generator.generateSurface(BigInteger.TEN))
        assertEquals("विंशति", generator.generateSurface(BigInteger.valueOf(20)))
    }

    @Test
    fun `generates dvavimshati via Sutra 6_3_47`() {
        val result = generator.generate(BigInteger.valueOf(22))
        assertEquals("द्वाविंशति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.47" })
    }

    @Test
    fun `generates trayovimshati via Sutra 6_3_46`() {
        val result = generator.generate(BigInteger.valueOf(23))
        assertEquals("त्रयोविंशति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.46" })
    }

    @Test
    fun `generates shodasha via Sutra 6_3_49`() {
        val result = generator.generate(BigInteger.valueOf(16))
        assertEquals("षोडश", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.49" })
    }

    @Test
    fun `generates ordinals using Purana rules`() {
        assertEquals("प्रथम", generator.generateOrdinalSurface(BigInteger.ONE))
        assertEquals("द्वितीय", generator.generateOrdinalSurface(BigInteger.TWO))
        assertEquals("तृतीय", generator.generateOrdinalSurface(BigInteger.valueOf(3)))
        assertEquals("चतुर्थ", generator.generateOrdinalSurface(BigInteger.valueOf(4)))
        assertEquals("पञ्चम", generator.generateOrdinalSurface(BigInteger.valueOf(5)))
        assertEquals("दशम", generator.generateOrdinalSurface(BigInteger.TEN))
    }

    @Test
    fun `generates gender declensions for numbers`() {
        assertEquals("एकः", generator.generateDeclined(BigInteger.ONE, Linga.PUMS, Vibhakti.PRATHAMA))
        assertEquals("एका", generator.generateDeclined(BigInteger.ONE, Linga.STRI, Vibhakti.PRATHAMA))
        assertEquals("एकम्", generator.generateDeclined(BigInteger.ONE, Linga.NAPUMSAKA, Vibhakti.PRATHAMA))

        assertEquals("त्रयः", generator.generateDeclined(BigInteger.valueOf(3), Linga.PUMS))
        assertEquals("तिस्रः", generator.generateDeclined(BigInteger.valueOf(3), Linga.STRI))
        assertEquals("त्रीणि", generator.generateDeclined(BigInteger.valueOf(3), Linga.NAPUMSAKA))
    }

    @Test
    fun `generates adhika compound phrasing`() {
        val adhika = generator.generateAdhikaSurface(BigInteger.valueOf(124))
        assertTrue(adhika.contains("धिकशत"))
    }
}

private class SanskritSanskritGeneratorWrapper {
    private val g = SanskritSankhyaGenerator()
    fun generate(v: BigInteger) = g.generate(v)
    fun generateSurface(v: BigInteger) = g.generateSurface(v)
    fun generateOrdinalSurface(v: BigInteger) = g.generateOrdinalSurface(v)
    fun generateDeclined(v: BigInteger, l: Linga = Linga.PUMS, vi: Vibhakti = Vibhakti.PRATHAMA, va: Vacana = Vacana.EKAVACANA) = g.generateDeclined(v, l, vi, va)
    fun generateAdhikaSurface(v: BigInteger) = g.generateAdhikaSurface(v)
}

class SanskritSanskritParserWrapper {
    private val parser = SanskritSankhyaParser()
    fun parse(surface: String) = parser.parse(surface)
}
