package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.adhyaya7.pada4.HrasvahSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.Rupa
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LitAbhyasaSutrasTest {
    @Test
    fun `6 1 8 duplicates a lit root and 6 1 4 designates the first copy as abhyasa`() {
        val state = DerivationState(
            listOf(DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(LitiDhatorAnabhyasasyaSutra.matches(state))
        val duplicated = LitiDhatorAnabhyasasyaSutra.apply(state).state
        assertEquals(listOf("भू", "भू"), duplicated.terms.map { it.surface })
        assertTrue(PurvoBhyasahSutra.matches(duplicated))
        val designated = PurvoBhyasahSutra.apply(duplicated).state
        assertTrue(designated.samjnas.any { it.targetId == "abhyasa" && it.samjna == Samjna.ABHYASA })
    }

    @Test
    fun `7 4 59 shortens the long vowel of a designated abhyasa`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
            ),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(HrasvahSutra.matches(state))
        val result = HrasvahSutra.apply(state).state
        assertEquals(listOf("भु", "भू"), result.terms.map { it.surface })
    }
}
