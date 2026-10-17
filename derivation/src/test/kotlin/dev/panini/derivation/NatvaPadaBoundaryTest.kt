package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.RasabhyamNoNahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.AtkupvangnumvyavayePiSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.core.ItMarker
import dev.panini.core.SupAffix
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya6.pada1.VrddhirEciSutra
import dev.panini.sutra.SutraStage
import kotlin.test.*

class NatvaPadaBoundaryTest {
    @Test fun `ramas trigger does not retroflex hanuman in a distinct pada`() {
        val result = SandhiEngine().join("राम", "हनुमान्")
        assertEquals("रामहनुमान्", SandhiEngine().render(result))
        assertTrue(result.applications.none { it.sutra in setOf("8.4.1", "8.4.2") })
    }

    @Test fun `adjacent r and n across padas retain dental n`() {
        val result = SandhiEngine().join("अग्निर्", "नयति")
        assertEquals("अग्निर्नयति", SandhiEngine().render(result))
        assertTrue(result.applications.none { it.sutra in setOf("8.4.1", "8.4.2") })
    }

    @Test fun `a term boundary within one pada does not block natva`() {
        val state = DerivationState(listOf(
            DerivationTerm("stem", "राम", TermKind.PRATIPADIKA),
            DerivationTerm("suffix", "नाम्", TermKind.PRATYAYA)),
            samjnas = setOf(SamjnaAssignment("suffix", Samjna.PADA)))
        assertTrue(AtkupvangnumvyavayePiSutra.matches(state))
        assertEquals("णाम्", AtkupvangnumvyavayePiSutra.apply(state).state.terms.last().surface)
    }

    @Test fun `pada final n remains protected before a following word`() {
        val result = SandhiEngine().join("प्रान्", "अपि")
        assertEquals("प्रानपि", SandhiEngine().render(result))
        assertTrue(result.applications.none { it.sutra == "8.4.2" })
    }

    @Test fun `soundless pada endpoint still separates the grammatical words`() {
        val state = DerivationState(listOf(
            DerivationTerm("left", "राम", TermKind.PRATIPADIKA),
            DerivationTerm("zero", "", TermKind.PRATYAYA, formedPadaRupa = Rupa()),
            DerivationTerm("right", "हनुमान्", TermKind.PRATIPADIKA)),
            samjnas = setOf(SamjnaAssignment("zero", Samjna.PADA)))
        assertFalse(AtkupvangnumvyavayePiSutra.matches(state))
        assertFalse(RasabhyamNoNahSutra.matches(state))
    }

    @Test fun `external dirgha does not make two original padas one natva domain`() {
        for ((left, right, expected) in listOf(Triple("निगृह्य", "आनय", "निगृह्यानय"),
            Triple("राम", "अनय", "रामानय"))) {
            val result = SandhiEngine().join(left, right)
            assertEquals(expected, SandhiEngine().render(result))
            assertTrue(result.applications.any { it.sutra == "6.1.101" })
            assertTrue(result.applications.none { it.sutra in setOf("8.4.1", "8.4.2") })
            val term = result.final.terms.single()
            assertEquals(term.varnas.size, term.completedPadaOrigins.size)
            assertTrue(term.completedPadaOrigins.any { it == setOf("sandhi_left", "sandhi_right") })
            assertTrue(term.completedPadaOrigins.any { it == setOf("sandhi_right") })
            val dropped = result.final.droppedTerms.single()
            assertTrue(dropped.completedPadaOrigins.isEmpty())
            assertEquals(right.toVarnas().size, dropped.originalCompletedPadaOriginsBeforeDrop.size)
        }
    }

    @Test fun `guna and vrddhi preserve separate original natva domains`() {
        for ((right, expected, sutra) in listOf(Triple("इनम्", "शूरेनम्", "6.1.87"),
            Triple("एनम्", "शूरैनम्", "6.1.88"))) {
            val result = SandhiEngine().join("शूर", right)
            assertEquals(expected, SandhiEngine().render(result))
            assertTrue(result.applications.any { it.sutra == sutra })
            assertTrue(result.applications.none { it.sutra in setOf("8.4.1", "8.4.2") })
        }
    }

