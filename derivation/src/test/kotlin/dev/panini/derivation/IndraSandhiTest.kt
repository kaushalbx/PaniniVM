package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IndraSandhiTest {
    @Test fun `optional avang preserves source annotations and archives its own ngit proof`() {
        val result = SandhiEngine().join("गोँऽ", "अग्रम्")
        val introduced = result.applications.single { it.sutra == "6.1.123" }.after
        val carrier = introduced.terms.single { it.kind == TermKind.ADESHA }
        val owner = introduced.terms.single { it.id == carrier.adeshaTarget?.termId }
        assertEquals("अवङ्", carrier.surface)
        assertEquals(owner.varnas, carrier.adeshaTarget?.originalVarnas)
        val integrated = result.applications.single { it.sutra == "1.1.53" }.after
        val changed = integrated.terms.single { it.id == owner.id }
        assertEquals("गँवऽ", changed.surface)
        assertEquals(1, integrated.substitutions.last().sourceVarnaIndex)
        assertEquals(owner.phonologicalText.effectiveVarnas.first().copy(sourceSpan = null),
            changed.phonologicalText.effectiveVarnas.first().copy(sourceSpan = null))
        val origin = changed.adeshaOrigins.single()
        assertEquals("6.1.123", origin.prescribingSutra)
        assertEquals("1.1.53", origin.targetingSutra)
        assertTrue(origin.itMarkerProvenance.any { it.marker == dev.panini.core.ItMarker.NGIT })
        assertTrue(changed.itMarkers.none { it == dev.panini.core.ItMarker.NGIT })
        assertTrue(integrated.terms.none { it.kind == TermKind.ADESHA })
        result.final.requireCompleteItProcessing()
    }
    @Test
    fun `avang retains the vowel boundary for ordinary guna`() {
        val left = DerivationTerm("left", "गो", TermKind.PRATIPADIKA)
        val initial = DerivationState(
            terms = listOf(left, DerivationTerm("right", "इन्द्रः", TermKind.PRATIPADIKA)),
            stage = DerivationStage.PADA_FORMED,
            samjnas = setOf(SamjnaAssignment(left.id, dev.panini.shiksha.Samjna.PADA)),
        )
        val avang = dev.panini.ashtadhyayi.adhyaya6.pada1.IndreCaSutra.apply(initial).state
        assertEquals(listOf("गो", "अवङ्", "इन्द्रः"), avang.terms.map { it.surface })
        assertEquals(TermKind.ADESHA, avang.terms[1].kind)
        assertEquals(left.id, avang.terms[1].adeshaTarget?.termId)
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(avang)
        val integrated = result.applications.first { it.sutra == "1.1.53" }.after
        assertEquals(listOf("गव", "इन्द्रः"), integrated.terms.map { it.surface })
        assertTrue(dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra.matches(integrated))
        assertEquals("गवेन्द्रः", result.final.surface)
        val carrier = result.final.droppedTerms.single { it.createdBySutra == "6.1.124" }
        assertEquals("अवङ्", carrier.upadesha)
        assertTrue(carrier.itMarkerProvenance.any { it.marker == dev.panini.core.ItMarker.NGIT })
        assertTrue(integrated.terms.first().itMarkers.none { it == dev.panini.core.ItMarker.NGIT })
    }

    @Test
    fun `go before Indra receives compulsory avang followed by guna`() {
        val result = SandhiEngine().join("गो", "इन्द्रः")
        assertEquals("गवेन्द्रः", result.final.surface)
        val rules = result.applications.map { it.sutra }
        assertTrue("6.1.124" in rules)
        assertTrue("6.1.87" in rules)
        assertTrue(rules.indexOf("6.1.124") < rules.indexOf("6.1.87"))
    }

    @Test
    fun `other o endings retain ordinary ec sandhi`() {
        val engine = SandhiEngine()
        val results = engine.joinAll("नो", "इन्द्रः")
        assertEquals(setOf("नविन्द्रः", "न इन्द्रः"), results.map(engine::render).toSet())
        assertTrue(results.all { it.applications.any { a -> a.sutra == "6.1.78" } })
        assertTrue(results.none { it.applications.any { a -> a.sutra == "6.1.124" } })
    }
}
