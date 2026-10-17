package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.AtomSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.SvamorNapumsakatSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class NeuterSingularAffixLifecycleTest {
    private fun state(stem: String, sup: SupAffix) = DerivationState(listOf(
        DerivationTerm("stem", stem, TermKind.PRATIPADIKA),
        sup.term("suffix").copy(surface = if (sup == SupAffix.SU) "स्" else "अम्", itProcessingPhase = ItProcessingPhase.PROCESSED),
    ), activeAdhikaras = setOf("6.4.1"), context = DerivationalContext(rupa = Rupa(linga = Linga.NAPUMSAKA)))

    @Test fun `ato am substitutes full am and retains original sup identity`() {
        for (sup in listOf(SupAffix.SU, SupAffix.AM)) {
            val initial = state("फल", sup)
            assertTrue(AtomSutra.matches(initial))
            val after = AtomSutra.apply(initial).state
            assertEquals(listOf(Svara.A, Vyanjana.MA), after.terms.last().varnas)
            assertEquals(sup, after.terms.last().sourceSupAffix)
            assertEquals(SupAffix.AM.upadesha, after.terms.last().upadesha)
            assertFalse(AtomSutra.matches(after))
            assertFalse(SvamorNapumsakatSutra.matches(after))
        }
    }

    @Test fun `neuter singular luk archives complete suffix without a manufactured ending`() {
        val initial = state("मनस्", SupAffix.AM)
        assertTrue(SvamorNapumsakatSutra.matches(initial))
        val after = SvamorNapumsakatSutra.apply(initial).state
        assertEquals(initial.terms.first(), after.terms.single())
        assertEquals(SupAffix.AM, after.droppedTerms.single().sourceSupAffix)
        assertEquals("7.1.23", after.droppedTerms.single().droppedBySutra)
        assertFalse(SvamorNapumsakatSutra.matches(initial.copy(context = DerivationalContext(rupa = Rupa(linga = Linga.PUMS)))))
    }

    @Test fun `real singular forms use luk or full am then purvarupa`() {
        for (vibhakti in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
            for ((stem, expected) in listOf("मनस्" to "मनः", "पयस्" to "पयः", "दधि" to "दधि", "मधु" to "मधु", "फल" to "फलम्")) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA, Linga.NAPUMSAKA))
                assertEquals(expected, result.final.surface, "$stem $vibhakti")
                val trace = result.applications.map { it.sutra }
                if (stem == "फल") {
                    assertTrue(trace.indexOf("7.1.24") >= 0)
                    assertTrue(trace.indexOf("6.1.107") > trace.indexOf("7.1.24"))
                    assertFalse("7.1.23" in trace)
                } else assertTrue("7.1.23" in trace)
            }
        }
    }
}
