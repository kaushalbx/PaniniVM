package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AloAntyatPurvaUpadhaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AnuditSavarnasyaCapratyayahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SnatSatSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SvamRupamSabdasyasabdasamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TarapTamapGhahSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.core.ItMarker
import dev.panini.shiksha.Svara
import dev.panini.shiksha.VarnaReference
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
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
        val state = DerivationState(terms = listOf(DerivationTerm("affix", "शतृँ", TermKind.PRATYAYA, upadesha = "शतृँ")))
        assertTrue(SnatSatSutra.matches(state))
    }

    @Test
    fun testAloAntyatPurvaUpadhaSutra() {
        assertTrue(AloAntyatPurvaUpadhaSutra.matches("राम".toVarnas()))
        assertEquals(Vyanjana.MA, AloAntyatPurvaUpadhaSutra.apply("राम".toVarnas()))
    }

    @Test
    fun testSvamRupamSabdasyasabdasamjnaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "अ", TermKind.PRATYAYA)))
        assertTrue(SvamRupamSabdasyasabdasamjnaSutra.matches(state))
    }

    @Test
    fun testAnuditSavarnasyaCapratyayahSutra() {
        val ku = VarnaReference(Vyanjana.KA, itMarkers = setOf(ItMarker.U))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches(ku))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches(VarnaReference(Svara.A)))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.apply(ku))
    }
}
