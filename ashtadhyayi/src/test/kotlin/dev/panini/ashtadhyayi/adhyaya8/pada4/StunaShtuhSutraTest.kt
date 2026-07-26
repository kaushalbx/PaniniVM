package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StunaShtuhSutraTest {
    @Test
    fun `retroflexes dental ta after retroflex ta`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("augment", "सीयुट्", TermKind.AGAMA),
                DerivationTerm("ending", "त", TermKind.PRATYAYA),
            ),
        )

        assertTrue(StunaShtuhSutra.matches(state))
        val result = StunaShtuhSutra.apply(state).state
        assertEquals("ट", result.terms.last().surface)
        assertFalse(StunaShtuhSutra.matches(result))
    }
}
