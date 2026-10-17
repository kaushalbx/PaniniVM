package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada4.NadiSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AnNadyahSutra
import dev.panini.core.*
import dev.panini.shiksha.*
import kotlin.test.*

class NadiAugmentLifecycleTest {
    private fun state(sup: SupAffix = SupAffix.NGE): DerivationState {
        val stem = DerivationTerm("stem", "नदी", TermKind.PRATIPADIKA)
        val suffix = sup.term("suffix").copy(surface = "ए", itMarkers = setOf(ItMarker.NGIT),
            itProcessingPhase = ItProcessingPhase.PROCESSED)
        return DerivationState(listOf(stem, suffix), activeAdhikaras = setOf("6.4.1"),
            samjnas = setOf(SamjnaAssignment(stem.id, Samjna.NADI)), stage = DerivationStage.IT_PROCESSED,
            context = DerivationalContext(rupa = Rupa(linga = Linga.STRI, vibhakti = sup.vibhakti, vacana = sup.vacana)))
    }

    @Test fun `producer introduces raw target linked aat only with nadi and ngit evidence`() {
        val before = state()
        assertTrue(AnNadyahSutra.matches(before))
        val after = AnNadyahSutra.apply(before).state
        val augment = after.terms[1]
        assertEquals("आट्", augment.upadesha)
        assertEquals(listOf(Svara.AA, Vyanjana.TTA), augment.varnas)
        assertEquals("suffix", augment.augmentTargetId)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
        assertTrue(augment.itDesignations.isEmpty())
        assertFalse(augment.mergeIntoAugmentTarget)
        assertTrue(augment.grammaticalAccents.isEmpty()) // Do not invent the past augment's prescribed udātta.
        assertFalse(AnNadyahSutra.matches(after))
        assertFalse(AnNadyahSutra.matches(before.copy(samjnas = emptySet())))
        assertFalse(AnNadyahSutra.matches(before.replaceTerm("suffix", before.terms.last().copy(itMarkers = emptySet()))))
        assertFalse(AnNadyahSutra.matches(state(SupAffix.NGI)))
    }

    @Test fun `nadi ngit forms record real it placement vrddhi and yan sequence`() {
        val engine = SubantaEngine()
        for ((vibhakti, expected) in listOf(Vibhakti.CHATURTHI to "नद्यै", Vibhakti.PANCHAMI to "नद्याः",
            Vibhakti.SASTHI to "नद्याः", Vibhakti.SAPTAMI to "नद्याम्")) {
            val result = engine.derive(SubantaDerivationRequest("नदी", vibhakti, Vacana.EKAVACANA, Linga.STRI))
            assertEquals(expected, result.final.surface)
            val trace = result.applications.map { it.sutra }
            val introduced = trace.indexOf("7.3.112")
            val coalesced = trace.indexOf("6.1.90")
            assertTrue(introduced >= 0 && coalesced > introduced)
            assertTrue(trace.subList(introduced + 1, coalesced).containsAll(listOf("1.3.3", "1.1.46", "1.3.9")))
            assertTrue(trace.indexOf("6.1.77") > coalesced)
            if (vibhakti == Vibhakti.SAPTAMI) assertTrue(trace.indexOf("7.3.116") < introduced)
            val augment = result.final.droppedTerms.single { it.createdBySutra == "7.3.112" }
            assertEquals("आट्", augment.upadesha)
            assertEquals("6.1.90", augment.droppedBySutra)
            assertTrue(augment.itMarkerProvenance.any { it.designatedText == "ट्" && it.designationSutra == "1.3.3" })
            assertEquals(1, trace.count { it == "7.3.112" })
        }
    }

    @Test fun `nadi genitive plural comes from nut not a word replacement`() {
        val result = SubantaEngine().derive(SubantaDerivationRequest("नदी", Vibhakti.SASTHI,
            Vacana.BAHUVACANA, Linga.STRI))
        assertEquals("नदीनाम्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "7.1.54" })
        assertTrue(result.applications.none { it.sutra in setOf("7.3.122", "7.3.112") })
        val nut = result.final.droppedTerms.single { it.createdBySutra == "7.1.54" }
        assertEquals("नुँट्", nut.upadesha)
    }

    @Test fun `nadi designation requires feminine interpretation not just long vowel shape`() {
        val before = state().copy(samjnas = emptySet())
        assertTrue(NadiSutra.matches(before))
        assertFalse(NadiSutra.matches(before.copy(context = before.context.copy(rupa = before.context.rupa.copy(linga = Linga.PUMS)))))
        assertFalse(NadiSutra.matches(before.copy(context = DerivationalContext())))
    }
}
