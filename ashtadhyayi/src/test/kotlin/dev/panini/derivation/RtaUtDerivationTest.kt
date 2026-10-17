package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.RtaUtSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class RtaUtDerivationTest {
    @Test fun `completed pada composition preserves ordinary plural derivation`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("देव", Vibhakti.PRATHAMA, Vacana.BAHUVACANA))
        assertEquals("देवाः", result.final.surface, result.applications.joinToString("\n") { "${it.sutra}: ${it.before.surface} -> ${it.after.surface}" })
    }
    private fun state(stem: String = "पितृ", sup: SupAffix = SupAffix.NGASI): DerivationState = DerivationState(listOf(
        DerivationTerm("stem", stem, TermKind.PRATIPADIKA),
        sup.term("suffix").copy(surface = "अस्", itProcessingPhase = ItProcessingPhase.PROCESSED),
    ))

    @Test fun `shared vowel keeps suffix identity and leaves s for real lopa`() {
        val before = state()
        val after = RtaUtSutra.apply(before).state
        assertEquals(listOf(Vyanjana.PA, Svara.I, Vyanjana.TA, Svara.U, Vyanjana.RA, Vyanjana.SA), after.terms.single().varnas)
        val consumed = after.droppedTerms.single()
        assertEquals(SupAffix.NGASI, consumed.sourceSupAffix)
        assertEquals("अस्", consumed.originalSurfaceBeforeDrop)
        assertEquals("stem", consumed.mergedIntoTermId)
        assertEquals("6.1.111", consumed.droppedBySutra)
    }

    @Test fun `composition transfers an established pada boundary but does not invent one`() {
        val before = state()
        assertFalse(RtaUtSutra.apply(before).state.samjnas.any { it.samjna == Samjna.PADA })
        val tagged = before.withSamjnas(setOf(SamjnaAssignment("suffix", Samjna.PADA)))
        val after = RtaUtSutra.apply(tagged).state
        assertTrue(after.samjnas.any { it.targetId == "stem" && it.samjna == Samjna.PADA })
        assertTrue(after.droppedTerms.any { it.id == "suffix" }) // Old assignments remain historical evidence.
    }

    @Test fun `coalescence projects grammatical accent and pada ownership to shared u`() {
        val before = state()
        val stem = before.terms.first().copy(completedPadaOrigins = List(4) { setOf("left") })
        val suffix = before.terms.last().copy(completedPadaOrigins = List(2) { setOf("right") },
            grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, "test-source")))
        val after = RtaUtSutra.apply(before.replaceTerm("stem", stem).replaceTerm("suffix", suffix)).state
        val composed = after.terms.single()
        assertEquals(setOf("left", "right"), composed.completedPadaOrigins[3])
        assertEquals(listOf(GrammaticalAccent(3, AccentType.UDATTA, "test-source")), composed.grammaticalAccents)
        assertEquals(suffix.grammaticalAccents, after.droppedTerms.single().originalGrammaticalAccentsBeforeDrop)
    }

    @Test fun `only short r before original typed ngasi or ngas initial a qualifies`() {
        assertTrue(RtaUtSutra.matches(state()))
        assertTrue(RtaUtSutra.matches(state(sup = SupAffix.NGAS)))
        assertFalse(RtaUtSutra.matches(state("पितॄ")))
        assertFalse(RtaUtSutra.matches(state("मति")))
        assertFalse(RtaUtSutra.matches(state(sup = SupAffix.AM)))
        val before = state()
        val suffix = before.terms.last()
        assertFalse(RtaUtSutra.matches(before.replaceTerm(suffix.id, suffix.copy(surface = "ए"))))
        assertFalse(RtaUtSutra.matches(before.replaceTerm(suffix.id, suffix.copy(itProcessingPhase = ItProcessingPhase.RAW_UPADESHA))))
        assertFalse(RtaUtSutra.matches(before.replaceTerm(suffix.id, suffix.copy(sourceSupAffix = null))))
    }

    @Test fun `written sign remains separate from the u r substitution`() {
        val after = RtaUtSutra.apply(state("पितृऽ")).state.terms.single()
        assertEquals(listOf(Vyanjana.PA, Svara.I, Vyanjana.TA, Svara.U, Vyanjana.RA, Vyanjana.SA), after.varnas)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 5)), after.orthographicSigns)
    }

    @Test fun `ablative and genitive derive through ut then s lopa then visarga`() {
        for ((stem, expected) in listOf("पितृ" to "पितुः", "भ्रातृ" to "भ्रातुः", "मातृ" to "मातुः", "होतृ" to "होतुः")) {
            for (vibhakti in listOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA))
                assertEquals(expected, result.final.surface)
                val trace = result.applications.map { it.sutra }
                assertTrue(trace.indexOf("6.1.111") >= 0)
                assertTrue(trace.indexOf("8.2.24") > trace.indexOf("6.1.111"))
                assertTrue(trace.indexOf("8.3.15") > trace.indexOf("8.2.24"))
                assertFalse("6.1.77" in trace)
            }
        }
    }
}
