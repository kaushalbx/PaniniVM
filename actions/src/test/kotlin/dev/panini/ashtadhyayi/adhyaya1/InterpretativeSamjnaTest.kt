package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdirAntyenaSahetaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SasthiSthaneYogaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TatiSankhyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasmatItyUttarasyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasminNirdistePurvasyaSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class InterpretativeSamjnaTest {

    @Test
    fun testTatiSankhyaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("num", "कति", TermKind.PRATIPADIKA, upadesha = "डति")))
        assertTrue(TatiSankhyaSutra.matches(state))
    }

    @Test
    fun testTasminNirdistePurvasyaSutra() {
        assertTrue(TasminNirdistePurvasyaSutra.matches("अचि"))
        assertTrue(TasminNirdistePurvasyaSutra.apply("अचि"))
    }

    @Test
    fun testTasmatItyUttarasyamSutra() {
        assertTrue(TasmatItyUttarasyamSutra.matches("तस्मात्"))
        assertTrue(TasmatItyUttarasyamSutra.apply("तस्मात्"))
    }

    @Test
    fun testSasthiSthaneYogaSutra() {
        assertTrue(SasthiSthaneYogaSutra.matches("इकः"))
        assertTrue(SasthiSthaneYogaSutra.apply("इकः"))
    }

    @Test
    fun testAdirAntyenaSahetaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "अ", TermKind.PRATYAYA)))
        assertTrue(AdirAntyenaSahetaSutra.matches(state))
    }
}
