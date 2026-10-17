package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.YaroAnunasikeAnunasikoVaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class YaroAnunasikeSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `dental stops optionally become dental nasals before n or m`() {
        assertEquals(setOf("तन्न", "तद्न"), engine.joinAll("तत्", "न").map(engine::render).toSet())
        assertEquals(setOf("तन्मयम्", "तद्मयम्"), engine.joinAll("तत्", "मयम्").map(engine::render).toSet())
    }

    @Test fun `nasal substitute follows source stop class not follower class`() {
        for ((source, nasal) in listOf("वाक्" to Vyanjana.NGA, "अच्" to Vyanjana.NYA,
            "अट्" to Vyanjana.NNA, "अत्" to Vyanjana.NA, "अप्" to Vyanjana.MA)) {
            val result = engine.joinAll(source, "नयति").single { r -> r.applications.any { it.sutra == "8.4.45" } }
            assertEquals(nasal, result.final.terms.first().varnas.last())
            assertTrue(result.applications.map { it.sutra }.indexOf("8.2.39") < result.applications.map { it.sutra }.indexOf("8.4.45"))
        }
    }

    @Test fun `internal non-pada stop is outside the optional rule`() {
        val context = DerivationState(listOf(DerivationTerm("root", "वेद्", TermKind.DHATU),
            DerivationTerm("suffix", "मि", TermKind.PRATYAYA)))
        assertFalse(YaroAnunasikeAnunasikoVaSutra.matches(context))
    }

    @Test fun `non-nasal follower and ra without nasal counterpart do not trigger`() {
        for ((left, right) in listOf("तत्" to "गच्छति", "अग्निर्" to "नयति", "तान्" to "नयति")) {
            assertTrue(engine.joinAll(left, right).none { r -> r.applications.any { it.sutra == "8.4.45" } })
        }
    }

    @Test fun `annotation and identity remain attached to the replaced occurrence`() {
        val original = DerivationState(listOf(DerivationTerm("left", "अँद्ऽ", TermKind.PRATIPADIKA),
            DerivationTerm("right", "न", TermKind.PRATIPADIKA)),
            samjnas = setOf(SamjnaAssignment("left", Samjna.PADA)))
        val result = YaroAnunasikeAnunasikoVaSutra.apply(original).state
        assertEquals("अँन्ऽ", result.terms.first().surface)
        assertEquals(original.terms.last(), result.terms.last())
        assertEquals(1, result.substitutions.single().sourceVarnaIndex)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), result.terms.first().orthographicSigns)
    }
}
