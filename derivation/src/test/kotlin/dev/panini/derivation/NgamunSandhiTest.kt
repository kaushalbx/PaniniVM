package dev.panini.derivation

import dev.panini.shiksha.*
import kotlin.test.*

class NgamunSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `short vowel nasal finals receive corresponding initial augment`() {
        for ((left, right, expected) in listOf(
            Triple("भजन्", "अति", "भजन्नति"), Triple("रुदन्", "इव", "रुदन्निव"),
            Triple("पश्यन्", "उवाच", "पश्यन्नुवाच"), Triple("वण्", "आस्ते", "वण्णास्ते"),
            Triple("प्रत्यङ्", "आस्ते", "प्रत्यङ्ङास्ते"))) {
            assertEquals(expected, engine.render(engine.join(left, right)))
        }
    }

    @Test fun `long vowels other nasals and consonant followers are excluded`() {
        for ((left, right) in listOf("भवान्" to "आस्ते", "प्राङ्" to "आस्ते",
            "कम्" to "अत्र", "कञ्" to "अत्र", "सन्" to "गच्छति")) {
            assertTrue(engine.join(left, right).applications.none { it.sutra == "8.3.32" })
        }
    }

    @Test fun `augment belongs to following pada with exact it provenance`() {
        val result = engine.join("कुर्वन्", "आस्ते")
        assertEquals("कुर्वन्नास्ते", engine.render(result))
        val left = result.final.terms.first()
        val right = result.final.terms.last()
        assertEquals("कुर्वन्", left.surface)
        assertEquals(Vyanjana.NA, right.varnas.first())
        assertTrue(right.itMarkerProvenance.any { it.designationSutra == "1.3.3" })
        assertTrue(right.itMarkerProvenance.any { it.designationSutra == "1.3.2" })
        assertTrue(result.applications.any { it.sutra == "1.1.46" })
        assertEquals(1, result.applications.count { it.sutra == "8.3.32" })
    }
}
