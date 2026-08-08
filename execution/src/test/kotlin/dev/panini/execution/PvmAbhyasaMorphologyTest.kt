package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class PvmAbhyasaMorphologyTest {

    @Test
    fun `isolates numeric stems from readable morphology`() {
        assertEquals(
            listOf("पञ्च"),
            PvmAbhyasaMorphology.numericStems(listOf("पञ्च", "कृत्वसुच्")),
        )
        assertEquals(listOf("त्रि"), PvmAbhyasaMorphology.numericStems(listOf("त्रि", "धा")))
    }

    @Test
    fun `renders krtvas suc and distribution surfaces`() {
        assertEquals("पञ्चकृत्वः", PvmAbhyasaMorphology.surface("कृत्वः", 5, "पञ्च"))
        assertEquals("द्विः", PvmAbhyasaMorphology.surface("सुच्", 2, "द्वि"))
        assertEquals("पञ्चकृत्वः", PvmAbhyasaMorphology.surface("सुच्", 5, "पञ्च"))
        assertEquals("त्रिधा", PvmAbhyasaMorphology.surface("धा", 3, "त्रि"))
    }
}
