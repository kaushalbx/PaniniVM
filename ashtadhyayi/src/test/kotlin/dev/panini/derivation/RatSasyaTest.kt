package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada2.RatSasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.SamyogantasyaLopaSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class RatSasyaTest {
    private fun state(surface: String, pada: Boolean = true) = DerivationState(
        listOf(DerivationTerm("pada", surface, TermKind.PRATIPADIKA, upadesha = "fixture", createdBySutra = "fixture")),
        samjnas = if (pada) setOf(SamjnaAssignment("pada", Samjna.PADA)) else emptySet(),
    )

    @Test fun `r permits only s deletion at cluster final pada boundary`() {
        for (surface in listOf("चतुर्स्", "क्रोष्टुर्स्", "पितुर्स्")) {
            val original = state(surface)
            assertTrue(RatSasyaSutra.matches(original))
            assertFalse(SamyogantasyaLopaSutra.matches(original))
            val result = RatSasyaSutra.apply(original).state
            assertEquals(original.terms.single().varnas.dropLast(1), result.terms.single().varnas)
            assertEquals(original.terms.single().id, result.terms.single().id)
            assertEquals(original.terms.single().upadesha, result.terms.single().upadesha)
            assertEquals(original.terms.single().createdBySutra, result.terms.single().createdBySutra)
            assertEquals(original.samjnas, result.samjnas)
            assertEquals("8.2.24", result.substitutions.single().sutra)
            assertFalse(RatSasyaSutra.matches(result))
        }
        for (surface in listOf("ऊर्ज्", "अमार्ट्", "ऊर्क्")) {
            assertFalse(SamyogantasyaLopaSutra.matches(state(surface)))
            assertFalse(RatSasyaSutra.matches(state(surface)))
        }
        assertFalse(RatSasyaSutra.matches(state("चतुर्स्", pada = false)))
        assertFalse(RatSasyaSutra.matches(state("रस")))
        assertTrue(SamyogantasyaLopaSutra.matches(state("अन्त्")))
    }

    @Test fun `exact s deletion retains nasalization and avagraha boundary`() {
        val original = state("चतुँर्स्ऽ")
        val result = RatSasyaSutra.apply(original).state.terms.single()
        assertTrue(result.phonologicalText.effectiveVarnas[3].nasalized)
        assertEquals("चतुँर्ऽ", result.surface)
    }
}
