package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.AtoYukCinKrtohSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class AtoYukLifecycleTest {
    private fun state(krt: Boolean = true, marker: ItMarker = ItMarker.NIT): DerivationState {
        val root = DerivationTerm("root", "दाँऽ", TermKind.DHATU, upadesha = "दा")
        val suffix = DerivationTerm("suffix", "उ", TermKind.PRATYAYA, upadesha = "उण्",
            itMarkers = setOf(marker), itProcessingPhase = ItProcessingPhase.PROCESSED)
        return DerivationState(listOf(root, suffix), activeAdhikaras = setOf("6.4.1"),
            samjnas = if (krt) setOf(SamjnaAssignment("suffix", Samjna.KRT)) else emptySet(),
            stage = DerivationStage.IT_PROCESSED)
    }

    @Test fun `raw yuk requires grammatical domain not the spelling u`() {
        for (marker in listOf(ItMarker.NIT, ItMarker.NYIT)) {
            val original = state(marker = marker)
            assertTrue(AtoYukCinKrtohSutra.matches(original))
            val after = AtoYukCinKrtohSutra.apply(original).state
            assertTrue(after.terms.containsAll(original.terms))
            val augment = after.terms.single { it.kind == TermKind.AGAMA }
            assertEquals(listOf(Vyanjana.YA, Vyanjana.KA), augment.varnas)
            assertEquals("युक्", augment.upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
            assertEquals("root", augment.augmentTargetId)
            assertEquals(NonOperativeUpadeshaFunction.UCCARANARTHA,
                augment.nonOperativeUpadeshaSegments.single().function)
            assertTrue(augment.itDesignations.isEmpty())
            assertFalse(AtoYukCinKrtohSutra.matches(after))
        }
        assertFalse(AtoYukCinKrtohSutra.matches(state(krt = false)))
        assertFalse(AtoYukCinKrtohSutra.matches(state(marker = ItMarker.KIT)))
        val original = state()
        assertFalse(AtoYukCinKrtohSutra.matches(original.copy(activeAdhikaras = emptySet())))
        assertFalse(AtoYukCinKrtohSutra.matches(original.replaceTerm("root",
            original.terms.first().copy(surface = "द"))))
        assertFalse(AtoYukCinKrtohSutra.matches(original.replaceTerm("suffix",
            original.terms.last().copy(sourceTaddhitaAffix = TaddhitaAffix.AN))))
        assertFalse(AtoYukCinKrtohSutra.matches(original.replaceTerm("suffix",
            original.terms.last().copy(upadesha = SanadiAffix.NIC.upadesha))))
        val cin = original.replaceTerm("suffix", original.terms.last().copy(upadesha = "चिण्", surface = "इ", itMarkers = emptySet()))
            .copy(samjnas = emptySet())
        assertTrue(AtoYukCinKrtohSutra.matches(cin))
    }


    @Test fun `kit placement preserves annotated root identity and exact it indices`() {
        val original = state()
        var current = AtoYukCinKrtohSutra.apply(original).state
        current = dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra.apply(current).state
        current = dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra.apply(current).state
        val designated = current.terms.first()
        assertEquals(setOf(3), designated.itDesignations.single().varnaIndices)
        current = dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra.apply(current).state
        val root = current.terms.first()
        assertEquals("दाँऽय्", root.surface)
        assertEquals(original.terms.first().id, root.id)
        assertEquals(original.terms.first().upadesha, root.upadesha)
        assertEquals(original.terms.last(), current.terms.last())
        assertEquals(listOf(Vyanjana.DA, Svara.AA, Vyanjana.YA), root.varnas)
        assertFalse(AtoYukCinKrtohSutra.matches(current))
        current.requireCompleteItProcessing()
    }
}
