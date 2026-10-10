package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.ShiTukSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.AnusvarasyaYayiParasavarnahSutra
import dev.panini.shiksha.*
import kotlin.test.*

class ShiTukTest {
    @Test
    fun `mandatory parasavarna excludes pada and prefix final anusvara`() {
        val left = DerivationTerm("left", "सं", TermKind.PRATIPADIKA)
        val right = DerivationTerm("right", "भू", TermKind.DHATU)
        for (samjna in listOf(Samjna.PADA, Samjna.UPASARGA)) {
            val boundary = DerivationState(listOf(left, right), samjnas = setOf(SamjnaAssignment(left.id, samjna)))
            assertFalse(AnusvarasyaYayiParasavarnahSutra.matches(boundary))
        }
        val internal = DerivationState(listOf(left.copy(surface = "शंभुः")))
        assertTrue(AnusvarasyaYayiParasavarnahSutra.matches(internal))
        assertEquals("शम्भुः", AnusvarasyaYayiParasavarnahSutra.apply(internal).state.surface)
    }

    @Test
    fun `tuk is final to the licensed pada and preserves annotations`() {
        val left = DerivationTerm("left", "सँन्ऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
        val right = DerivationTerm("right", "शम्भुः", TermKind.PRATIPADIKA)
        val original = DerivationState(listOf(left, right), samjnas = setOf(SamjnaAssignment(left.id, Samjna.PADA)))
        assertTrue(ShiTukSutra.matches(original))
        val result = ShiTukSutra.apply(original).state
        assertEquals(listOf(Vyanjana.SA, Svara.A, Vyanjana.NA, Vyanjana.TA), result.terms.first().varnas)
        assertTrue(result.terms.first().phonologicalText.effectiveVarnas[1].nasalized)
        assertEquals(left.id, result.terms.first().id)
        assertEquals(left.createdBySutra, result.terms.first().createdBySutra)
        assertEquals(right, result.terms.last())
        assertFalse(ShiTukSutra.matches(result))
        assertFalse(ShiTukSutra.matches(original.copy(samjnas = emptySet())))
        assertFalse(ShiTukSutra.matches(original.replaceTerm(right.id, right.copy(surface = "षम्भुः"))))
        assertFalse(ShiTukSutra.matches(original.replaceTerm(left.id, left.copy(surface = "सम्"))))
    }
}
