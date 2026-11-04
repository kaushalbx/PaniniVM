package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdhikaraDocumentTest {

    private fun scope(source: String) = runCatching {
        PvmScript.parse(source).filterIsInstance<PvmScriptStatement.AdhikaraDefinition>().singleOrNull()?.scope
    }.getOrNull()

    @Test
    fun `returns presentation and semantic domain from one parsed header`() {
        val declaration = kotlin.test.assertNotNull(scope("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।"))
        assertEquals("गणित + अण् + सुँ", declaration.domain)
        assertEquals("गणित + अण्", declaration.domainIdentity)
    }

    @Test
    fun `extracts domain from lexical and derived adhikara markers`() {
        assertEquals(
            "गणित + सुँ",
            scope("गणित + सुँ इति अधिकार + सुँ ।")?.domain,
        )
        assertEquals(
            "गणित",
            scope("गणित + सुँ इति अधिकार + सुँ ।")?.domainIdentity,
        )
        assertEquals(
            "गणित + अण् + सुँ",
            scope("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।")?.domain,
        )
        assertEquals(
            "गणित + अण्",
            scope("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।")?.domainIdentity,
        )
    }

    @Test
    fun `rejects marker words without adhikara construction`() {
        assertNull(scope("अधिकार + अम् पठ् + लोट् + सिप् ।"))
        assertNull(scope("गणित + सुँ अधिकार + सुँ ।"))
        assertNull(scope("गणित + सुँ इति अधि + कृ + ल्युट् + सुँ ।"))
    }
}
