package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya5.pada2.NantadAsankhyaderMatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.EdhaccaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.SankhyayascavidhartheDhaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.DvitrichaturbhyahSucSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.SankhyayahKriyaAbhyavrttiKrtvasucSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class NumeralCompletionVarnaTest {
    @Test fun `frequency completion guards inspect exact varna inventories`() {
        for (form in listOf("द्विः", "त्रिः", "चतुः", "सकृत्")) {
            for (surface in listOf(form, form + "ऽ"))
                assertFalse(DvitrichaturbhyahSucSutra.matches(state("द्वि", "द्वि", Samjna.SUC, surface)))
        }
        assertFalse(DvitrichaturbhyahSucSutra.matches(state("द्वि", "द्वि", Samjna.SUC, "द्विँः")))
        assertTrue(DvitrichaturbhyahSucSutra.matches(state("द्वि", "द्वि", Samjna.SUC, "द्वीः")))
        for (surface in listOf("कृत्वः", "कृत्वँः", "कृत्वःऽ"))
            assertFalse(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(state("पञ्च", "पञ्चन्", Samjna.KRTVASUC, surface)))
        val original = state("पञ्च", "पञ्चन्", Samjna.KRTVASUC, "कृत्वाः")
        assertTrue(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(original))
        val result = SankhyayahKriyaAbhyavrttiKrtvasucSutra.apply(original).state
        assertEquals(original.terms, result.terms.dropLast(1))
        assertEquals(original.samjnas + SamjnaAssignment(result.terms.last().id, Samjna.TADDHITA), result.samjnas)
        assertFalse(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(result))
        assertFalse(DvitrichaturbhyahSucSutra.matches(state("द्वि", "द्वि", Samjna.DHA)))
        assertFalse(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(state("पञ्च", "पञ्चन्", Samjna.SUC)))
    }
    private fun state(surface: String, upadesha: String, request: Samjna, prior: String? = null) = DerivationState(
        listOfNotNull(prior?.let { DerivationTerm("prior", it, TermKind.AGAMA, upadesha = "fixture") },
            DerivationTerm("base", surface, TermKind.PRATIPADIKA, upadesha = upadesha)),
        samjnas = setOf(SamjnaAssignment("base", request)),
    )

    @Test fun `mat completed form guards use exact varnas`() {
        for (surface in listOf("म", "मँ", "मऽ"))
            assertFalse(NantadAsankhyaderMatSutra.matches(state("पञ्च", "पञ्चन्", Samjna.PURANA, surface)))
        val original = state("पञ्च", "पञ्चन्", Samjna.PURANA, "मा")
        assertTrue(NantadAsankhyaderMatSutra.matches(original))
        val result = NantadAsankhyaderMatSutra.apply(original).state
        assertEquals(original.terms, result.terms.dropLast(1))
        assertEquals("मट्", result.terms.last().upadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, result.terms.last().itProcessingPhase)
        assertFalse(NantadAsankhyaderMatSutra.matches(result))
    }

    @Test fun `dha completion guards preserve exact length and semantic exclusions`() {
        for (surface in listOf("धा", "धाँ", "धाऽ")) {
            assertFalse(EdhaccaSutra.matches(state("एक", "एक", Samjna.DHA, surface)))
            assertFalse(SankhyayascavidhartheDhaSutra.matches(state("द्वि", "द्वि", Samjna.DHA, surface)))
        }
        assertTrue(EdhaccaSutra.matches(state("एक", "एक", Samjna.DHA, "ध")))
        assertTrue(SankhyayascavidhartheDhaSutra.matches(state("द्वि", "द्वि", Samjna.DHA, "ध")))
        val annotated = state("एकँऽ", "fixture", Samjna.DHA)
        assertTrue(EdhaccaSutra.matches(annotated))
        val result = EdhaccaSutra.apply(annotated).state
        assertEquals(annotated.terms.first(), result.terms.first())
        assertEquals(annotated.samjnas, result.samjnas)
        assertEquals("5.3.43", result.terms.last().createdBySutra)
        assertFalse(EdhaccaSutra.matches(result))
        assertFalse(EdhaccaSutra.matches(state("एका", "fixture", Samjna.DHA)))
        assertFalse(EdhaccaSutra.matches(state("एक", "एक", Samjna.PURANA)))
        assertFalse(SankhyayascavidhartheDhaSutra.matches(state("द्वि", "द्वि", Samjna.PURANA)))
        assertFalse(EdhaccaSutra.matches(annotated.copy(terms = annotated.terms.map { it.copy(kind = TermKind.DHATU) })))
    }
}
