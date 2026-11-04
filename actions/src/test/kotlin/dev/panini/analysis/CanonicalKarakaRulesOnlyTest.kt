package dev.panini.analysis

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import dev.panini.ashtadhyayi.adhyaya1.pada4.KarakeSutra
import dev.panini.ashtadhyayi.adhyaya2.pada3.AnabhihiteSutra
import dev.panini.sutra.SutraRole
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada

class CanonicalKarakaRulesOnlyTest {
    @Test fun `anabhihite governs eligibility without assigning nominative`() {
        val domain = AnabhihiteSutra.role as SutraRole.Adhikara
        for (abhihita in listOf(false, true)) {
            val context = VibhaktiRuleContext(Karaka.KARMAN, setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA),
                abhihita = abhihita)
            assertEquals(!abhihita, domain.isContextEligible(context))
            assertFalse(AnabhihiteSutra.matches(context))
            assertFailsWith<UnsupportedOperationException> { AnabhihiteSutra.apply(context) }
        }
        assertTrue(KarakaRuleEngine.vibhaktiRules.none { it.number in setOf("2.3.1", "2.3.48", "2.3.49", "2.3.69", "2.3.70") })
    }

    @Test fun `every governed samjna is executable exactly once and the adhikara never assigns a role`() {
        val expected = (24..55).map { "1.4.$it" }.toSet()
        val actual = KarakaRuleEngine.karakaRules.map { it.number }
        assertEquals(expected, actual.toSet())
        assertEquals(expected.size, actual.size)
        assertTrue(KarakaRuleEngine.karakaRules.all { "1.4.23" in it.adhikara })
        val participant = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.PANCHAMI))
        val context = KarakaRuleContext(DhatuIdentity("गम्"), participant, listOf(participant),
            Prayoga.KARTARI, candidates = setOf(Karaka.APADANA))
        assertFalse(KarakeSutra.matches(context))
        assertFailsWith<UnsupportedOperationException> { KarakeSutra.apply(context) }
        val result = KarakaRuleEngine.resolve(context)
        assertEquals(null, result.resolved, "A case-derived hypothesis alone is not a saṃjñā")
        assertTrue(result.evidence.none { it.sutra == "1.4.23" })
    }

    @Test fun `karaka and vibhakti registries contain only canonical numbered sutras`() {
        val canonicalNumber = Regex("[1-8]\\.[1-4]\\.[1-9][0-9]*")
        (KarakaRuleEngine.karakaRules + KarakaRuleEngine.vibhaktiRules).forEach {
            assertTrue(canonicalNumber.matches(it.number), "Noncanonical executable rule: ${it.number}")
        }
    }
}
