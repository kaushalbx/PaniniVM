package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada2.AdasAuSuLopahSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class AdasSuLifecycleTest {
    @Test fun `au and su lopa do not manufacture the final word`() {
        val stem = DerivationTerm("stem", "अदस्", TermKind.PRATIPADIKA)
        val suffix = SupAffix.SU.term("suffix").copy(surface = "स्", itProcessingPhase = ItProcessingPhase.PROCESSED)
        val before = DerivationState(listOf(stem, suffix), activeAdhikaras = setOf("6.4.1"), stage = DerivationStage.IT_PROCESSED)
        assertTrue(AdasAuSuLopahSutra.matches(before))
        val after = AdasAuSuLopahSutra.apply(before).state
        assertEquals(listOf(Svara.A, Vyanjana.DA, Svara.A, Svara.AU), after.terms.single().varnas)
        assertEquals(SupAffix.SU, after.droppedTerms.single().sourceSupAffix)
        assertEquals("स्", after.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals("7.2.107", after.droppedTerms.single().droppedBySutra)
        assertFalse(AdasAuSuLopahSutra.matches(after))
    }

    @Test fun `asau has independently traceable au su lopa d substitution and vrddhi`() {
        for (linga in listOf(Linga.PUMS, Linga.STRI)) {
            val result = SubantaEngine().derive(SubantaDerivationRequest("अदस्", Vibhakti.PRATHAMA, Vacana.EKAVACANA, linga))
            assertEquals("असौ", result.final.surface, result.applications.joinToString("\n") { "${it.sutra}: ${it.before.surface} -> ${it.after.surface}" })
            val trace = result.applications.map { it.sutra }
            assertTrue(trace.indexOf("7.2.107") >= 0)
            assertTrue("7.2.106" in trace)
            assertTrue("6.1.88" in trace)
            assertFalse("7.2.102" in trace)
            assertFalse("8.2.80" in trace)
        }
    }

    @Test fun `nonfinal t produces s before independently recorded satva`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("एतद्", Vibhakti.PRATHAMA, Vacana.EKAVACANA))
        assertEquals("एषः", result.final.surface, result.applications.joinToString("\n") { "${it.sutra}: ${it.after.surface}" })
        val trace = result.applications.map { it.sutra }
        assertTrue(trace.indexOf("7.2.106") >= 0)
        assertTrue(trace.indexOf("8.3.59") > trace.indexOf("7.2.106"))
        assertEquals(listOf(Vyanjana.SA), result.final.substitutions.first { it.sutra == "7.2.106" }.replacementVarnas)
    }

    @Test fun `neuter luk prevents masculine nominative substitutions`() {
        for ((stem, expected) in listOf("तद्" to "तत्", "एतद्" to "एतत्", "अदस्" to "अदः", "किम्" to "किम्")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.NAPUMSAKA))
            assertEquals(expected, result.final.surface, "$stem: ${result.applications.map { it.sutra }}")
            assertTrue(result.applications.any { it.sutra == "7.1.23" })
            assertTrue(result.applications.none { it.sutra in setOf("7.2.102", "7.2.103", "7.2.106", "7.2.107") })
        }
    }

    @Test fun `au substitution requires original current su and adas final s`() {
        val before = DerivationState(listOf(DerivationTerm("stem", "अदस्", TermKind.PRATIPADIKA),
            SupAffix.SU.term("suffix")), activeAdhikaras = setOf("6.4.1"))
        assertFalse(AdasAuSuLopahSutra.matches(before.replaceTerm("suffix", SupAffix.AU.term("suffix"))))
        assertFalse(AdasAuSuLopahSutra.matches(before.replaceTerm("stem", before.terms.first().copy(upadesha = "अन्य"))))
        assertFalse(AdasAuSuLopahSutra.matches(before.replaceTerm("stem", before.terms.first().copy(surface = "अद"))))
    }

    @Test fun `completed lexical hiatus is not reprocessed but newly exposed junction is`() {
        val result = SandhiEngine().join("अतएव", "इति")
        assertEquals("अतएवेति", result.final.surface)
        assertTrue(result.applications.none { it.sutra == "6.1.88" })
        val term = DerivationTerm("stem", "अदस्", TermKind.PRATIPADIKA, formedPadaRupa = Rupa())
        val changed = DerivationState(listOf(term), stage = DerivationStage.ANGAKARYA)
            .replaceTermVarna(term.id, term.varnas.lastIndex, listOf(Svara.AU), "test-source")
        assertTrue(dev.panini.ashtadhyayi.adhyaya6.pada1.VrddhirEciSutra.matches(changed))
        assertEquals(listOf(Svara.A, Vyanjana.DA, Svara.AU),
            dev.panini.ashtadhyayi.adhyaya6.pada1.VrddhirEciSutra.apply(changed).state.terms.single().varnas)
    }
}
