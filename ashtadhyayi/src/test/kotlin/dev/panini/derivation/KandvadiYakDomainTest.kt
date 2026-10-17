package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya3.pada1.KandvadibhyoYakSutra
import dev.panini.core.*
import kotlin.test.*

class KandvadiYakDomainTest {
    @Test fun `processed dhatu retains lexical membership through original upadesha`() {
        val initial = DerivationState(listOf(DerivationTerm("root", "कण्डू", TermKind.DHATU, upadesha = "कण्डूञ्")))
        assertTrue(KandvadibhyoYakSutra.matches(initial))
        val after = KandvadibhyoYakSutra.apply(initial).state
        assertEquals("कण्डू", after.terms.first().surface)
        assertEquals("कण्डूञ्", after.terms.first().upadesha)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, after.terms.last().itProcessingPhase)
        val consumed = after.removeTerm("root-yak", "test-consumption")
        assertFalse(KandvadibhyoYakSutra.matches(consumed))
    }

    @Test fun `nominal inflection of payas does not request a verbal yak`() {
        val initial = SubantaDerivationRequest("पयस्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.NAPUMSAKA).initialState()
        assertFalse(KandvadibhyoYakSutra.matches(initial))
        val result = SubantaEngine().derive(SubantaDerivationRequest("पयस्", Vibhakti.TRTIYA, Vacana.EKAVACANA, Linga.NAPUMSAKA))
        assertEquals("पयसा", result.final.surface)
        assertFalse(result.applications.any { it.sutra == "3.1.27" })
        assertTrue(result.final.allEffectiveTerms.none { it.createdBySutra == "3.1.27" })
    }
}
