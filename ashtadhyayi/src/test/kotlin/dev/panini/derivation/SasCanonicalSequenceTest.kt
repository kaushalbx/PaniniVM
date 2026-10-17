package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya6.pada1.PrathamayohPurvaSavarnahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.TasmacChasoNahPumsiSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class SasCanonicalSequenceTest {
    private fun state(vowel: Svara, linga: Linga) = DerivationState(listOf(
        DerivationTerm("stem", listOf(Vyanjana.KA, vowel).toDevanagari(), TermKind.PRATIPADIKA),
        SupAffix.SAS.term("suffix").copy(surface = "अस्", itProcessingPhase = ItProcessingPhase.PROCESSED)),
        stage = DerivationStage.PADA_FORMED,
        context = DerivationalContext(rupa = Rupa(linga = linga, vibhakti = Vibhakti.DVITIYA,
            vacana = Vacana.BAHUVACANA)))

    @Test fun `all ik vowel lengths coalesce before typed sas with gender dependent na`() {
        for (vowel in listOf(Svara.I, Svara.II, Svara.U, Svara.UU, Svara.R, Svara.RR, Svara.L, Svara.LL)) {
            for (linga in listOf(Linga.PUMS, Linga.STRI)) {
                val before = state(vowel, linga)
                assertTrue(PrathamayohPurvaSavarnahSutra.matches(before))
                val formed = PrathamayohPurvaSavarnahSutra.apply(before).state
                assertEquals(listOf(Vyanjana.KA, vowel.toDirgha(), Vyanjana.SA), formed.terms.single().varnas)
                assertEquals(linga == Linga.PUMS, TasmacChasoNahPumsiSutra.matches(formed))
                val suffix = formed.droppedTerms.single()
                assertEquals(SupAffix.SAS, suffix.sourceSupAffix)
                assertEquals("6.1.102", suffix.droppedBySutra)
                assertEquals("अस्", suffix.originalSurfaceBeforeDrop)
                assertEquals("stem", suffix.mergedIntoTermId)
                if (linga == Linga.PUMS) assertEquals(listOf(Vyanjana.KA, vowel.toDirgha(), Vyanjana.NA),
                    TasmacChasoNahPumsiSutra.apply(formed).state.terms.single().varnas)
            }
        }
    }

    @Test fun `full feminine paradigms use purvasavarna then rutva visarga not a synthetic ending`() {
        val engine = SubantaEngine()
        for ((stem, expected) in listOf("मति" to "मतीः", "धेनु" to "धेनूः", "नदी" to "नदीः", "वधू" to "वधूः")) {
            val result = engine.derive(SubantaDerivationRequest(stem, Vibhakti.DVITIYA, Vacana.BAHUVACANA, Linga.STRI))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            assertTrue(trace.containsAll(listOf("6.1.102", "8.2.66", "8.3.15")))
            assertFalse("6.1.103" in trace)
            assertFalse("7.3.126" in trace)
        }
    }

    @Test fun `full r stems use canonical long r followed by sas na`() {
        val engine = SubantaEngine()
        for ((stem, expected) in listOf("पितृ" to "पितॄन्", "कर्तृ" to "कर्तॄन्")) {
            val result = engine.derive(SubantaDerivationRequest(stem, Vibhakti.DVITIYA, Vacana.BAHUVACANA, Linga.PUMS))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            assertTrue("6.1.102" in trace)
            assertTrue("6.1.103" in trace)
            assertFalse("7.3.134" in trace)
        }
        assertTrue(Ashtadhyayi.executableSutras.none { it.sutra in setOf("7.3.124", "7.3.126", "7.3.134") })
    }

    @Test fun `long ik dual and jas are not swept into the sas exception`() {
        for (sup in listOf(SupAffix.AU, SupAffix.AUT, SupAffix.JAS)) {
            val before = state(Svara.II, Linga.STRI)
            val suffix = sup.term("suffix").copy(surface = if (sup == SupAffix.JAS) "अस्" else "औ")
            assertFalse(PrathamayohPurvaSavarnahSutra.matches(before.replaceTerm("suffix", suffix)))
        }
    }

    @Test fun `exact coalescence keeps nasal vowels glyph qualifiers and grammatical accent`() {
        val initial = state(Svara.R, Linga.PUMS)
        val stem = initial.terms.first().copy(surface = "कृँऽ",
            grammaticalAccents = listOf(GrammaticalAccent(1, AccentType.UDATTA, "fixture")))
        val suffix = initial.terms.last().copy(surface = "अस्\u200D")
        val formed = PrathamayohPurvaSavarnahSutra.apply(initial.copy(terms = listOf(stem, suffix))).state
        assertEquals("कॄँऽस्\u200D", formed.surface)
        assertEquals(stem.grammaticalAccents, formed.terms.single().grammaticalAccents)
        assertEquals(suffix.surface, formed.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals("कॄँऽन्\u200D", TasmacChasoNahPumsiSutra.apply(formed).state.surface)
    }
}
