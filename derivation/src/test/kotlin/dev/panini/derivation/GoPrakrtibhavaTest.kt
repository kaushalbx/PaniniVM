package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class GoPrakrtibhavaTest {
    private fun state(vararg surfaces: String) = DerivationState(
        terms = surfaces.mapIndexed { index, surface -> DerivationTerm("term-$index", surface, TermKind.PRATIPADIKA) },
        stage = DerivationStage.PADA_FORMED,
        samjnas = surfaces.indices.mapTo(mutableSetOf()) { SamjnaAssignment("term-$it", Samjna.PADA) },
    )

    @Test fun `prakrtibhava retains term identity and blocks only its boundary`() {
        val initial = state("गो", "अग्रम्", "नो", "इति")
        val preserved = SarvatraVibhashaGohSutra.apply(initial).state
        assertEquals(initial.terms, preserved.terms)
        assertEquals(initial.samjnas, preserved.samjnas)
        assertNotEquals(initial, preserved)
        assertEquals(preserved, preserved.copy())
        assertEquals(preserved.hashCode(), preserved.copy().hashCode())
        assertFalse(SarvatraVibhashaGohSutra.matches(preserved))
        assertTrue(EcoYavayavahSutra.matches(preserved))
        val changed = EcoYavayavahSutra.apply(preserved).state
        assertEquals("गो", changed.terms[0].surface)
        assertEquals("अग्रम्", changed.terms[1].surface)
        assertEquals("नविति", changed.terms[2].surface)
    }

    @Test fun `short a and pada are necessary for prakrtibhava`() {
        assertTrue(SarvatraVibhashaGohSutra.matches(state("गो", "अग्रम्")))
        assertFalse(SarvatraVibhashaGohSutra.matches(state("गो", "आगमः")))
        assertFalse(SarvatraVibhashaGohSutra.matches(state("गो", "इन्द्रः")))
        assertFalse(SarvatraVibhashaGohSutra.matches(state("नो", "अग्रम्")))
        assertFalse(SarvatraVibhashaGohSutra.matches(state("गो", "अग्रम्").copy(samjnas = emptySet())))
    }

    @Test fun `preserved short a prevents purvarupa but not unrelated internal nasal rules`() {
        val result = SandhiEngine().join("गो", "अन्तः", DerivationConfig(
            optionalRulePolicy = OptionalRulePolicy.CUSTOM,
            optionalRuleSelector = { it != "6.1.123" },
        ))
        assertEquals("गोअन्तः", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.1.122" })
        assertTrue(result.applications.any { it.sutra == "8.3.24" })
        assertTrue(result.applications.any { it.sutra == "8.4.58" })
        assertFalse(result.applications.any { it.sutra == "6.1.109" || it.sutra == "6.1.78" })
    }
}
