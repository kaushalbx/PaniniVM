package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.*
import dev.panini.shiksha.*
import kotlin.test.*

class VisargaRetentionSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `shar admits visarga retention and s substitution alternatives`() {
        for ((right, substituted) in listOf("शेते" to "वृक्षश्शेते", "षण्डे" to "वृक्षष्षण्डे", "साये" to "वृक्षस्साये")) {
            val results = engine.joinAll("वृक्षः", right)
            assertEquals(setOf("वृक्षः$right", substituted), results.map(engine::render).toSet())
            val retention = results.single { r -> r.applications.any { it.sutra == "8.3.36" } }
            assertTrue(retention.applications.none { it.sutra == "8.3.34" })
            assertTrue(retention.events.filterIsInstance<DerivationEvent.BranchCreated>().any { it.sutra == "8.3.36" })
        }
    }

    @Test fun `khar followed immediately by shar requires retention`() {
        for ((left, right) in listOf("शशः" to "क्षुरम्", "वासः" to "क्षौमम्", "पुरुषः" to "त्सरुः", "अद्भिः" to "प्सातम्")) {
            val results = engine.joinAll(left, right)
            assertEquals(setOf(left + right), results.map(engine::render).toSet())
            assertTrue(results.all { r -> r.applications.any { it.sutra == "8.3.35" } })
            assertTrue(results.all { r -> r.applications.none { it.sutra in setOf("8.3.34", "8.3.36") } })
        }
    }

    @Test fun `ordinary dental follower still receives s not retention`() {
        val result = engine.join("रामः", "तत्र")
        assertEquals("रामस्तत्र", engine.render(result))
        assertTrue(result.applications.none { it.sutra in setOf("8.3.35", "8.3.36") })
        assertTrue(result.applications.any { it.sutra == "8.3.34" })
    }

    @Test fun `shar must immediately follow khar for compulsory retention`() {
        for (right in listOf("कशा", "ग्शा", "तत्र")) {
            val state = DerivationState(listOf(DerivationTerm("left", "वृक्षः", TermKind.PRATIPADIKA),
                DerivationTerm("right", right, TermKind.PRATIPADIKA)))
            assertFalse(SharpareVisarjaniyahSutra.matches(state), right)
        }
    }

    @Test fun `explicit underlying s proceeds through ru and visarga before retention`() {
        val results = engine.joinAll("वृक्षस्", "शेते")
        assertEquals(setOf("वृक्षःशेते", "वृक्षश्शेते"), results.map(engine::render).toSet())
        for (result in results) assertTrue(result.applications.map { it.sutra }.containsAll(listOf("8.2.66", "8.3.15")))
    }

    @Test fun `retention keeps term identity annotations and signs unchanged`() {
        val left = DerivationTerm("left", "अँःऽ", TermKind.PRATIPADIKA,
            createdBySutra = "4.1.2", sourceSuffixUpadeshas = setOf("क्त"))
        val right = DerivationTerm("right", "शेते", TermKind.PRATIPADIKA)
        val state = DerivationState(listOf(left, right))
        val retained = VaShariSutra.apply(state).state
        assertEquals(state.terms, retained.terms)
        assertFalse(VisarjaniyasyaSahSutra.matches(retained))
        assertFalse(VaShariSutra.matches(retained))
        assertEquals(emptyList(), retained.substitutions)
    }

    @Test fun `boundary protection does not suppress a distinct boundary`() {
        val state = DerivationState(listOf(DerivationTerm("a", "अः", TermKind.PRATIPADIKA),
            DerivationTerm("b", "शेते", TermKind.PRATIPADIKA),
            DerivationTerm("c", "इः", TermKind.PRATIPADIKA),
            DerivationTerm("d", "तत्र", TermKind.PRATIPADIKA)))
        val retained = VaShariSutra.apply(state).state
        assertTrue(VisarjaniyasyaSahSutra.matches(retained))
        val changed = VisarjaniyasyaSahSutra.apply(retained).state
        assertEquals("अः", changed.terms[0].surface)
        assertEquals("इस्", changed.terms[2].surface)
    }

    @Test fun `identity retention does not advance past work at other boundaries`() {
        val state = DerivationState(listOf(DerivationTerm("a", "अः", TermKind.PRATIPADIKA),
            DerivationTerm("b", "क्षः", TermKind.PRATIPADIKA),
            DerivationTerm("c", "शः", TermKind.PRATIPADIKA),
            DerivationTerm("d", "तत्र", TermKind.PRATIPADIKA)))
        val engine = DerivationEngine(listOf(VisarjaniyasyaSahSutra, SharpareVisarjaniyahSutra, VaShariSutra))
        val result = engine.derive(state, DerivationConfig(computeSvara = false))
        assertEquals(listOf("अः", "क्षः", "शस्", "तत्र"), result.final.terms.map { it.surface })
        assertEquals(listOf("8.3.35", "8.3.36", "8.3.34"), result.applications.map { it.sutra })
        assertTrue(result.applications.first().conflictTrace.none { it.contains("8.3.36") })
    }
}
