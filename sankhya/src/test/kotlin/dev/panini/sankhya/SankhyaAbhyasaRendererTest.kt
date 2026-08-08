package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals

class SankhyaAbhyasaRendererTest {
    private val renderer = SankhyaAbhyasaRenderer()

    @Test
    fun `isolates numeral stems from grammatical markers`() {
        assertEquals(listOf("पञ्च"), renderer.numericStems(listOf("पञ्च", "कृत्वसुच्")))
        assertEquals(listOf("त्रि"), renderer.numericStems(listOf("त्रि", "धा")))
        assertEquals(listOf("त्रि", "धा"), SankhyaAbhyasaMarkers.numericFrequencyStems(listOf("त्रि", "धा")))
    }

    @Test
    fun `renders krtvasuc suc and dha forms`() {
        assertEquals("पञ्चकृत्वः", renderer.render("कृत्वः", 5))
        assertEquals("द्विः", renderer.render("सुच्", 2))
        assertEquals("पञ्चकृत्वः", renderer.render("सुच्", 5))
        assertEquals("त्रिधा", renderer.render("धा", 3))
    }
}
