package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.derivation.DerivationalContext
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Rupa
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ashtadhyayi.adhyaya7.pada4.HrasvahSutra
import dev.sanskrit.shiksha.Samjna
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
