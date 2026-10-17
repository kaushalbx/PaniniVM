package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AutoAmSasoSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.PrathamayohPurvaSavarnahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.TasmacChasoNahPumsiSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class OStemCoalescenceTest {
    private fun state(stem: String, sup: SupAffix, suffix: String = if (sup == SupAffix.AM) "अम्" else "अस्") =
        DerivationState(listOf(DerivationTerm("stem", stem, TermKind.PRATIPADIKA, createdBySutra = "fixture"),
            sup.term("suffix").copy(surface = suffix, itProcessingPhase = ItProcessingPhase.PROCESSED)),
            stage = DerivationStage.PADA_FORMED,
            context = DerivationalContext(rupa = Rupa(linga = Linga.PUMS, vibhakti = sup.vibhakti, vacana = sup.vacana)))

    @Test fun `o coalescence retains arbitrary stem material and affix consonants`() {
        for ((stem, base) in listOf("गो" to "गा", "द्यो" to "द्या", "सुगो" to "सुगा")) {
            for (sup in listOf(SupAffix.AM, SupAffix.SAS)) {
                val before = state(stem, sup)
                assertTrue(AutoAmSasoSutra.matches(before))
                val after = AutoAmSasoSutra.apply(before).state
                assertEquals((base + if (sup == SupAffix.AM) "म्" else "स्").toVarnas(), after.terms.single().varnas)
                assertEquals("stem", after.terms.single().id)
                assertEquals("fixture", after.terms.single().createdBySutra)
                assertEquals(before.terms.first().varnas.lastIndex, after.substitutions.first().sourceVarnaIndex)
                val dropped = after.droppedTerms.single()
                assertEquals(before.terms.last().surface, dropped.originalSurfaceBeforeDrop)
                assertEquals(sup, dropped.sourceSupAffix)
                assertEquals("stem", dropped.mergedIntoTermId)
                assertEquals(0, dropped.mergedAffixVowelFromEnd)
                assertFalse(TasmacChasoNahPumsiSutra.matches(after))
            }
        }
    }

    @Test fun `coalescence preserves vowel annotations written signs and contributor ownership`() {
        val before = state("द्योँऽ", SupAffix.AM, "अम्\u200D").let { initial ->
            initial.copy(terms = initial.terms.map { term ->
                term.copy(completedPadaOrigins = List(term.varnas.size) { setOf(term.id) })
            })
        }
        val after = AutoAmSasoSutra.apply(before).state
        assertEquals("द्याँऽम्\u200D", after.surface)
        val term = after.terms.single()
        assertEquals(setOf("stem", "suffix"), term.padaOriginsAt(2))
        assertEquals(setOf("suffix"), term.padaOriginsAt(3))
        assertEquals(setOf("stem"), term.padaOriginsAt(0))
        assertTrue(term.phonologicalText.effectiveVarnas[2].nasalized)
        assertEquals(before.terms.last().surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
    }

    @Test fun `source vowel and current sup identity are required`() {
        assertFalse(AutoAmSasoSutra.matches(state("गौ", SupAffix.AM)))
        assertFalse(AutoAmSasoSutra.matches(state("गो", SupAffix.JAS)))
        val before = state("गो", SupAffix.AM)
        val suffix = before.terms.last()
        assertFalse(AutoAmSasoSutra.matches(before.replaceTerm("suffix", suffix.copy(kind = TermKind.DHATU))))
        // Homonymous laṅ-am is not sup-am: the śas association restricts the domain.
        assertFalse(AutoAmSasoSutra.matches(before.replaceTerm("suffix", suffix.copy(
            sourceSupAffix = null, sourceTingAffix = TingAffix.MIP))))
        assertFalse(AutoAmSasoSutra.matches(before.replaceTerm("suffix", suffix.copy(upadesha = "आदेश",
            sthaniProps = SthaniProperties(SupAffix.AM.upadesha, emptySet())))))
    }

    @Test fun `sha to na requires actual purvasavarna formation and sas source`() {
        val formed = PrathamayohPurvaSavarnahSutra.apply(state("राम", SupAffix.SAS)).state
        assertTrue(TasmacChasoNahPumsiSutra.matches(formed))
        assertEquals("रामान्", TasmacChasoNahPumsiSutra.apply(formed).state.surface)
        assertFalse(TasmacChasoNahPumsiSutra.matches(formed.copy(droppedTerms = emptyList())))
        assertFalse(TasmacChasoNahPumsiSutra.matches(formed.copy(droppedTerms = formed.droppedTerms.map {
            it.copy(sourceSupAffix = SupAffix.JAS)
        })))
    }

    @Test fun `full engine derives both Kashika dyo examples without go specific replacement`() {
        val engine = SubantaEngine()
        for ((vacana, expected) in listOf(Vacana.EKAVACANA to "द्याम्", Vacana.BAHUVACANA to "द्याः")) {
            val result = engine.derive(SubantaDerivationRequest("द्यो", Vibhakti.DVITIYA, vacana, Linga.PUMS))
            assertEquals(expected, result.final.surface)
            assertTrue(result.applications.any { it.sutra == "6.1.93" })
            assertTrue(result.applications.none { it.sutra == "6.1.103" })
        }
    }

    @Test fun `short ik accusative plurals use authentic purvasavarna and sha na sequence`() {
        val engine = SubantaEngine()
        for ((stem, expected) in listOf("अग्नि" to "अग्नीन्", "वायु" to "वायून्")) {
            val result = engine.derive(SubantaDerivationRequest(stem, Vibhakti.DVITIYA,
                Vacana.BAHUVACANA, Linga.PUMS))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            assertTrue("6.1.102" in trace)
            assertTrue("6.1.103" in trace)
            assertTrue(trace.indexOf("6.1.102") < trace.indexOf("6.1.103"))
            assertFalse("7.3.124" in trace)
        }
    }
}
