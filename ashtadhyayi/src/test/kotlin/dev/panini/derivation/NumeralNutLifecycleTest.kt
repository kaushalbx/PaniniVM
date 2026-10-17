package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.SatCaturbhyascaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.NopadhayahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AstanAaVibhaktuSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class NumeralNutLifecycleTest {
    private fun state() = DerivationState(listOf(
        DerivationTerm("stem", "पञ्चन्", TermKind.PRATIPADIKA),
        SupAffix.AM_6.term("suffix").copy(itProcessingPhase = ItProcessingPhase.PROCESSED),
    ), activeAdhikaras = setOf("6.4.1"), samjnas = setOf(SamjnaAssignment("stem", Samjna.SHAT)))

    @Test fun `producer introduces raw targeted nut without any stem substitution`() {
        val before = state()
        assertTrue(SatCaturbhyascaSutra.matches(before))
        val after = SatCaturbhyascaSutra.apply(before).state
        assertEquals(before.terms, after.terms.take(2))
        assertTrue(after.substitutions.isEmpty())
        val augment = after.terms.last()
        assertEquals("नुँट्", augment.upadesha)
        assertEquals("suffix", augment.augmentTargetId)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
        assertFalse(SatCaturbhyascaSutra.matches(after))
        assertFalse(SatCaturbhyascaSutra.matches(before.copy(context = DerivationalContext(samasaType = SamasaType.BAHUVRIHI))))
        assertFalse(SatCaturbhyascaSutra.matches(before.replaceTerm("suffix", before.terms.last().copy(sourceSupAffix = SupAffix.AM))))
    }

    @Test fun `nami eligibility is linked to placed nut not merely written nam`() {
        val before = state()
        val suffix = before.terms.last().copy(surface = "नाम्")
        val written = before.replaceTerm("suffix", suffix)
        assertFalse(written.hasNutAugmentedGenitivePlural(suffix))
        assertFalse(NopadhayahSutra.matches(written))
        val placed = DerivationTerm("augment", "", TermKind.AGAMA, upadesha = "नुँट्",
            createdBySutra = "7.1.55", augmentTargetId = "suffix", mergedIntoTermId = "suffix", droppedBySutra = "1.1.46")
        val linked = written.copy(droppedTerms = listOf(placed))
        assertTrue(NopadhayahSutra.matches(linked))
        val after = NopadhayahSutra.apply(linked).state
        assertEquals("पञ्चान्", after.terms.first().surface)
        assertEquals(Vyanjana.NA, after.terms.first().varnas.last()) // lopa is a later rule.
        assertEquals(suffix, after.terms.last())
        assertFalse(NopadhayahSutra.matches(linked.copy(droppedTerms = listOf(placed.copy(augmentTargetId = "another")))))
        assertFalse(NopadhayahSutra.matches(linked.replaceTerm("stem", before.terms.first().copy(surface = "पञ्चन्त्"))))
    }

    @Test fun `numeral genitives use real upadha lengthening before final n lopa`() {
        for ((stem, expected) in listOf("पञ्चन्" to "पञ्चानाम्", "सप्तन्" to "सप्तानाम्", "नवन्" to "नवानाम्",
            "दशन्" to "दशानाम्")) {
            val result = SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.SASTHI, Vacana.BAHUVACANA))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            assertTrue(trace.indexOf("7.1.55") >= 0)
            assertTrue(trace.indexOf("6.4.7") > trace.indexOf("1.1.46"), "$stem: $trace")
            assertTrue(trace.indexOf("8.2.7") > trace.indexOf("6.4.7"), "$stem: $trace")
            assertTrue(trace.none { it.startsWith("7.3.") && it.substringAfterLast('.').toInt() > 120 })
            assertEquals(1, trace.count { it == "7.1.55" })
            val augment = result.final.droppedTerms.single { it.createdBySutra == "7.1.55" }
            assertEquals("नुँट्", augment.upadesha)
            assertEquals("1.1.46", augment.droppedBySutra)
        }
    }

    @Test fun `astan optional aa and retained n branches derive independently`() {
        val request = SubantaDerivationRequest("अष्टन्", Vibhakti.SASTHI, Vacana.BAHUVACANA)
        val results = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).deriveAll(request.initialState())
        assertEquals(setOf("अष्टानाम्"), results.map { it.final.surface }.toSet())
        assertTrue(results.any { result -> result.applications.any { it.sutra == "7.2.84" } })
        assertTrue(results.any { result -> result.applications.any { it.sutra == "6.4.7" } &&
            result.applications.none { it.sutra == "7.2.84" } })
        for (result in results.filter { it.applications.any { application -> application.sutra == "7.2.84" } }) {
            val trace = result.applications.map { it.sutra }
            assertTrue(trace.indexOf("6.1.101") > trace.indexOf("7.2.84"))
        }
    }

    @Test fun `astan aa substitutes final n only and internal dirgha is separate`() {
        val before = state().replaceTerm("stem", DerivationTerm("stem", "अष्टन्", TermKind.PRATIPADIKA,
            grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, "test-accent"))))
        val aa = AstanAaVibhaktuSutra.apply(before).state
        assertEquals(listOf(Svara.A, Vyanjana.SSA, Vyanjana.TTA, Svara.A, Svara.AA), aa.terms.first().varnas)
        assertEquals(before.terms.first().grammaticalAccents, aa.terms.first().grammaticalAccents)
        assertTrue(SavarnaDirghaSutra.matches(aa))
        val after = SavarnaDirghaSutra.apply(aa).state
        assertEquals("अष्टा", after.terms.first().surface)
        assertEquals(before.terms.last(), after.terms.last())
        assertEquals(before.terms.first().grammaticalAccents, after.terms.first().grammaticalAccents)
    }

    @Test fun `internal dirgha does not erase completed word hiatus`() {
        val term = DerivationTerm("carrier", "अआ", TermKind.PRATIPADIKA,
            completedPadaOrigins = listOf(setOf("first"), setOf("second")))
        val before = DerivationState(listOf(term), stage = DerivationStage.PADA_FORMED)
        assertFalse(SavarnaDirghaSutra.matches(before))
        val internal = before.replaceTerm("carrier", term.copy(completedPadaOrigins = listOf(setOf("same"), setOf("same"))))
        assertTrue(SavarnaDirghaSutra.matches(internal))
        assertEquals(listOf(Svara.AA), SavarnaDirghaSutra.apply(internal).state.terms.single().varnas)
    }
}
