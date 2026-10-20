package dev.panini.linganushasanam

import dev.panini.core.Linga
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CanonicalCorpusCompletenessTest {
    @Test
    fun `registry contains every canonical sutra exactly once`() {
        val sutras = LinganushasanamRegistry.canonicalSutras

        assertEquals(189, sutras.size)
        assertEquals((1..189).map(Int::toString), sutras.map { it.number })
        sutras.forEach { assertSame(it, LinganushasanamRegistry.require(it.number)) }
    }

    @Test
    fun `source numbering follows the Ashtadhyayi com edition`() {
        assertEquals("क्तिन्नन्तः", LinganushasanamRegistry.require("9").text)
        assertEquals("ईकारान्तश्च", LinganushasanamRegistry.require("10").text)
        assertEquals("द्वन्द्वैकत्वम्", LinganushasanamRegistry.require("124").text)
        assertEquals("सर्वादीनि सर्वनामानि", LinganushasanamRegistry.require("189").text)
    }

    @Test
    fun `all six canonical sections and their gender scopes are represented`() {
        val boundaries = mapOf(
            "1" to LinganushasanaSection.STRILINGA,
            "35" to LinganushasanaSection.PUMLINGA,
            "118" to LinganushasanaSection.NAPUMSAKALINGA,
            "171" to LinganushasanaSection.STRIPUMSALINGA,
            "176" to LinganushasanaSection.PUMNAPUMSAKALINGA,
            "183" to LinganushasanaSection.AVISISHTALINGA,
        )

        boundaries.forEach { (number, section) ->
            assertEquals(section, LinganushasanamRegistry.require(number).section)
        }
        assertEquals(setOf(Linga.STRI, Linga.PUMS), LinganushasanamRegistry.require("172").targetLingas)
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), LinganushasanamRegistry.require("177").targetLingas)
        assertEquals(Linga.entries.toSet(), LinganushasanamRegistry.require("189").targetLingas)
    }

    @Test
    fun `every canonical vidhi is executable from established provenance`() {
        val engine = LinganushasanamEngine()

        LinganushasanamRegistry.canonicalSutras
            .filter { it.kind == LinganushasanaRuleKind.VIDHI }
            .forEach { sutra ->
            val result = engine.resolveOrNull(
                LingaRuleContext(
                    pratipadika = "परीक्षाप्रातिपदिक",
                    applicableSutraNumbers = setOf(sutra.number),
                )
            )

            assertEquals(sutra.number, result?.ruleId)
            assertTrue(result!!.lingas.isNotEmpty())
            assertTrue(result.lingas.all { it in sutra.targetLingas })
        }
    }

    @Test
    fun `adhikara sutras establish scope but never assign a word directly`() {
        val adhikaras = LinganushasanamRegistry.canonicalSutras
            .filter { it.kind == LinganushasanaRuleKind.ADHIKARA }

        assertEquals(setOf("1", "2", "35", "60", "118", "126", "171", "176", "183"), adhikaras.map { it.number }.toSet())
        adhikaras.forEach { sutra ->
            assertEquals(
                null,
                LinganushasanamEngine(listOf(sutra)).resolveOrNull(
                    LingaRuleContext("परीक्षा", applicableSutraNumbers = setOf(sutra.number))
                ),
            )
        }
    }

    @Test
    fun `every canonical sutra has a dedicated implementation`() {
        assertEquals((1..189).map(Int::toString).toSet(), LinganushasanamRegistry.dedicatedCanonicalNumbers)
    }
}
