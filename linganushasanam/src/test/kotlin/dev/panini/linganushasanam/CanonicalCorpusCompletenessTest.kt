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

        assertEquals(191, sutras.size)
        assertEquals((1..191).map(Int::toString), sutras.map { it.number })
        sutras.forEach { assertSame(it, LinganushasanamRegistry.require(it.number)) }
    }

    @Test
    fun `dedicated implementations preserve canonical metadata exactly`() {
        CanonicalLinganushasanamCorpus.entries.zip(LinganushasanamRegistry.canonicalSutras)
            .forEach { (entry, sutra) ->
                assertEquals(entry.number, sutra.number, "number for canonical entry ${entry.number}")
                assertEquals(entry.text, sutra.text, "text for canonical sūtra ${entry.number}")
                assertEquals(entry.section, sutra.section, "section for canonical sūtra ${entry.number}")
                assertEquals(entry.kind, sutra.kind, "kind for canonical sūtra ${entry.number}")
                assertEquals(entry.lingas, sutra.targetLingas, "gender scope for canonical sūtra ${entry.number}")
            }
    }

    @Test
    fun `normalized source numbering preserves the two corrected positions`() {
        assertEquals("क्तिन्नन्तः ।", LinganushasanamRegistry.require("9").text)
        assertEquals("ईकारान्तश्च ।", LinganushasanamRegistry.require("10").text)
        assertEquals("दीधितिः स्त्रियाम् ।", LinganushasanamRegistry.require("101").text)
        assertEquals("दिनाहनी नपुंसके ।", LinganushasanamRegistry.require("102").text)
    }

    @Test
    fun `all six canonical sections and their gender scopes are represented`() {
        val boundaries = mapOf(
            "1" to LinganushasanaSection.STRILINGA,
            "35" to LinganushasanaSection.PUMLINGA,
            "118" to LinganushasanaSection.NAPUMSAKALINGA,
            "173" to LinganushasanaSection.STRIPUMSALINGA,
            "178" to LinganushasanaSection.PUMNAPUMSAKALINGA,
            "185" to LinganushasanaSection.AVISISHTALINGA,
        )

        boundaries.forEach { (number, section) ->
            assertEquals(section, LinganushasanamRegistry.require(number).section)
        }
        assertEquals(setOf(Linga.STRI, Linga.PUMS), LinganushasanamRegistry.require("174").targetLingas)
        assertEquals(setOf(Linga.PUMS, Linga.NAPUMSAKA), LinganushasanamRegistry.require("179").targetLingas)
        assertEquals(Linga.entries.toSet(), LinganushasanamRegistry.require("191").targetLingas)
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

        assertEquals(setOf("1", "2", "35", "60", "118", "127", "173", "178", "185"), adhikaras.map { it.number }.toSet())
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
        assertEquals((1..191).map(Int::toString).toSet(), LinganushasanamRegistry.dedicatedCanonicalNumbers)
    }
}
