package dev.panini.execution

import kotlin.test.*

class PrakriyaDefinitionMarkerParserTest {
    @Test fun `product noun belongs to result-producing predicate rather than qualifier list`() {
        val result = assertNotNull(PrakriyaDefinitionMarkerParser.qualifiers(
            "परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।"))
        assertEquals(setOf(PrakriyaDefinitionQualifier.PRAKRIYA), result.qualifiers)
        assertFailsWith<IllegalArgumentException> {
            PrakriyaDefinitionMarkerParser.qualifiers(
                "परीक्षण + सुँ इति प्रक्रिया + सुँ सङ्ख्या + अम् असँ + लट् + तिप् ।")
        }
    }

    @Test fun `legacy qualifiers retain recognized feminine morphology`() {
        for (modifier in listOf("नित्य + सुँ", "नित्य + टाप् + सुँ")) {
            val result = assertNotNull(PrakriyaDefinitionMarkerParser.qualifiers(
                "परीक्षण + सुँ इति $modifier प्रक्रिया + सुँ असँ + लट् + तिप् ।"))
            assertEquals(setOf(PrakriyaDefinitionQualifier.NITYA, PrakriyaDefinitionQualifier.PRAKRIYA), result.qualifiers)
        }
    }

    @Test fun `legacy qualifiers cannot silently erase derivation case number or unknown nominals`() {
        for (modifier in listOf("नित्य + मतुप् + सुँ", "नित्य + तरप् + सुँ",
            "नित्य + जस्", "नित्य + औ", "नित्य + अम्", "अज्ञात + सुँ",
            "अप + वद् + णिच् + घञ् + सुँ")) {
            assertFailsWith<IllegalArgumentException>(modifier) {
                PrakriyaDefinitionMarkerParser.qualifiers(
                    "परीक्षण + सुँ इति $modifier प्रक्रिया + सुँ असँ + लट् + तिप् ।")
            }
        }
    }
}
