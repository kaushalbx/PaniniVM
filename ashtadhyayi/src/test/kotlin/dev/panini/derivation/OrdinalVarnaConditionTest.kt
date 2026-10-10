package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya5.pada2.DvesTiyahSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.ShatKatiKatipayaChaturamThukSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.TasyaPuraneDatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada2.ShashtyadeshCasankhyadehSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class OrdinalVarnaConditionTest {
    @Test fun `dat inventory uses exact varnas and preserves compound head identity`() {
        for (base in listOf("एकादश", "द्वादश", "त्रयोदश", "चतुर्दश", "पञ्चदश", "षोडश", "सप्तदश", "अष्टादश", "नवदश")) {
            for (surface in listOf(base, base + "ँ", base + "ऽ")) {
                val original = state(surface)
                assertTrue(TasyaPuraneDatSutra.matches(original), surface)
                val result = TasyaPuraneDatSutra.apply(original).state
                assertEquals(original.terms.single(), result.terms.first())
                assertEquals(ItProcessingPhase.RAW_UPADESHA, result.terms.last().itProcessingPhase)
                assertFalse(TasyaPuraneDatSutra.matches(result))
            }
        }
        for (surface in listOf("एकादशा", "द्वादशन्", "नवदशि")) assertFalse(TasyaPuraneDatSutra.matches(state(surface)))
        val compound = state("एकविंशति")
        assertTrue(TasyaPuraneDatSutra.matches(compound.copy(terms = compound.terms.map { it.copy(compoundHeadUpadesha = "विंशति") })))
        assertFalse(TasyaPuraneDatSutra.matches(state("एकादश", purana = false)))
        val blocked = state("एकादश").let { it.copy(terms = listOf(DerivationTerm("prior", "अँऽ", TermKind.PRATYAYA)) + it.terms) }
        assertFalse(TasyaPuraneDatSutra.matches(blocked))
    }

    @Test fun `shashtyadi guard ignores signs without accepting numeral prefixes`() {
        for (base in listOf("षष्टि", "सप्तति", "अशीति", "नवति")) {
            val original = state(base + "ँऽ").let {
                it.copy(terms = it.terms + DerivationTerm("dat", "डट्", TermKind.PRATYAYA, upadesha = "डट्"))
            }
            assertTrue(ShashtyadeshCasankhyadehSutra.matches(original), base)
            val result = ShashtyadeshCasankhyadehSutra.apply(original).state
            assertEquals(original.terms.first(), result.terms.first())
            assertEquals(original.terms.last(), result.terms.last())
            assertEquals("तमट्", result.terms[1].upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, result.terms[1].itProcessingPhase)
            assertFalse(ShashtyadeshCasankhyadehSutra.matches(result))
            val prefixed = original.copy(terms = listOf(original.terms.first().copy(surface = "एक" + base)) + original.terms.drop(1))
            assertFalse(ShashtyadeshCasankhyadehSutra.matches(prefixed))
            assertFalse(ShashtyadeshCasankhyadehSutra.matches(original.copy(samjnas = emptySet())))
        }
    }
    private fun state(surface: String, purana: Boolean = true) = DerivationState(
        terms = listOf(DerivationTerm("numeral", surface, TermKind.PRATIPADIKA)),
        samjnas = if (purana) setOf(SamjnaAssignment("numeral", Samjna.PURANA)) else emptySet(),
    )

    @Test fun `tiya condition ignores orthographic signs but retains exact numeral and semantic domain`() {
        for (surface in listOf("द्वि", "द्विँ", "द्विऽ")) {
            val original = state(surface)
            assertTrue(DvesTiyahSutra.matches(original))
            val result = DvesTiyahSutra.apply(original).state
            assertEquals(original.terms.single(), result.terms.first())
            assertEquals(original.samjnas, result.samjnas)
            assertEquals("तीय", result.terms.last().surface)
            assertEquals("5.2.54", result.terms.last().createdBySutra)
            assertFalse(DvesTiyahSutra.matches(result))
        }
        for (surface in listOf("द्वी", "द्व", "त्रि")) assertFalse(DvesTiyahSutra.matches(state(surface)))
        assertFalse(DvesTiyahSutra.matches(state("द्वि", purana = false)))
    }

    @Test fun `thuk condition retains supported bases and raw it lifecycles`() {
        for (surface in listOf("चतुर्", "चतुँर्", "चतुर्ऽ", "षष्", "षष्ऽ")) {
            val original = state(surface)
            assertTrue(ShatKatiKatipayaChaturamThukSutra.matches(original))
            val result = ShatKatiKatipayaChaturamThukSutra.apply(original).state
            assertEquals(original.terms.single(), result.terms.first())
            assertEquals(original.samjnas, result.samjnas)
            assertEquals(listOf("थुँक्", "डट्"), result.terms.drop(1).map { it.upadesha })
            assertTrue(result.terms.drop(1).all { it.itProcessingPhase == ItProcessingPhase.RAW_UPADESHA })
            assertFalse(ShatKatiKatipayaChaturamThukSutra.matches(result))
        }
        // Broader coverage named by the sutra needs separate grammatical implementation.
        for (surface in listOf("चतुः", "षट्", "कति", "कतिपय"))
            assertFalse(ShatKatiKatipayaChaturamThukSutra.matches(state(surface)))
        assertFalse(ShatKatiKatipayaChaturamThukSutra.matches(state("षष्", purana = false)))
    }
}
