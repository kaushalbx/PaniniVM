package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IndraSandhiTest {
    @Test
    fun `avang retains the vowel boundary for ordinary guna`() {
        val left = DerivationTerm("left", "गो", TermKind.PRATIPADIKA)
        val initial = DerivationState(
            terms = listOf(left, DerivationTerm("right", "इन्द्रः", TermKind.PRATIPADIKA)),
            stage = DerivationStage.PADA_FORMED,
            samjnas = setOf(SamjnaAssignment(left.id, dev.panini.shiksha.Samjna.PADA)),
        )
        val avang = dev.panini.ashtadhyayi.adhyaya6.pada1.IndreCaSutra.apply(initial).state
        assertEquals(listOf("गव", "इन्द्रः"), avang.terms.map { it.surface })
        assertTrue(dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra.matches(avang))
        assertEquals("गवेन्द्रः", dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra.apply(avang).state.surface)
    }

    @Test
    fun `go before Indra receives compulsory avang followed by guna`() {
        val result = SandhiEngine().join("गो", "इन्द्रः")
        assertEquals("गवेंद्रः", result.final.surface)
        val rules = result.applications.map { it.sutra }
        assertTrue("6.1.124" in rules)
        assertTrue("6.1.87" in rules)
        assertTrue(rules.indexOf("6.1.124") < rules.indexOf("6.1.87"))
    }

    @Test
    fun `other o endings retain ordinary ec sandhi`() {
        assertEquals("नविन्द्रः", SandhiEngine().join("नो", "इन्द्रः").final.surface)
    }
}
