package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada4.*
import dev.panini.core.*
import kotlin.test.*

class AngaConditionVarnaTest {
    @Test
    fun `nic lopa identifies its vowel from canonical varnas`() {
        val root = DerivationTerm("root", "भू", TermKind.DHATU)
        val nic = DerivationTerm("nic", "इँऽ", TermKind.PRATYAYA, upadesha = SanadiAffix.NIC.upadesha)
        val suffix = DerivationTerm("suffix", "अ", TermKind.PRATYAYA)
        val original = DerivationState(listOf(root, nic, suffix), activeAdhikaras = setOf("6.4.1"),
            context = DerivationalContext(environments = setOf(DerivationalEnvironment.ARDHADHATUKA)))
        assertTrue(NerAnitiSutra.matches(original))
        val result = NerAnitiSutra.apply(original).state
        assertEquals(listOf(root, suffix), result.terms)
        assertEquals(nic.id, result.droppedTerms.single().id)
        assertEquals(nic.upadesha, result.droppedTerms.single().upadesha)
        assertFalse(NerAnitiSutra.matches(original.replaceTerm(nic.id, nic.copy(surface = "ई"))))
    }

    @Test
    fun `dhi environment and completed-form exclusion inspect varnas`() {
        val root = DerivationTerm("root", "दा", TermKind.DHATU, upadesha = "डुदाञ्")
        val ending = DerivationTerm("ending", "धिँऽ", TermKind.PRATYAYA)
        val original = DerivationState(listOf(root, ending), context = DerivationalContext(rupa = Rupa(lakara = Lakara.LOT)))
        assertTrue(GhvasorEddhavabhyasalopashCaSutra.matches(original))
        for (complete in listOf("देँऽ", "देहिँऽ")) {
            assertFalse(GhvasorEddhavabhyasalopashCaSutra.matches(original.replaceTerm(root.id, root.copy(surface = complete))))
        }
        assertFalse(GhvasorEddhavabhyasalopashCaSutra.matches(original.replaceTerm(ending.id, ending.copy(surface = "धी"))))
    }
}
