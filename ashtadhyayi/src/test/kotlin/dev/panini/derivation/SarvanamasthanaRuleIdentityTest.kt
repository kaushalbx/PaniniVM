package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada4.SantamahatahSamyogasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.SarvanamasthaneCasambuddhauSutra
import dev.panini.core.SupAffix
import kotlin.test.*

class SarvanamasthanaRuleIdentityTest {
    @Test fun `lengthening rules recognize typed affixes with arbitrary ids`() {
        for (identity in listOf(SupAffix.SU, SupAffix.AU, SupAffix.JAS, SupAffix.AM, SupAffix.AUT)) {
            val affix = identity.term().copy(id = "ending")
            fun state(stem: String, suffix: DerivationTerm = affix) = DerivationState(
                listOf(DerivationTerm("stem", stem, TermKind.PRATIPADIKA), suffix),
                activeAdhikaras = setOf("6.4.1"),
            )
            assertTrue(SarvanamasthaneCasambuddhauSutra.matches(state("राजन्")))
            assertTrue(SantamahatahSamyogasyaSutra.matches(state("महत्")))
            val lookalike = affix.copy(kind = TermKind.PRATIPADIKA)
            assertFalse(SarvanamasthaneCasambuddhauSutra.matches(state("राजन्", lookalike)))
            assertFalse(SantamahatahSamyogasyaSutra.matches(state("महत्", lookalike)))
        }
    }
}
