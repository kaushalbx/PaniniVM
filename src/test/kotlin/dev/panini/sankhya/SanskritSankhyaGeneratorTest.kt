package dev.panini.sankhya

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.core.Linga
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SanskritSankhyaGeneratorTest {

    private val generator = SanskritSanskritGeneratorWrapper()

    @Test
    fun `generates primitives correctly`() {
        assertEquals("एक", generator.generateSurface(BigInteger.ONE))
        assertEquals("द्वि", generator.generateSurface(BigInteger.valueOf(2)))
        assertEquals("दशन्", generator.generateSurface(BigInteger.TEN))
        assertEquals("विंशति", generator.generateSurface(BigInteger.valueOf(20)))
        assertEquals("पञ्चन्", generator.generateSurface(BigInteger.valueOf(5)))
        assertEquals("षष्", generator.generateSurface(BigInteger.valueOf(6)))
        assertEquals("सप्तन्", generator.generateSurface(BigInteger.valueOf(7)))
        assertEquals("अष्टन्", generator.generateSurface(BigInteger.valueOf(8)))
        assertEquals("नवन्", generator.generateSurface(BigInteger.valueOf(9)))
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
    fun `derives purana numerals one through ten`() {
        val expected = listOf("प्रथम", "द्वितीय", "तृतीय", "चतुर्थ", "पञ्चम", "षष्ठ", "सप्तम", "अष्टम", "नवम", "दशम")
        expected.forEachIndexed { index, surface ->
            assertEquals(surface, SanskritSankhyaGenerator().generateOrdinalSurface(BigInteger.valueOf((index + 1).toLong())))
        }
    }

    @Test
    fun `purana results retain their actual sutra provenance`() {
        val sut = SanskritSankhyaGenerator()

        assertTrue(sut.generateOrdinal(BigInteger.ONE).applications.isEmpty())
        assertEquals(listOf("5.2.54"), sut.generateOrdinal(BigInteger.TWO).applications.map { it.sutra })
        assertEquals(listOf("5.2.55"), sut.generateOrdinal(BigInteger.valueOf(3)).applications.map { it.sutra })
        assertEquals(listOf("5.2.51"), sut.generateOrdinal(BigInteger.valueOf(4)).applications.map { it.sutra })
        assertEquals(
            listOf("5.2.49", "8.2.7"),
            sut.generateOrdinal(BigInteger.valueOf(5)).applications.map { it.sutra },
        )
    }

    @Test
    fun `derives purana numerals eleven through twenty nine with dat`() {
        val expected = listOf(
            "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश",
            "विंश", "एकविंश", "द्वाविंश", "त्रयोविंश", "चतुर्विंश", "पञ्चविंश", "षड्विंश", "सप्तविंश", "अष्टाविंश", "नवविंश",
        )
        val sut = SanskritSankhyaGenerator()

        expected.forEachIndexed { index, surface ->
            val value = BigInteger.valueOf((index + 11).toLong())
            val result = sut.generateOrdinal(value)
            assertEquals(surface, result.final.surface, "$value")
            assertTrue(result.applications.any { it.sutra == "5.2.48" }, "$value")
            assertEquals(value >= BigInteger.valueOf(20), result.applications.any { it.sutra == "6.4.142" }, "$value")
        }
    }

    @Test
    fun `derives both dat and optional tamat ordinals from twenty through fifty nine`() {
        val sut = SanskritSankhyaGenerator()

        (20L..59L).forEach { number ->
            val value = BigInteger.valueOf(number)
            val cardinal = sut.generateSurface(value)
            val short = if (cardinal.endsWith("विंशति")) cardinal.dropLast(2) else cardinal.dropLast(2)
            val tamat = "${cardinal}तम"
            val variants = sut.generateOrdinalVariants(value)

            assertEquals(
                setOf(short, tamat),
                variants.map { it.final.surface }.toSet(),
                "$number: ${variants.map { result -> result.final.surface to result.applications.map { it.sutra } }}",
            )
            assertTrue(variants.all { result -> result.applications.any { it.sutra == "5.2.48" } }, "$number")
            assertTrue(variants.any { result -> result.applications.any { it.sutra == "5.2.56" } }, "$number")
            assertTrue(variants.any { result -> result.applications.none { it.sutra == "5.2.56" } }, "$number")
            assertEquals(short, sut.generateOrdinalSurface(value), "$number default")
        }
    }

    @Test
    fun `generates gender declensions for numbers`() {
        assertEquals("एकः", generator.generateDeclined(BigInteger.ONE, Linga.PUMS, Vibhakti.PRATHAMA))
        assertEquals("एका", generator.generateDeclined(BigInteger.ONE, Linga.STRI, Vibhakti.PRATHAMA))
        assertEquals("एकम्", generator.generateDeclined(BigInteger.ONE, Linga.NAPUMSAKA, Vibhakti.PRATHAMA))

        assertEquals("द्वौ", generator.generateDeclined(BigInteger.TWO, Linga.PUMS, vacana = Vacana.DVIVACANA))
        assertEquals("द्वे", generator.generateDeclined(BigInteger.TWO, Linga.STRI, vacana = Vacana.DVIVACANA))
        assertEquals("त्रयः", generator.generateDeclined(BigInteger.valueOf(3), Linga.PUMS, vacana = Vacana.BAHUVACANA))
        assertEquals("तिस्रः", generator.generateDeclined(BigInteger.valueOf(3), Linga.STRI, vacana = Vacana.BAHUVACANA))
        assertEquals("त्रीणि", generator.generateDeclined(BigInteger.valueOf(3), Linga.NAPUMSAKA, vacana = Vacana.BAHUVACANA))

        assertFailsWith<IllegalArgumentException> {
            generator.generateDeclined(BigInteger.valueOf(3), Linga.PUMS, Vibhakti.SASTHI, Vacana.BAHUVACANA)
        }
        assertFailsWith<IllegalArgumentException> {
            generator.generateDeclined(BigInteger.valueOf(4), Linga.PUMS, vacana = Vacana.EKAVACANA)
        }
    }

    @Test
    fun `declines cardinals according to the grammatical class of their head`() {
        val sut = SanskritSankhyaGenerator()
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
            assertEquals(surface, sut.generateDeclinedSurface(BigInteger.valueOf(value)), "$value")
        }
    }

    @Test
    fun `generates adhika compound phrasing`() {
        val adhika = generator.generateAdhikaSurface(BigInteger.valueOf(124))
        assertEquals("चतुर्विंशत्यधिकशत", adhika)
    }
}

private class SanskritSanskritGeneratorWrapper {
    private val g = SanskritSankhyaGenerator()
    fun generate(v: BigInteger) = g.generate(v)
    fun generateSurface(v: BigInteger) = g.generateSurface(v)
    fun generateDeclined(
        value: BigInteger,
        linga: Linga = Linga.PUMS,
        vibhakti: Vibhakti = Vibhakti.PRATHAMA,
        vacana: Vacana = Vacana.EKAVACANA,
    ) = g.generateDeclined(value, linga, vibhakti, vacana)
    fun generateAdhikaSurface(v: BigInteger) = g.generateAdhikaSurface(v)
}
