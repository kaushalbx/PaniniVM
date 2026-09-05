package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya2.pada4.YanoLukasSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanadyantaDhatavahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SanyAngasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.YasyetiCaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada4.SanyAtaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.KitiCaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.TaddhitesvAcamAdehSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.MatorVahSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AffixIdentityPredicateTest {
    private val root = DerivationTerm("root", "पच्", TermKind.DHATU)

    @Test
    fun `8 2 9 replaces processed matup through explicit preservation policy`() {
        val matup = DerivationTerm(
            id = "possessive",
            surface = "मत्",
            kind = TermKind.PRATYAYA,
            upadesha = "मतुँप्",
            createdBySutra = "5.2.94",
            itProcessingPhase = ItProcessingPhase.PROCESSED,
        )
        val initial = DerivationState(listOf(DerivationTerm("stem", "धन", TermKind.PRATIPADIKA), matup))

        assertTrue(MatorVahSutra.matches(initial))
        val result = MatorVahSutra.apply(initial).state.terms.last()
        assertEquals("वत्", result.surface)
        assertEquals("मतुँप्", result.upadesha)
        assertEquals("5.2.94", result.createdBySutra)
        assertEquals(ItProcessingPhase.PROCESSED, result.itProcessingPhase)
        DerivationState(listOf(result)).requireCompleteItProcessing()
    }

    @Test
    fun `san and yan rules reject suggestive IDs without grammatical upadesha identity`() {
        val fakeSan = DerivationTerm("san_pratyaya", "स", TermKind.PRATYAYA, upadesha = "घञ्")
        val fakeYan = DerivationTerm("yan_pratyaya", "य", TermKind.PRATYAYA, upadesha = "घञ्")
        val abhyasa = DerivationTerm("abhyasa", "प", TermKind.DHATU)

        assertFalse(SanyAngasyaSutra.matches(DerivationState(listOf(root, fakeSan))))
        assertFalse(SanyAtaSutra.matches(DerivationState(listOf(abhyasa, root, fakeSan))))
        assertFalse(SanadyantaDhatavahSutra.matches(DerivationState(listOf(root, fakeSan))))
        assertFalse(
            YanoLukasSutra.matches(
                DerivationState(
                    terms = listOf(root, fakeYan),
                    samjnas = setOf(SamjnaAssignment(fakeYan.id, Samjna.YAN_LUK)),
                ),
            ),
        )
    }

    @Test
    fun `san and yan rules accept exact grammatical upadesha identity`() {
        val san = DerivationTerm("suffix", "स", TermKind.PRATYAYA, upadesha = "सन्")
        val yan = DerivationTerm("suffix", "य", TermKind.PRATYAYA, upadesha = "यङ्")
        val abhyasa = DerivationTerm("abhyasa", "प", TermKind.DHATU)

        assertTrue(SanyAngasyaSutra.matches(DerivationState(listOf(root, san))))
        assertTrue(SanyAtaSutra.matches(DerivationState(listOf(abhyasa, root, san))))
        assertTrue(SanadyantaDhatavahSutra.matches(DerivationState(listOf(root, san))))
        assertTrue(
            YanoLukasSutra.matches(
                DerivationState(
                    terms = listOf(root, yan),
                    samjnas = setOf(SamjnaAssignment(yan.id, Samjna.YAN_LUK)),
                ),
            ),
        )
    }

    @Test
    fun `taddhita rules reject suggestive IDs without grammatical identity`() {
        val stem = DerivationTerm("stem", "नर", TermKind.PRATIPADIKA)
        val fakeNit = DerivationTerm(
            "apatya_taddhita",
            "इ",
            TermKind.PRATYAYA,
            upadesha = "घञ्",
            itMarkers = setOf(ItMarker.NIT, ItMarker.KIT),
        )
        val state = DerivationState(listOf(stem, fakeNit), stage = DerivationStage.ANGAKARYA)

        assertFalse(TaddhitesvAcamAdehSutra.matches(state))
        assertFalse(KitiCaSutra.matches(state))
        assertFalse(YasyetiCaSutra.matches(state.copy(activeAdhikaras = setOf("6.4.1"))))
        assertTrue(AdGunaSutra.matches(state))
    }

    @Test
    fun `yan luk consumes lifecycle before dropping the affix`() {
        val yan = DerivationTerm(
            id = "suffix",
            surface = "य",
            kind = TermKind.PRATYAYA,
            upadesha = "यङ्",
            itProcessingPhase = ItProcessingPhase.PROCESSED,
        )
        val initial = DerivationState(
            terms = listOf(root, yan),
            samjnas = setOf(SamjnaAssignment(yan.id, Samjna.YAN_LUK)),
        )

        val result = YanoLukasSutra.apply(initial).state
        assertTrue(result.terms.none { it.id == yan.id })
        val dropped = result.droppedTerms.single { it.id == yan.id }
        assertEquals(ItProcessingPhase.PROCESSED, dropped.itProcessingPhase)
        assertTrue(dropped.itDesignations.isEmpty())
        assertTrue(dropped.deferredItDesignations.isEmpty())
        assertEquals("2.4.74", dropped.droppedBySutra)
        result.requireCompleteItProcessing()
    }
}
