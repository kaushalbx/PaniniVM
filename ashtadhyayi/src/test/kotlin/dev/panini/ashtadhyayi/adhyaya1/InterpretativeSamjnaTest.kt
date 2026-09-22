package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdirAntyenaSahetaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SasthiSthaneYogaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TatiSankhyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasmadItyUttarasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasminnitiNirdishtePurvasyaSutra
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.RuleOperandReference
import dev.panini.sutra.RuleOperandRelation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InterpretativeSamjnaTest {

    @Test
    fun testTatiSankhyaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("num", "कति", TermKind.PRATIPADIKA, upadesha = "डति")))
        assertTrue(TatiSankhyaSutra.matches(state))
    }

    @Test
    fun `1 1 66 resolves an explicit locative operand to its immediate predecessor`() {
        val locative = RuleOperandReference(Vibhakti.SAPTAMI)
        assertTrue(TasminnitiNirdishtePurvasyaSutra.matches(locative))
        assertEquals(RuleOperandRelation.IMMEDIATELY_PRECEDING, TasminnitiNirdishtePurvasyaSutra.apply(locative))
        assertFalse(TasminnitiNirdishtePurvasyaSutra.matches(RuleOperandReference(Vibhakti.PANCHAMI)))
    }

    @Test
    fun `1 1 67 resolves an explicit ablative operand to its immediate successor`() {
        val ablative = RuleOperandReference(Vibhakti.PANCHAMI)
        assertTrue(TasmadItyUttarasyaSutra.matches(ablative))
        assertEquals(RuleOperandRelation.IMMEDIATELY_FOLLOWING, TasmadItyUttarasyaSutra.apply(ablative))
        assertFalse(TasmadItyUttarasyaSutra.matches(RuleOperandReference(Vibhakti.SAPTAMI)))
    }

    @Test
    fun `1 1 49 resolves an explicit genitive operand as the substitution target`() {
        val genitive = RuleOperandReference(Vibhakti.SASTHI)
        assertTrue(SasthiSthaneYogaSutra.matches(genitive))
        assertEquals(RuleOperandRelation.SUBSTITUTION_TARGET, SasthiSthaneYogaSutra.apply(genitive))
        assertFalse(SasthiSthaneYogaSutra.matches(RuleOperandReference(Vibhakti.SAPTAMI)))
    }

    @Test
    fun `canonical interpretive sutras are catalogued once and are not executable derivation rules`() {
        val expected = mapOf(
            "1.1.49" to "षष्ठी स्थानेयोगा",
            "1.1.66" to "तस्मिन्निति निर्दिष्टे पूर्वस्य",
            "1.1.67" to "तस्मादित्युत्तरस्य",
        )
        expected.forEach { (number, text) ->
            val matches = Ashtadhyayi.registry.sutras.filter { it.number == number }
            assertEquals(1, matches.size)
            assertEquals(text, matches.single().text)
            assertFalse(Ashtadhyayi.runtimeSutras.any { it.number == number })
        }
    }

    @Test
    fun testAdirAntyenaSahetaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "अ", TermKind.PRATYAYA)))
        assertTrue(AdirAntyenaSahetaSutra.matches(state))
    }
}
