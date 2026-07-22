package dev.panini.sankhya

import dev.panini.derivation.TermKind
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SankhyaGeneratorTest {

    private val generator = SankhyaGenerator()

    @Test
    fun `generates primitives correctly`() {
        assertEquals("एक", generator.cardinal(BigInteger.ONE).final.surface)
        assertEquals("द्वि", generator.cardinal(BigInteger.valueOf(2)).final.surface)
        assertEquals("दशन्", generator.cardinal(BigInteger.TEN).final.surface)
        assertEquals("विंशति", generator.cardinal(BigInteger.valueOf(20)).final.surface)
        assertEquals("पञ्चन्", generator.cardinal(BigInteger.valueOf(5)).final.surface)
        assertEquals("षष्", generator.cardinal(BigInteger.valueOf(6)).final.surface)
        assertEquals("सप्तन्", generator.cardinal(BigInteger.valueOf(7)).final.surface)
        assertEquals("अष्टन्", generator.cardinal(BigInteger.valueOf(8)).final.surface)
        assertEquals("नवन्", generator.cardinal(BigInteger.valueOf(9)).final.surface)
    }

    @Test
    fun `generates dvavimshati via Sutra 6_3_47`() {
        val result = generator.cardinal(BigInteger.valueOf(22))
        assertEquals("द्वाविंशति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.47" })
    }

    @Test
    fun `generates trayovimshati via Sutra 6_3_48`() {
        val result = generator.cardinal(BigInteger.valueOf(23))
        assertEquals("त्रयोविंशति", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.3.48" })
    }

    @Test
    fun `generates lexicalized shodasha without a fabricated Sutra trace`() {
        val result = generator.cardinal(BigInteger.valueOf(16))
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
            assertEquals(surface, generator.cardinal(BigInteger.valueOf((index + 11).toLong())).final.surface, "${index + 11}")
        }
    }

    @Test
    fun `trayovimshati records each grammatical operation`() {
        val sutras = generator.cardinal(BigInteger.valueOf(23)).applications.map { it.sutra }

        assertTrue("6.3.48" in sutras)
        assertTrue("8.2.66" in sutras)
        assertTrue("6.1.114" in sutras)
        assertTrue("6.1.87" in sutras)
    }

    @Test
    fun `applies the optional substitutions from forty onward`() {
        val fortyTwo = generator.cardinal(BigInteger.valueOf(42))
        val fortyEight = generator.cardinal(BigInteger.valueOf(48))

        assertEquals("द्वाचत्वारिंशत्", fortyTwo.final.surface)
        assertEquals("अष्टाचत्वारिंशत्", fortyEight.final.surface)
        assertTrue(fortyTwo.applications.any { it.sutra == "6.3.49" })
        assertTrue(fortyEight.applications.any { it.sutra == "6.3.49" })
    }

    @Test
    fun `returns both branches licensed by 6_3_49`() {
        val fortyTwo = SankhyaGenerator().cardinalVariants(BigInteger.valueOf(42))
        val fortyEight = SankhyaGenerator().cardinalVariants(BigInteger.valueOf(48))

        assertEquals(setOf("द्विचत्वारिंशत्", "द्वाचत्वारिंशत्"), fortyTwo.map { it.final.surface }.toSet())
        assertEquals(setOf("अष्टचत्वारिंशत्", "अष्टाचत्वारिंशत्"), fortyEight.map { it.final.surface }.toSet())
        assertTrue(fortyTwo.any { result -> result.applications.any { it.sutra == "6.3.49" } })
        assertTrue(fortyTwo.any { result -> result.applications.none { it.sutra == "6.3.49" } })

        val fortyThree = SankhyaGenerator().cardinalVariants(BigInteger.valueOf(43))
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
            val result = generator.cardinal(BigInteger.valueOf(number))
            assertEquals(surface, result.final.surface, "$number")
            assertTrue(result.applications.none { it.sutra in setOf("6.3.47", "6.3.48", "6.3.49") })
        }
    }

    @Test
    fun `uses adhika for remainders above a completed magnitude`() {
        assertEquals("शत", generator.cardinal(BigInteger.valueOf(100)).final.surface)
        assertEquals("एकाधिकशत", generator.cardinal(BigInteger.valueOf(101)).final.surface)
        assertEquals("चतुर्विंशत्यधिकशत", generator.cardinal(BigInteger.valueOf(124)).final.surface)
        assertEquals("द्विशत", generator.cardinal(BigInteger.valueOf(200)).final.surface)
        assertEquals("त्रिशत", generator.cardinal(BigInteger.valueOf(300)).final.surface)
        assertEquals("अष्टशत", generator.cardinal(BigInteger.valueOf(800)).final.surface)
    }

    @Test
    fun `derives purana numerals one through ten`() {
        val expected = listOf("प्रथम", "द्वितीय", "तृतीय", "चतुर्थ", "पञ्चम", "षष्ठ", "सप्तम", "अष्टम", "नवम", "दशम")
        expected.forEachIndexed { index, surface ->
            assertEquals(surface, SankhyaGenerator().ordinal(BigInteger.valueOf((index + 1).toLong())).final.surface)
        }
    }

    @Test
    fun `purana results retain their actual sutra provenance`() {
        val sut = SankhyaGenerator()

        assertTrue(sut.ordinal(BigInteger.ONE).applications.isEmpty())
        val second = sut.ordinal(BigInteger.TWO)
        assertEquals(listOf("5.2.54"), second.applications.map { it.sutra })
        assertEquals(listOf("द्वि", "तीय"), second.final.terms.map { it.surface })
        assertEquals(TermKind.PRATYAYA, second.final.terms.last().kind)
        assertEquals("5.2.54", second.final.terms.last().createdBySutra)

        val third = sut.ordinal(BigInteger.valueOf(3))
        assertEquals(listOf("5.2.55"), third.applications.map { it.sutra })
        assertEquals(listOf("तृ", "तीय"), third.final.terms.map { it.surface })
        assertEquals("5.2.55", third.final.terms.last().createdBySutra)
        val fourth = sut.ordinal(BigInteger.valueOf(4))
        assertEquals(listOf("5.2.51"), fourth.applications.map { it.sutra })
        assertEquals(listOf("चतुर्", "थ", ""), fourth.final.terms.map { it.surface })
        assertEquals("5.2.51", fourth.final.terms.single { it.upadesha == "थुक्" }.createdBySutra)

        val sixth = sut.ordinal(BigInteger.valueOf(6))
        assertEquals("षष्ठ", sixth.final.surface)
        assertEquals(listOf("5.2.51", "8.4.41"), sixth.applications.map { it.sutra })
        assertEquals(listOf("षष्", "ठ", ""), sixth.final.terms.map { it.surface })
        assertEquals(
            listOf("5.2.49", "8.2.7"),
            sut.ordinal(BigInteger.valueOf(5)).applications.map { it.sutra },
        )

        val twentiethTamat = sut.ordinalVariants(BigInteger.valueOf(20))
            .single { result -> result.applications.any { it.sutra == "5.2.56" } }
        assertEquals("5.2.48", twentiethTamat.final.terms.single { it.upadesha == "डट्" }.createdBySutra)
        assertEquals("5.2.56", twentiethTamat.final.terms.single { it.upadesha == "तमट्" }.createdBySutra)
    }

    @Test
    fun `derives purana numerals eleven through twenty nine with dat`() {
        val expected = listOf(
            "एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश",
            "विंश", "एकविंश", "द्वाविंश", "त्रयोविंश", "चतुर्विंश", "पञ्चविंश", "षड्विंश", "सप्तविंश", "अष्टाविंश", "नवविंश",
        )
        val sut = SankhyaGenerator()

        expected.forEachIndexed { index, surface ->
            val value = BigInteger.valueOf((index + 11).toLong())
            val result = sut.ordinal(value)
            assertEquals(surface, result.final.surface, "$value")
            assertTrue(result.applications.any { it.sutra == "5.2.48" }, "$value")
            assertEquals(value >= BigInteger.valueOf(20), result.applications.any { it.sutra == "6.4.142" }, "$value")
        }
    }

    @Test
    fun `derives both dat and optional tamat ordinals from twenty through fifty nine`() {
        val sut = SankhyaGenerator()

        (20L..59L).forEach { number ->
            val value = BigInteger.valueOf(number)
            val cardinal = sut.cardinal(value).final.surface
            val short = if (cardinal.endsWith("विंशति")) cardinal.dropLast(2) else cardinal.dropLast(2)
            val tamat = "${cardinal}तम"
            val variants = sut.ordinalVariants(value)

            assertEquals(
                setOf(short, tamat),
                variants.map { it.final.surface }.toSet(),
                "$number: ${variants.map { result -> result.final.surface to result.applications.map { it.sutra } }}",
            )
            assertTrue(variants.all { result -> result.applications.any { it.sutra == "5.2.48" } }, "$number")
            assertTrue(variants.any { result -> result.applications.any { it.sutra == "5.2.56" } }, "$number")
            assertTrue(variants.any { result -> result.applications.none { it.sutra == "5.2.56" } }, "$number")
            assertEquals(short, sut.ordinal(value).final.surface, "$number default")
        }
    }

    @Test
    fun `makes tamat obligatory only for unprefixed shashti through navati`() {
        val sut = SankhyaGenerator()
        val unprefixed = setOf(60L, 70L, 80L, 90L)

        (60L..99L).forEach { number ->
            val value = BigInteger.valueOf(number)
            val cardinal = sut.cardinal(value).final.surface
            val tamat = "${cardinal}तम"
            val variants = sut.ordinalVariants(value)

            if (number in unprefixed) {
                assertEquals(setOf(tamat), variants.map { it.final.surface }.toSet(), "$number")
                assertTrue(variants.all { result -> result.applications.any { it.sutra == "5.2.58" } }, "$number")
                assertTrue(variants.all { result -> result.applications.none { it.sutra == "5.2.56" } }, "$number")
                assertEquals(tamat, sut.ordinal(value).final.surface, "$number default")
            } else {
                val short = cardinal.dropLast(1)
                assertEquals(setOf(short, tamat), variants.map { it.final.surface }.toSet(), "$number")
                assertTrue(variants.any { result -> result.applications.any { it.sutra == "5.2.56" } }, "$number")
                assertTrue(variants.all { result -> result.applications.none { it.sutra == "5.2.58" } }, "$number")
                assertEquals(short, sut.ordinal(value).final.surface, "$number default")
            }
        }

        val eightyOne = sut.ordinal(BigInteger.valueOf(81))
        assertEquals("एकाशीति", eightyOne.initial.terms.single().upadesha)
        assertEquals("अशीति", eightyOne.initial.terms.single().compoundHeadUpadesha)
    }

    @Test
    fun `makes tamat obligatory for shatadi ordinals and their compounds`() {
        val sut = SankhyaGenerator()
        val values = listOf(100L, 101L, 124L, 200L, 999L, 1_000L, 1_001L, 10_000L, 100_000L, 1_000_000L, 10_000_000L, 10_000_001L)

        values.forEach { number ->
            val value = BigInteger.valueOf(number)
            val expected = "${sut.cardinal(value).final.surface}तम"
            val variants = sut.ordinalVariants(value)

            assertEquals(setOf(expected), variants.map { it.final.surface }.toSet(), "$number")
            assertTrue(variants.all { result -> result.applications.any { it.sutra == "5.2.48" } }, "$number")
            assertTrue(variants.all { result -> result.applications.any { it.sutra == "5.2.57" } }, "$number")
            assertEquals(expected, sut.ordinal(value).final.surface, "$number default")
        }

        assertFailsWith<IllegalArgumentException> { sut.ordinal(BigInteger.ZERO) }

        val hundredAndOne = sut.ordinal(BigInteger.valueOf(101))
        assertEquals("एकाधिकशत", hundredAndOne.initial.terms.single().upadesha)
        assertEquals("शत", hundredAndOne.initial.terms.single().compoundHeadUpadesha)
    }

}
