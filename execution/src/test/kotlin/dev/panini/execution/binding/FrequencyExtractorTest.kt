package dev.panini.execution.binding

import dev.panini.analysis.AnalyzedAvyaya
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.analysis.KriyaQualification
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.core.Prayoga
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.NamaVakya
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class FrequencyExtractorTest {
    @Test fun `counts cannot wrap or become negative`() {
        assertEquals(0, FrequencyExtractor.checkedCount(0))
        assertEquals(Int.MAX_VALUE, FrequencyExtractor.checkedCount(Int.MAX_VALUE.toLong()))
        for (value in listOf(-1L, Int.MAX_VALUE.toLong() + 1, Long.MAX_VALUE))
            assertFailsWith<IllegalArgumentException> { FrequencyExtractor.checkedCount(value) }
    }

    @Test
    fun `recognizes canonical and legacy abhyasa suffix stems`() {
        listOf("कृत्वः", "कृत्वस", "कृत्वा", "कृत्वसुच्", "सुच्").forEach { suffix ->
            assertTrue(FrequencyExtractor.isAbhyasa(listOf("त्रि", suffix)))
        }
        assertFalse(FrequencyExtractor.isAbhyasa(listOf("त्रि")))
    }

    @Test
    fun `isolates numeric stems from abhyasa morphology`() {
        assertEquals(
            listOf("त्रि"),
            FrequencyExtractor.numericStems(listOf("त्रि", "कृत्वसुच्")),
        )
    }

    @Test
    fun `does not reinterpret discourse recurrence as a numeric frequency`() {
        assertEquals(null, FrequencyExtractor.extractFrequencyCount(emptyList(), frame("पुनः")))
        assertEquals(null, FrequencyExtractor.extractFrequencyCount(emptyList(), frame("पुनर्")))
        assertEquals(null, FrequencyExtractor.extractFrequencyCount(emptyList(), frame("शीघ्रम्")))
    }

    private fun frame(value: String): KriyaFrame {
        val id = KriyaId("test")
        val pada = AvyayaPada(value, value)
        return KriyaFrame(
            id = id,
            vakya = NamaVakya(value, listOf(pada)),
            kriya = null,
            prayoga = Prayoga.ANIRDHARITA,
            relations = emptyList(),
            qualifications = listOf(
                KriyaQualification(id, AnalyzedAvyaya(pada), KriyaQualificationKind.FREQUENCY, value),
            ),
        )
    }
}
