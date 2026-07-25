package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.PratyayasyaLupSlulopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.PrathamanirdistamSamasaUpasarjanamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.UpasarjanamPurvamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada4.VipratisedheParamKaryamSutra
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConflictAndCompoundSamjnaTest {

    @Test
    fun testVipratisedheParamKaryamSutra() {
        assertTrue(VipratisedheParamKaryamSutra.matches(100 to 200))
        assertEquals(200, VipratisedheParamKaryamSutra.apply(100 to 200))
    }

    @Test
    fun testPratyayasyaLupSlulopahSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "अ", TermKind.PRATYAYA)))
        assertTrue(PratyayasyaLupSlulopahSutra.matches(state))
    }

    @Test
    fun testUpasarjanamPurvamSutra() {
        val pair = "राज" to "पुरुष"
        assertTrue(UpasarjanamPurvamSutra.matches(pair))
        assertEquals(pair, UpasarjanamPurvamSutra.apply(pair))
    }

    @Test
    fun testPrathamanirdistamSamasaUpasarjanamSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("dummy", "राज", TermKind.PRATIPADIKA)))
        assertTrue(PrathamanirdistamSamasaUpasarjanamSutra.matches(state))
    }
}
