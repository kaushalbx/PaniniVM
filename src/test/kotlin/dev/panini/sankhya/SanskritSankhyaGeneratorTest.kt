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
    fun `generates trayovimshati via Sutra 6_3_48`() {
        val result = generator.generate(BigInteger.valueOf(23))
        assertEquals("त्रयोविंशति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.48" })
    }

    @Test
    fun `generates lexicalized shodasha without a fabricated Sutra trace`() {
        val result = generator.generate(BigInteger.valueOf(16))
        assertEquals("षोडश", result.final.surface)
        assertTrue(result.applications.none { it.sutra == "6.3.49" })
    }

    @Test
    fun `derives the cardinal compounds from eleven through twenty nine`() {
        val expected = listOf(
            "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश",
            "विंशति", "एकविंशति", "द्वाविंशति", "त्रयोविंशति", "चतुर्विंशति", "पञ्चविंशति",
            "षड्विंशति", "सप्तविंशति", "अष्टाविंशति", "नवविंशति",
        )

        expected.forEachIndexed { index, surface ->
            assertEquals(surface, generator.generateSurface(BigInteger.valueOf((index + 11).toLong())), "${index + 11}")
        }
    }

    @Test
    fun `trayovimshati records each grammatical operation`() {
        val sutras = generator.generate(BigInteger.valueOf(23)).applications.map { it.sutra }

        assertTrue("6.3.48" in sutras)
        assertTrue("8.2.66" in sutras)
        assertTrue("6.1.114" in sutras)
        assertTrue("6.1.87" in sutras)
    }

    @Test
    fun `applies the optional substitutions from forty onward`() {
        val fortyTwo = generator.generate(BigInteger.valueOf(42))
        val fortyEight = generator.generate(BigInteger.valueOf(48))

        assertEquals("द्वाचत्वारिंशत्", fortyTwo.final.surface)
        assertEquals("अष्टाचत्वारिंशत्", fortyEight.final.surface)
        assertTrue(fortyTwo.applications.any { it.sutra == "6.3.49" })
        assertTrue(fortyEight.applications.any { it.sutra == "6.3.49" })
    }

    @Test
    fun `returns both branches licensed by 6_3_49`() {
        val fortyTwo = SanskritSankhyaGenerator().generateVariants(BigInteger.valueOf(42))
        val fortyEight = SanskritSankhyaGenerator().generateVariants(BigInteger.valueOf(48))

        assertEquals(setOf("द्विचत्वारिंशत्", "द्वाचत्वारिंशत्"), fortyTwo.map { it.final.surface }.toSet())
        assertEquals(setOf("अष्टचत्वारिंशत्", "अष्टाचत्वारिंशत्"), fortyEight.map { it.final.surface }.toSet())
        assertTrue(fortyTwo.any { result -> result.applications.any { it.sutra == "6.3.49" } })
        assertTrue(fortyTwo.any { result -> result.applications.none { it.sutra == "6.3.49" } })

        val fortyThree = SanskritSankhyaGenerator().generateVariants(BigInteger.valueOf(43))
        assertEquals(setOf("त्रिचत्वारिंशत्", "त्रयश्चत्वारिंशत्"), fortyThree.map { it.final.surface }.toSet())
    }

    @Test
    fun `derives the ashiti series without 6_3_47 through 49`() {
        val expected = mapOf(
            81L to "एकाशीति",
            82L to "द्व्यशीति",
            83L to "त्र्यशीति",
            88L to "अष्टाशीति",
        )

        expected.forEach { (number, surface) ->
            val result = generator.generate(BigInteger.valueOf(number))
            assertEquals(surface, result.final.surface, "$number")
            assertTrue(result.applications.none { it.sutra in setOf("6.3.47", "6.3.48", "6.3.49") })
        }
    }

    @Test
    fun `uses adhika for remainders above a completed magnitude`() {
        assertEquals("शत", generator.generateSurface(BigInteger.valueOf(100)))
        assertEquals("एकाधिकशत", generator.generateSurface(BigInteger.valueOf(101)))
        assertEquals("चतुर्विंशत्यधिकशत", generator.generateSurface(BigInteger.valueOf(124)))
        assertEquals("द्विशत", generator.generateSurface(BigInteger.valueOf(200)))
        assertEquals("त्रिशत", generator.generateSurface(BigInteger.valueOf(300)))
        assertEquals("अष्टशत", generator.generateSurface(BigInteger.valueOf(800)))
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
    fun generateDeclined(v: BigInteger, l: Linga = Linga.PUMS, vi: Vibhakti = Vibhakti.PRATHAMA, va: Vacana = Vacana.EKAVACANA) = g.generateDeclined(v, l, vi, va)
    fun generateAdhikaSurface(v: BigInteger) = g.generateAdhikaSurface(v)
}

class SanskritSanskritParserWrapper {
    private val parser = SanskritSankhyaParser()
    fun parse(surface: String) = parser.parse(surface)
}
