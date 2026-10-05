package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AdhikaraHeaderParserTest {

    @Test
    fun `returns presentation and semantic domain from one parsed header`() {
        assertEquals(
            ParsedAdhikaraHeader(domainSource = "गणित + अण् + सुँ", domainIdentity = "गणित + अण्"),
            AdhikaraHeaderParser.parse("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।"),
        )
    }

    @Test
    fun `extracts domain from lexical and derived adhikara markers`() {
        assertEquals(
            "गणित + सुँ",
            AdhikaraHeaderParser.domain("गणित + सुँ इति अधिकार + सुँ ।"),
        )
        assertEquals(
            "गणित",
            AdhikaraHeaderParser.domainIdentity("गणित + सुँ इति अधिकार + सुँ ।"),
        )
        assertEquals(
            "गणित + अण् + सुँ",
            AdhikaraHeaderParser.domain("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।"),
        )
        assertEquals(
            "गणित + अण्",
            AdhikaraHeaderParser.domainIdentity("गणित + अण् + सुँ इति अधि+कृ+घञ्+सुँ ।"),
        )
    }

    @Test
    fun `rejects marker words without adhikara construction`() {
        assertNull(AdhikaraHeaderParser.domain("अधिकार + अम् पठ् + लोट् + सिप् ।"))
        assertNull(AdhikaraHeaderParser.domain("गणित + सुँ अधिकार + सुँ ।"))
        assertNull(AdhikaraHeaderParser.domain("गणित + सुँ इति अधि + कृ + ल्युट् + सुँ ।"))
    }
}
