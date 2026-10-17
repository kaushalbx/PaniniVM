package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.HalngyabbhyoSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.SamyogantasyaLopaSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class EndingLopaPadaCarrierTest {
    private fun input(pada: Boolean): DerivationState {
        val stem = DerivationTerm("stem", "अनड्वान्ह्", TermKind.PRATIPADIKA, upadesha = "अनडुह्")
        val ending = SupAffix.SU.term().copy(surface = "स्")
        return DerivationState(listOf(stem, ending), samjnas = if (pada)
            setOf(SamjnaAssignment(ending.id, Samjna.PADA)) else emptySet())
    }

    @Test fun `ending lopa preserves established pada on surviving carrier`() {
        val initial = input(true)
        assertTrue(HalngyabbhyoSutra.matches(initial))
        val result = HalngyabbhyoSutra.apply(initial).state
        assertTrue(SamjnaAssignment("stem", Samjna.PADA) in result.samjnas)
        assertEquals(initial.terms.last().id, result.droppedTerms.single().id)
        assertEquals(SupAffix.SU, result.droppedTerms.single().sourceSupAffix)
        assertTrue(SamyogantasyaLopaSutra.matches(result))
        val deleted = SamyogantasyaLopaSutra.apply(result).state
        assertEquals(initial.terms.first().varnas.dropLast(1), deleted.terms.single().varnas)
        assertEquals(Vyanjana.NA, deleted.terms.single().varnas.last())
    }

    @Test fun `ending lopa does not invent an unestablished pada designation`() {
        val result = HalngyabbhyoSutra.apply(input(false)).state
        assertFalse(SamjnaAssignment("stem", Samjna.PADA) in result.samjnas)
        assertFalse(SamyogantasyaLopaSutra.matches(result))
    }
}
