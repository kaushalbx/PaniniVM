package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.GhuSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class GhuDesignationTest {
    private val rule = GhuSutra

    @Test fun `six lexical roots receive ghu even after their shape changes`() {
        val roots = listOf("डुदाञ्", "दाण्", "दो", "देङ्", "डुधाञ्", "धेट्").mapIndexed { index, upadesha ->
            DerivationTerm("root-$index", "दि", TermKind.DHATU, upadesha = upadesha)
        }
        val original = DerivationState(roots, samjnas = setOf(SamjnaAssignment(roots.first().id, Samjna.GHI)))
        assertTrue(rule.matches(original))
        val result = rule.apply(original).state
        assertEquals(roots, result.terms)
        roots.forEach { assertTrue(SamjnaAssignment(it.id, Samjna.GHU) in result.samjnas) }
        assertTrue(original.samjnas.all { it in result.samjnas })
        assertFalse(rule.matches(result))
        assertEquals(result, rule.apply(result).state)
    }

    @Test fun `excluded homophonous roots and nonroot spellings do not receive ghu`() {
        for (upadesha in listOf("दाप्", "दैप्", "दा", "धा", "अज्ञात")) {
            val state = DerivationState(listOf(DerivationTerm("root", "दा", TermKind.DHATU, upadesha = upadesha)))
            assertFalse(rule.matches(state), upadesha)
            assertEquals(state, rule.apply(state).state)
        }
        assertFalse(rule.matches(DerivationState(listOf(
            DerivationTerm("stem", "दा", TermKind.PRATIPADIKA, upadesha = "डुदाञ्")))))
    }

    @Test fun `authentic designation is registered once and executable`() {
        assertSame(rule, Ashtadhyayi.registry.require(rule.number))
        assertEquals(1, Ashtadhyayi.cataloguedSutras.count { it.number == rule.number })
        assertTrue(Ashtadhyayi.executableSutras.any { it === rule })
        assertTrue(Ashtadhyayi.runtimeSutras.any { it === rule })
    }

    @Test fun `designation engine records one application on real dhatupatha entries`() {
        for (root in listOf(dev.panini.dhatupatha.juhotyadi.DaDhatu(), dev.panini.dhatupatha.juhotyadi.DhaDhatu())) {
            val source = DerivationTerm("root", root.sourceSurface, TermKind.DHATU, upadesha = root.upadesha)
            val registered = Ashtadhyayi.registry.require(rule.number) as DerivationSutra
            val result = DerivationEngine(listOf(registered)).derive(DerivationState(listOf(source)))
            assertEquals(listOf(source), result.final.terms)
            assertEquals(1, result.applications.count { it.sutra == rule.sutra })
            assertTrue(SamjnaAssignment(source.id, Samjna.GHU) in result.final.samjnas)
            result.final.requireCompleteItProcessing()
        }
    }
}
