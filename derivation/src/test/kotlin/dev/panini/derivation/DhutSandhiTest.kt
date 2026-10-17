package dev.panini.derivation

import dev.panini.shiksha.*
import dev.panini.ashtadhyayi.adhyaya8.pada3.NashCaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.KhariCaSutra
import kotlin.test.*

class DhutSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `nas ca supplies both source-backed outcomes`() {
        val results = engine.joinAll("भवान्", "साये")
        assertEquals(setOf("भवान्त्साये", "भवान्साये"), results.map { engine.render(it) }.toSet())
        val applied = results.single { it.applications.any { step -> step.sutra == "8.3.30" } }
        assertEquals("भवान्", applied.final.terms.first().surface)
        assertEquals(Vyanjana.TA, applied.final.terms.last().varnas.first())
        assertTrue(applied.applications.any { it.sutra == "1.1.46" })
        assertTrue(applied.applications.any { it.sutra == "8.4.55" })
        assertTrue(applied.final.terms.last().itMarkerProvenance.any { it.designationSutra == "1.3.3" })
        assertTrue(applied.final.terms.last().itMarkerProvenance.any { it.designationSutra == "1.3.2" })
        assertFalse(applied.applications.any { it.sutra == "8.3.7" })
    }

    @Test fun `other source nasals and other followers do not license dhut`() {
        for ((left, right) in listOf("कम्" to "साये", "प्राङ्" to "साये", "भवान्" to "हसति")) {
            assertTrue(engine.joinAll(left, right).all { result -> result.applications.none { it.sutra == "8.3.30" } })
        }
    }

    @Test fun `an internal term boundary does not license external dhut`() {
        val state = DerivationState(listOf(
            DerivationTerm("stem", "भवान्", TermKind.PRATIPADIKA),
            DerivationTerm("suffix", "साये", TermKind.PRATYAYA)))
        assertFalse(NashCaSutra.matches(state))
    }

    @Test fun `internal khari targets exact occurrence and retains nasal annotations`() {
        val state = DerivationState(listOf(DerivationTerm("word", "अँध्स", TermKind.PRATIPADIKA)))
        assertTrue(KhariCaSutra.matches(state))
        val after = KhariCaSutra.apply(state).state
        assertEquals("अँत्स", after.surface)
        assertEquals(1, after.substitutions.single().sourceVarnaIndex)
        assertFalse(KhariCaSutra.matches(after))
    }
}
