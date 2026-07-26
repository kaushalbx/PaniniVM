package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AloAntyatPurvaUpadhaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AnuditSavarnasyaCapratyayahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SnatSatSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SvamRupamSabdasyasabdasamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TarapTamapGhahSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MetaGrammarSamjnaTest {

    @Test
    fun testTarapTamapGhahSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("affix", "तरप्", TermKind.PRATYAYA, upadesha = "तरप्")))
        assertTrue(TarapTamapGhahSutra.matches(state))
    }

    @Test
    fun testSnatSatSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("affix", "शतृ", TermKind.PRATYAYA, upadesha = "शतृ")))
        assertTrue(SnatSatSutra.matches(state))
    }

    @Test
    fun testAloAntyatPurvaUpadhaSutra() {
        assertTrue(AloAntyatPurvaUpadhaSutra.matches("राम"))
        assertEquals('ा', AloAntyatPurvaUpadhaSutra.apply("राम"))
    }

    @Test
    fun testSvamRupamSabdasyasabdasamjnaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "अ", TermKind.PRATYAYA)))
        assertTrue(SvamRupamSabdasyasabdasamjnaSutra.matches(state))
    }

    @Test
    fun testAnuditSavarnasyaCapratyayahSutra() {
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches("कु"))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches("अ"))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.apply("कु"))
    }
}
