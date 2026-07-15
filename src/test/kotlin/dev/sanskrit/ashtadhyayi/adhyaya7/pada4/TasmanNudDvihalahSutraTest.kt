package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationalContext
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Rupa
import dev.sanskrit.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TasmanNudDvihalahSutraTest {
    @Test
    fun `7 4 71 inserts effective nut after a lengthened abhyasa before a two-consonant root`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "आ", TermKind.DHATU, upadesha = "अ"),
                DerivationTerm("dhatu", "अङ्ग्", TermKind.DHATU, upadesha = "अङ्ग्"),
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(TasmanNudDvihalahSutra.matches(state))
        val result = TasmanNudDvihalahSutra.apply(state).state
        assertEquals(listOf("आ", "न्", "अङ्ग्"), result.terms.map { it.surface })
        assertEquals("नुट्", result.terms[1].upadesha)
    }
}
