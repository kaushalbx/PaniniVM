package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya8.pada3.NashChavyAprashanSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.HaliSarveshamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.*
import dev.panini.core.ItMarker
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import kotlin.test.*

class ChaviRutvaSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `chav and am have their exact phonological domains`() {
        assertEquals(setOf(Vyanjana.CHA, Vyanjana.TTHA, Vyanjana.THA, Vyanjana.CA, Vyanjana.TTA, Vyanjana.TA),
            Vyanjana.entries.filter { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.CHAV, it) }.toSet())
        assertTrue(Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AM, Svara.A))
        assertFalse(Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AM, Vyanjana.SA))
    }

    @Test fun `nasal vowel and anusvara are separate ru branches`() {
        for ((left, right, expected) in listOf(
            Triple("हनुमान्", "च", setOf("हनुमाँश्च", "हनुमांश्च")),
            Triple("विद्वान्", "तु", setOf("विद्वाँस्तु", "विद्वांस्तु")),
            Triple("भवान्", "छादयति", setOf("भवाँश्छादयति", "भवांश्छादयति")))) {
            val results = engine.joinAll(left, right)
            assertEquals(expected, results.map(engine::render).toSet(), results.joinToString("; ") { r ->
                r.applications.joinToString(" -> ") { "${it.sutra}:${engine.render(it.after)}" }
            })
            for (result in results) {
                val trace = result.applications.map { it.sutra }
                assertTrue("8.3.7" in trace && "1.3.2" in trace && "1.3.9" in trace)
                assertTrue("8.3.15" in trace && "8.3.34" in trace)
                assertEquals(1, trace.count { it == "8.3.7" })
                assertTrue(result.final.terms.first().itMarkerProvenance.any { it.designationSutra == "1.3.2" })
                assertNull(result.final.terms.first().pendingRutvaUpadeshaIndex)
            }
        }
    }

    @Test fun `prashan non-chav and non-am follower block the rule`() {
        for ((left, right) in listOf("प्रशान्" to "चिनोति", "भवान्" to "करोति", "भवान्" to "त्सरुकः")) {
            assertTrue(engine.joinAll(left, right).none { r -> r.applications.any { it.sutra == "8.3.7" } })
        }
        assertFalse(NashChavyAprashanSutra.matches(DerivationState(listOf(
            DerivationTerm("stem", "भवान्", TermKind.PRATIPADIKA),
            DerivationTerm("suffix", "च", TermKind.PRATYAYA)))))
    }

    @Test fun `fresh ru does not delete existing nasal vowels or their provenance`() {
        val result = engine.join("अँसँस्", "च")
        val word = result.final.terms.first()
        assertEquals(2, word.phonologicalText.effectiveVarnas.count { it.nasalized })
        assertEquals("अँसँस्", word.upadesha)
        assertEquals(1, word.itMarkerProvenance.count { it.designationSutra == "1.3.2" })
        val designation = result.applications.single { it.sutra == "1.3.2" }.after.terms.first().itDesignations
        assertEquals(1, designation.size)
        assertEquals(setOf(4), designation.single().varnaIndices)
    }

    @Test fun `fresh ru does not reactivate old initial markers and retains metadata`() {
        val itEngine = DerivationEngine(listOf(UpadesheAjanunasikaItSutra, HalantyamSutra,
            AdirNitudavahSutra, ShahPratyayasyaSutra, ChutuSutra, LasakvataddhiteSutra, TasyaLopahSutra))
        for ((surface, kind) in listOf("ञिँस्" to TermKind.DHATU, "कँस्" to TermKind.PRATYAYA,
            "षँस्" to TermKind.PRATYAYA, "चँस्" to TermKind.PRATYAYA)) {
            val history = ItMarkerProvenance(ItMarker.P, "1.3.3", "प्")
            val term = DerivationTerm("word", surface, kind, createdBySutra = "3.1.1",
                itMarkerProvenance = setOf(history), sourceSuffixUpadeshas = setOf("क्त"))
            val state = DerivationState(listOf(term), stage = DerivationStage.PADA_FORMED)
            val raw = state.substituteFinalVarnaWithRutva(term.id, "8.2.66")
            val result = itEngine.derive(raw, DerivationConfig(computeSvara = false))
            val final = result.final.terms.single()
            assertEquals(listOf("1.3.2", "1.3.9"), result.applications.map { it.sutra })
            assertEquals(term.varnas.dropLast(1) + Vyanjana.RA, final.varnas)
            assertEquals(term.phonologicalText.effectiveVarnas.count { it.nasalized },
                final.phonologicalText.effectiveVarnas.count { it.nasalized })
            assertEquals(term.upadesha, final.upadesha)
            assertEquals(term.createdBySutra, final.createdBySutra)
            assertEquals(term.sourceSuffixUpadeshas, final.sourceSuffixUpadeshas)
            assertTrue(history in final.itMarkerProvenance)
        }
    }

    @Test fun `hali deletes y not visarga s or a following y vowel`() {
        assertFalse(HaliSarveshamSutra.matches(DerivationState(listOf(
            DerivationTerm("left", "भोय्", TermKind.PRATIPADIKA),
            DerivationTerm("right", "देवाः", TermKind.PRATIPADIKA)))))
        for (surface in listOf("भोः", "भोस्", "आः", "आस्", "आय", "भोय्", "आय्")) {
            val state = DerivationState(listOf(DerivationTerm("left", surface, TermKind.PRATIPADIKA),
                DerivationTerm("right", "क", TermKind.PRATIPADIKA)),
                samjnas = setOf(SamjnaAssignment("left", dev.panini.shiksha.Samjna.PADA)))
            assertFalse(HaliSarveshamSutra.matches(state), surface)
        }
        val result = engine.join(
            SandhiEngine.Pada(DerivationTerm("left", "भोः", TermKind.PRATIPADIKA), visargaSource = VisargaSource.S),
            SandhiEngine.Pada(DerivationTerm("right", "देवाः", TermKind.PRATIPADIKA)),
            DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL))
        assertEquals("भोदेवाः", engine.render(result))
        assertTrue(result.applications.any { it.sutra == "8.3.17" })
        assertTrue(result.applications.any { it.sutra == "8.3.22" })
    }
}
