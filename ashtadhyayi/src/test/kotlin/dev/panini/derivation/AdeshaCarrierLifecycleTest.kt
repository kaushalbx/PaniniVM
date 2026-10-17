package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.NgitScaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.ashtadhyayi.adhyaya4.pada1.KalyanyadinamInangSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class AdeshaCarrierLifecycleTest {
    private val lifecycle = DerivationEngine(listOf(HalantyamSutra, ChutuSutra, LasakvataddhiteSutra,
        UpadesheAjanunasikaItSutra, TasyaLopahSutra, NgitScaSutra))
    private fun original(surface: String = "कल्याणी") = DerivationState(
        listOf(DerivationTerm("stem", surface, TermKind.PRATIPADIKA, upadesha = "कल्याणी")),
        context = DerivationalContext(requestedMeaning = DerivationalMeaning.APATYA))

    @Test fun `inang remains raw until independent it designation lopa and targeting`() {
        val initial = original()
        val prescribed = KalyanyadinamInangSutra.apply(initial).state
        val raw = prescribed.terms.single { it.kind == TermKind.ADESHA }
        assertEquals("इनङ्", raw.surface)
        assertEquals("इनङ्", raw.upadesha)
        assertEquals("4.1.126", raw.createdBySutra)
        assertEquals("stem", raw.adeshaTarget?.termId)
        assertEquals(initial.terms.single().varnas, raw.adeshaTarget?.originalVarnas)
        assertEquals(AdeshaPlacement.NGIT_FINAL, raw.adeshaTarget?.placement)
        assertEquals(initial.terms.single(), prescribed.terms.first())
        assertTrue(raw.itProcessingPending)
        assertTrue(raw.itMarkerProvenance.isEmpty())
        assertFalse(NgitScaSutra.matches(prescribed))
        assertFailsWith<IllegalArgumentException> { prescribed.requireCompleteItProcessing() }

        val result = lifecycle.derive(prescribed)
        val stem = result.final.terms.first()
        assertEquals("कल्याणिन", stem.surface)
        assertEquals(initial.terms.single().upadesha, stem.upadesha)
        assertEquals("stem", stem.id)
        assertTrue(stem.itMarkerProvenance.isEmpty())
        val origin = stem.adeshaOrigins.single()
        assertEquals(raw.id, origin.carrierId)
        assertEquals("इनङ्", origin.upadesha)
        assertEquals("4.1.126", origin.prescribingSutra)
        assertEquals("1.1.53", origin.targetingSutra)
        assertEquals(initial.terms.single().varnas.lastIndex, origin.sourceVarnaIndex)
        assertTrue(origin.itMarkerProvenance.any { it.marker == ItMarker.NGIT && it.designationSutra == "1.3.3" && it.designatedText == "ङ्" })
        val archived = result.final.droppedTerms.single { it.id == raw.id }
        assertEquals("इनङ्", archived.upadesha)
        assertEquals("इन", archived.originalSurfaceBeforeDrop)
        assertEquals("stem", archived.mergedIntoTermId)
        assertEquals(origin.itMarkerProvenance, archived.itMarkerProvenance)
        assertEquals("4.1.126", archived.droppedBySutra)
        assertTrue(result.applications.map { it.sutra }.containsAll(listOf("1.3.3", "1.3.9", "1.1.53")))
        assertTrue(result.applications.indexOfFirst { it.sutra == "1.3.9" } < result.applications.indexOfFirst { it.sutra == "1.1.53" })
        assertEquals("4.1.126", result.final.substitutions.single().sutra)
        result.final.requireCompleteItProcessing()
        assertFalse(KalyanyadinamInangSutra.matches(result.final))
    }

    @Test fun `target nasalization and signs survive carrier integration`() {
        val source = original("कल्याणीँऽ")
        val result = lifecycle.derive(KalyanyadinamInangSutra.apply(source).state).final
        val stem = result.terms.first()
        assertTrue(stem.phonologicalText.effectiveVarnas[stem.adeshaOrigins.single().sourceVarnaIndex].nasalized)
        assertTrue(stem.orthographicSigns.any { it.sign == OrthographicSign.AVAGRAHA && it.afterVarnaCount == stem.varnas.size })
        assertEquals(source.terms.single().surface, result.substitutions.single().originalSurface)
    }

    @Test fun `unintegrated processed carriers are invalid and stale targets are rejected`() {
        val raw = original().introduceRawAdesha("stem", listOf(Svara.I, Vyanjana.NA, Svara.A, Vyanjana.NGA),
            AdeshaPlacement.NGIT_FINAL, "4.1.126", "carrier")
        val processed = TasyaLopahSutra.apply(HalantyamSutra.apply(raw).state).state
        assertTrue(NgitScaSutra.matches(processed))
        assertFailsWith<IllegalArgumentException> { processed.requireCompleteItProcessing() }
        val stale = processed.replaceTerm("stem", processed.terms.first().copy(surface = "सुभगा"))
        assertFailsWith<IllegalArgumentException> { NgitScaSutra.apply(stale) }
        assertEquals("सुभगा", stale.terms.first().surface)
        assertEquals("इन", stale.terms.last().surface)
    }

    @Test fun `ordinary ngit affixes and marker flags do not masquerade as substitute proofs`() {
        val ordinary = DerivationTerm("tatang", "तात्", TermKind.PRATYAYA, upadesha = "तातङ्",
            itMarkerProvenance = setOf(ItMarkerProvenance(ItMarker.NGIT, "1.3.3", "ङ्")))
        assertFalse(NgitScaSutra.matches(DerivationState(listOf(ordinary))))
        val unproven = original().introduceRawAdesha("stem", listOf(Svara.I, Vyanjana.NA, Svara.A, Vyanjana.NGA),
            AdeshaPlacement.NGIT_FINAL, "4.1.126", "carrier")
        val flagsOnly = unproven.replaceTerm("carrier", unproven.terms.last().copy(surface = "इन्",
            itProcessingPhase = ItProcessingPhase.PROCESSED, itMarkers = setOf(ItMarker.NGIT)))
        assertFalse(NgitScaSutra.matches(flagsOnly))
        assertFailsWith<IllegalArgumentException> {
            original().introduceRawAdesha("stem", listOf(Svara.I, Vyanjana.NA),
                AdeshaPlacement.NGIT_FINAL, "4.1.126", "carrier")
        }
    }

    @Test fun `full registry keeps introduction targeting and subsequent a lopa distinct`() {
        val result = TaddhitaEngine().derive("कल्याणी", DerivationalMeaning.APATYA)
        val prescription = result.applications.single { it.sutra == "4.1.126" }
        assertEquals("कल्याणी", prescription.after.terms.first().surface)
        assertEquals("इनङ्", prescription.after.terms.single { it.kind == TermKind.ADESHA }.upadesha)
        val targeting = result.applications.single { it.sutra == "1.1.53" }
        assertEquals("कल्याणिन", targeting.after.terms.first().surface)
        assertTrue(result.applications.any { it.sutra == "6.4.148" })
        val aLopa = result.applications.first { it.sutra == "6.4.148" }
        assertEquals(Svara.A, aLopa.before.terms.first().varnas.last())
        assertTrue(result.applications.indexOf(targeting) < result.applications.indexOf(aLopa))
        assertTrue(result.final.terms.none { it.kind == TermKind.ADESHA })
        val carrier = result.final.droppedTerms.single { it.kind == TermKind.ADESHA }
        assertEquals("इनङ्", carrier.upadesha)
        assertTrue(carrier.itMarkerProvenance.any { it.marker == ItMarker.NGIT })
        assertTrue(result.final.terms.any { term -> term.adeshaOrigins.any { it.carrierId == carrier.id } })
        assertTrue(result.applications.all { application ->
            application.after.samjnas.none { it.targetId == carrier.id && it.samjna == Samjna.PRATYAYA }
        })
        result.final.requireCompleteItProcessing()
    }

    @Test fun `active carriers integrate even in a state containing two completed padas`() {
        val base = original().copy(terms = original().terms + DerivationTerm("right", "अत्र", TermKind.PRATIPADIKA),
            samjnas = setOf(SamjnaAssignment("stem", Samjna.PADA), SamjnaAssignment("right", Samjna.PADA)))
        val raw = base.introduceRawAdesha("stem", listOf(Svara.I, Vyanjana.NA, Svara.A, Vyanjana.NGA),
            AdeshaPlacement.NGIT_FINAL, "4.1.126", "carrier")
        val result = lifecycle.derive(raw)
        assertEquals(1, result.applications.count { it.sutra == "1.1.53" })
        assertEquals("अत्र", result.final.terms.last().surface)
        assertEquals("कल्याणिन", result.final.terms.first().surface)
        assertFailsWith<IllegalArgumentException> { DerivationTerm("missing-target", "इनङ्", TermKind.ADESHA) }
        assertFailsWith<IllegalArgumentException> {
            result.final.terms.first().copy(adeshaTarget = AdeshaTarget("other", listOf(Svara.A), AdeshaPlacement.NGIT_FINAL))
        }
    }
}
