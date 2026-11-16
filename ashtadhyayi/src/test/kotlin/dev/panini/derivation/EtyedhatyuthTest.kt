package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EngiPararupamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EtyedhatyuthSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class EtyedhatyuthTest {
    private fun state(prefix: String, root: String, upadesha: String) = DerivationState(listOf(
        DerivationTerm("prefix", prefix, TermKind.PRATIPADIKA, upadesha = prefix),
        DerivationTerm("root", root, TermKind.DHATU, upadesha = upadesha),
    ), stage = DerivationStage.ANGAKARYA, samjnas = setOf(SamjnaAssignment("prefix", Samjna.UPASARGA)))

    @Test fun `6_1_89 gives vrddhi before the e of i and edh`() {
        assertEquals("उपैति", EtyedhatyuthSutra.apply(state("उप", "एति", "इण्")).state.surface)
        assertEquals("उपैधते", EtyedhatyuthSutra.apply(state("उप", "एधते", "एधँ")).state.surface)
    }

    @Test fun `6_1_89 leaves other e-initial roots to 6_1_94`() {
        val ej = state("उप", "एजते", "एजृँ")
        assertFalse(EtyedhatyuthSutra.matches(ej))
        assertTrue(EngiPararupamSutra.matches(ej))
    }
}
