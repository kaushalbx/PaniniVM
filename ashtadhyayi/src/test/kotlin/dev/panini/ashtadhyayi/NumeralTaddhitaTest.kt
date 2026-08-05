package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya5.pada3.EdhaccaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.SankhyayascavidhartheDhaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.DvitrichaturbhyahSucSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.SankhyayahKriyaAbhyavrttiKrtvasucSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna

class NumeralTaddhitaTest {

    @Test
    fun `test SankhyayascavidhartheDhaSutra derives dvidha and pancadha`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "द्वि", kind = TermKind.PRATIPADIKA, upadesha = "द्वि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(SankhyayascavidhartheDhaSutra.matches(state))
        val change = SankhyayascavidhartheDhaSutra.apply(state)
        assertEquals("धा", change.state.terms.last().surface)
    }

    @Test
    fun `test EdhaccaSutra matches eka and derives ekadha`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "एक", kind = TermKind.PRATIPADIKA, upadesha = "एक")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(EdhaccaSutra.matches(state))
        val change = EdhaccaSutra.apply(state)
        assertEquals("धा", change.state.terms.last().surface)
    }

    @Test
    fun `test SankhyayahKriyaAbhyavrttiKrtvasucSutra derives pancakrtvah`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "पञ्च", kind = TermKind.PRATIPADIKA, upadesha = "पञ्चन्")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(state))
        val change = SankhyayahKriyaAbhyavrttiKrtvasucSutra.apply(state)
        assertEquals("कृत्वः", change.state.terms.last().surface)
    }

    @Test
    fun `test DvitrichaturbhyahSucSutra derives sakrt, dvih, trih, catuh`() {
        val stateDvi = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "द्वि", kind = TermKind.PRATIPADIKA, upadesha = "द्वि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(DvitrichaturbhyahSucSutra.matches(stateDvi))
        val changeDvi = DvitrichaturbhyahSucSutra.apply(stateDvi)
        assertEquals("द्विः", changeDvi.state.terms.single().surface)

        val stateEka = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "एक", kind = TermKind.PRATIPADIKA, upadesha = "एक")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        val changeEka = DvitrichaturbhyahSucSutra.apply(stateEka)
        assertEquals("सकृत्", changeEka.state.terms.single().surface)
    }
}
