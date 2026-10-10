package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada2.AtoYeyahSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.LingsicorAtmanepadesuSutra
import dev.panini.core.*
import kotlin.test.*

class LingSicVarnaTest {
    @Test
    fun `yasut completion condition uses varnas without changing gana exclusions`() {
        val root = DerivationTerm("root", "भव", TermKind.DHATU, gana = DhatuGana.BHVADI)
        val augment = DerivationTerm("yasut", "याँस्ऽ", TermKind.AGAMA, upadesha = "यासुट्")
        val original = DerivationState(listOf(root, augment), context = DerivationalContext(rupa = Rupa(lakara = Lakara.LING)))
        assertTrue(AtoYeyahSutra.matches(original))
        val result = AtoYeyahSutra.apply(original).state
        assertEquals("इय्", result.terms.last().surface)
        assertEquals(augment.id, result.terms.last().id)
        assertEquals(augment.upadesha, result.terms.last().upadesha)
        assertEquals(root, result.terms.first())
        assertFalse(AtoYeyahSutra.matches(result))
        assertFalse(AtoYeyahSutra.matches(original.replaceTerm(root.id, root.copy(gana = DhatuGana.ADADI))))
        assertFalse(AtoYeyahSutra.matches(original.replaceTerm(augment.id, augment.copy(surface = "यस्"))))
    }

    @Test
    fun `sic insertion targets the same pronounced affix that matches`() {
        val root = DerivationTerm("root", "कृ", TermKind.DHATU)
        val raw = DerivationTerm("raw", "सिँच्", TermKind.PRATYAYA, upadesha = "सिँच्")
        val sic = DerivationTerm("sic", "स्ऽ", TermKind.PRATYAYA, upadesha = "सिँच्")
        val ending = DerivationTerm("ending", "त", TermKind.PRATYAYA, upadesha = TingAffix.TA.upadesha)
        val original = DerivationState(listOf(root, raw, sic, ending))
        assertTrue(LingsicorAtmanepadesuSutra.matches(original))
        val result = LingsicorAtmanepadesuSutra.apply(original).state
        val augment = result.terms.single { it.id == "it-agama" }
        assertEquals(sic.id, augment.augmentTargetId)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, augment.itProcessingPhase)
        assertEquals(listOf(root, raw, sic, ending), result.terms.filter { it.id != augment.id })
        assertFalse(LingsicorAtmanepadesuSutra.matches(result))
        assertFalse(LingsicorAtmanepadesuSutra.matches(original.replaceTerm(ending.id, ending.copy(upadesha = TingAffix.TIP.upadesha))))
    }
}
