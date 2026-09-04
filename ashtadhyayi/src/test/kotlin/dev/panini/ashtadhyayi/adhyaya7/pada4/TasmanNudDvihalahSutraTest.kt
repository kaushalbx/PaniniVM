package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
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
        assertEquals(listOf("आ", "नुट्", "अङ्ग्"), result.terms.map { it.surface })
        assertEquals("नुट्", result.terms[1].upadesha)
        assertEquals("7.4.71", result.terms[1].createdBySutra)
        assertEquals("dhatu", result.terms[1].augmentTargetId)
    }
}
