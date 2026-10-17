package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya7.pada1.NalUttamoVaSutra
import dev.panini.core.*
import kotlin.test.*

class NalMarkerEffectTest {
    private fun perfect() = TingantaEngine().derive(
        TingantaDerivationRequest("णीञ्", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LIT))

    @Test fun `first person raw nal and both nit work choices preserve actual designation`() {
        val result = perfect()
        assertEquals("निनय", result.final.surface)
        val raw = result.applications.single { it.sutra == "3.4.82" }.after.terms.last()
        assertEquals("णल्", raw.upadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)
        val decision = result.applications.single { it.sutra == "7.1.91" }
        val original = decision.before.terms.last()
        val changes = NalUttamoVaSutra.applyAll(decision.before)
        assertEquals(listOf(false, true), changes.map { it.state.terms.last().hasEffectiveMarker(ItMarker.NIT) })
        for (change in changes) {
            val suffix = change.state.terms.last()
            assertEquals(original.surface, suffix.surface)
            assertEquals(original.itMarkers, suffix.itMarkers)
            assertEquals(original.itMarkerProvenance, suffix.itMarkerProvenance)
            assertEquals(original.itDesignations, suffix.itDesignations)
            assertTrue(ItMarker.NIT in suffix.itMarkers ||
                (suffix.itDesignations + suffix.deferredItDesignations).any { it.marker == ItMarker.NIT } ||
                suffix.itMarkerProvenance.any { it.marker == ItMarker.NIT })
            assertFalse(NalUttamoVaSutra.matches(change.state))
        }
        val endings = result.final.allEffectiveTerms.filter { it.sourceTingAffix == TingAffix.MIP && it.upadesha == "णल्" }
        assertTrue(endings.isNotEmpty())
        assertTrue(endings.all { it.itMarkerProvenance.any { proof -> proof.marker == ItMarker.NIT } })
        assertTrue(endings.all { !it.hasEffectiveMarker(ItMarker.NIT) })
        assertFalse(NalUttamoVaSutra.matches(decision.before.copy(terms = decision.before.terms.dropLast(1) +
            original.copy(sourceTingAffix = TingAffix.TIP))))
        assertFalse(NalUttamoVaSutra.matches(decision.before.copy(terms = decision.before.terms.dropLast(1) +
            original.copy(itMarkers = setOf(ItMarker.NIT), itMarkerProvenance = emptySet(),
                itDesignations = emptyList(), deferredItDesignations = emptyList()))))
        assertTrue(Ashtadhyayi.executableSutras.any { it.sutra == "7.1.91" })
    }

    @Test fun `both prescribed marker effects produce independently continued perfect derivations`() {
        val before = perfect().applications.single { it.sutra == "7.1.91" }.before
        val results = NalUttamoVaSutra.applyAll(before).map {
            DerivationEngine(Ashtadhyayi.executableSutras).derive(it.state)
        }
        assertEquals(setOf("निनय", "निनाय"), results.map { it.final.surface }.toSet())
        val enumerated = DerivationEngine(Ashtadhyayi.executableSutras).deriveAll(before)
        assertEquals(setOf("निनय", "निनाय"), enumerated.map { it.final.surface }.toSet())
        assertTrue(enumerated.all { it.applications.any { application -> application.sutra == "7.1.91" } })
        assertTrue(results.all { it.final.stage == DerivationStage.FINAL })
        results.forEach { it.final.requireCompleteItProcessing() }
    }

    @Test fun `fresh upadesha clears old marker work decisions without fabricating a new marker`() {
        val before = perfect().applications.single { it.sutra == "7.1.91" }.before
        val suffix = NalUttamoVaSutra.apply(before).state.terms.last()
        val fresh = suffix.replaceWholeAffix("णल्", "णल्", "test-fresh", WholeAffixDesignationPolicy.FreshUpadesha)
        assertTrue(fresh.itMarkerEffects.isEmpty())
        assertTrue(fresh.itMarkerProvenance.isEmpty())
        assertFalse(fresh.hasEffectiveMarker(ItMarker.NIT))
        assertEquals(ItProcessingPhase.RAW_UPADESHA, fresh.itProcessingPhase)
    }

    @Test fun `kri first singular matches both independently attested kasika examples`() {
        val dhatu = dev.panini.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुकृञ्" }
        val initial = TingantaDerivationRequest("कृ", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LIT).initialState(dhatu)
        val results = DerivationEngine(Ashtadhyayi.executableSutras).deriveAll(initial)
        assertEquals(setOf("चकर", "चकार"), results.map { it.final.surface }.toSet())
        assertTrue(results.all { it.applications.any { application -> application.sutra == "7.1.91" } })
    }

    @Test fun `nit work consumers preserve annotations with exact occurrence substitutions`() {
        val before = perfect().applications.single { it.sutra == "7.1.91" }.before
        val ending = NalUttamoVaSutra.applyAll(before).last().state.terms.last()
        data class Case(val word: String, val expected: String, val rule: DerivationSutra, val index: Int)
        for ((word, expected, sutra, index) in listOf(
            Case("पँनिऽ", "पँनैऽ", dev.panini.ashtadhyayi.adhyaya7.pada2.AcoNnitiSutra, 3),
            Case("पँतँद्ऽ", "पँताँद्ऽ", dev.panini.ashtadhyayi.adhyaya7.pada2.AtaUpadhayahSutra, 3))) {
            val state = DerivationState(listOf(DerivationTerm("root", word, TermKind.DHATU), ending),
                activeAdhikaras = setOf("6.4.1"))
            assertTrue(sutra.matches(state))
            val changed = sutra.apply(state).state
            assertEquals(expected, changed.terms.first().surface)
            assertEquals(index, changed.substitutions.single().sourceVarnaIndex)
            assertEquals(ending, changed.terms.last())
        }
    }
}