    @Test fun `both varnas in vocalic r guna carry coalesced ownership`() {
        val result = SandhiEngine().join("शूर", "ऋणम्")
        val term = result.final.terms.single()
        val gradeR = term.varnas.indexOfLast { it == Vyanjana.RA }
        assertEquals(Svara.A, term.varnas[gradeR - 1])
        assertEquals(setOf("sandhi_left", "sandhi_right"), term.padaOriginsAt(gradeR - 1))
        assertEquals(setOf("sandhi_left", "sandhi_right"), term.padaOriginsAt(gradeR))
        val nasal = term.varnas.indexOf(Vyanjana.NNA)
        assertTrue(result.final.areInSameCompletedPada(term, gradeR, term, nasal))
        assertFalse(result.final.areInSameCompletedPada(term, gradeR - 2, term, nasal))
    }

    @Test fun `original domains survive completed word handoff across three inputs`() {
        val outcome = SandhiSequenceEvaluator().evaluate(listOf("शूर", "अनु", "आनय")).single()
        assertEquals("शूरान्वानय", outcome.surface)
        assertTrue(outcome.steps.flatMap { it.applications }.none { it.sutra in setOf("8.4.1", "8.4.2") })
        assertTrue(outcome.padas.flatMap { it.term.completedPadaOrigins }.any { "corpus_pada_1" in it })
    }

    @Test fun `UoH row 85 expected alternative survives both boundaries`() {
        val outcomes = SandhiSequenceEvaluator().evaluate(listOf("शीघ्रम्", "निगृह्य", "आनय"), alternatives = true)
        assertTrue(outcomes.any { it.surface == "शीघ्रंनिगृह्यानय" }, outcomes.joinToString { it.surface })
        assertTrue(outcomes.flatMap { it.steps }.flatMap { it.applications }.none { it.sutra == "8.4.2" })
    }

    @Test fun `processed historical it marker does not delete a pronounced vrddhi remainder`() {
        val engine = SandhiEngine()
        val left = SandhiEngine.Pada(DerivationTerm("left", "अ", TermKind.PRATIPADIKA))
        val right = SandhiEngine.Pada(DerivationTerm("right", "ओम्", TermKind.PRATIPADIKA,
            itMarkers = setOf(ItMarker.KIT)))
        val result = engine.join(left, right)
        assertEquals("औम्", engine.render(result))
        assertEquals(Vyanjana.MA, result.final.terms.single().varnas.last())
        assertEquals(setOf("right"), result.final.terms.single().completedPadaOrigins.last())
    }

    @Test fun `transferred deferred it receives lopa before consonant phonology without reopening nasal vowels`() {
        val raw = SupAffix.AUT.term("affix").copy(itProcessingPhase = ItProcessingPhase.PROCESSED)
        val affix = raw.copy(deferredItDesignations = listOf(raw.designateVarnaIt(1, ItMarker.T, "1.3.3")))
        val before = DerivationState(listOf(DerivationTerm("stem", "अँ", TermKind.PRATIPADIKA), affix),
            stage = DerivationStage.ANGAKARYA)
        val after = VrddhirEciSutra.apply(before).state
        assertEquals(ItProcessingPhase.DESIGNATED, after.terms.single().itProcessingPhase)
        assertEquals(emptySet(), after.terms.single().itUpadeshaVarnaIndices)
        val result = DerivationEngine(Ashtadhyayi.executableSutras.filter { it.stage == SutraStage.IT_PROCESSING }).derive(after)
        assertEquals("औँ", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "1.3.9" })
        assertTrue(result.applications.none { it.sutra == "1.3.2" })
    }
}
