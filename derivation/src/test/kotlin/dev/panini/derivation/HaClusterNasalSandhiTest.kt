package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.HeMapareVaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.NapareNahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class HaClusterNasalSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `hm allows retained m and anusvara with distinct traces`() {
        val results = engine.joinAll("किम्", "ह्मलयति")
        assertEquals(setOf("किम्ह्मलयति", "किंह्मलयति"), results.map { engine.render(it) }.toSet())
        val retained = results.single { it.applications.any { step -> step.sutra == "8.3.26" } }
        assertFalse(retained.applications.any { it.sutra == "8.3.23" })
        assertEquals("किम्", retained.final.terms.first().surface)
        assertEquals(1, retained.applications.count { it.sutra == "8.3.26" })
    }

    @Test fun `hn allows n and anusvara but not retained m`() {
        val results = engine.joinAll("किम्", "ह्नुते")
        assertEquals(setOf("किन्ह्नुते", "किंह्नुते"), results.map { engine.render(it) }.toSet())
        val nasal = results.single { it.applications.any { step -> step.sutra == "8.3.27" } }
        assertFalse(nasal.applications.any { it.sutra == "8.3.23" })
        assertEquals(2, nasal.final.substitutions.single { it.sutra == "8.3.27" }.sourceVarnaIndex)
    }

    @Test fun `intervening vowels and direct nasal followers do not satisfy h cluster`() {
        for (right in listOf("हनुते", "हमलयति", "मलयति", "नयति", "हसति")) {
            assertTrue(engine.joinAll("किम्", right).all { result ->
                result.applications.none { it.sutra in setOf("8.3.26", "8.3.27") }
            })
        }
    }

    @Test fun `internal boundary without pada designation is excluded`() {
        val state = DerivationState(listOf(DerivationTerm("left", "किम्", TermKind.PRATIPADIKA),
            DerivationTerm("right", "ह्मलयति", TermKind.PRATYAYA)))
        assertFalse(HeMapareVaSutra.matches(state))
        assertFalse(NapareNahSutra.matches(state))
    }

    @Test fun `m retention does not block another m boundary`() {
        val terms = listOf("किम्", "ह्मलयति", "तम्", "गच्छति").mapIndexed { index, word ->
            DerivationTerm("pada-$index", word, TermKind.PRATIPADIKA)
        }
        val state = DerivationState(terms, samjnas = terms.map { SamjnaAssignment(it.id, Samjna.PADA) }.toSet())
        val retained = HeMapareVaSutra.apply(state).state
        assertFalse(HeMapareVaSutra.matches(retained))
        assertTrue(MonusvarahSutra.matches(retained))
        val after = MonusvarahSutra.apply(retained).state
        assertEquals("किम्", after.terms.first().surface)
        assertEquals("तं", after.terms[2].surface)
    }
}
