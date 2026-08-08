package dev.panini.execution.binding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FrequencyExtractorTest {

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
}
