package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.NaVibhaktauTusmahSutra
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NaVibhaktauTusmahLifecycleTest {
    @Test
    fun `fresh tusma-final ting is protected before halantyam designation`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(
                    id = "ting-thas-a",
                    surface = "थास्",
                    kind = TermKind.PRATYAYA,
                    upadesha = "थास्",
                    itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
                ),
            ),
            stage = DerivationStage.IT_PROCESSED,
        )

        assertTrue(NaVibhaktauTusmahSutra.matches(state))
        val protected = NaVibhaktauTusmahSutra.apply(state).state
        assertTrue("ting-thas-a" in protected.halantyamExemptTermIds)
        assertFalse(HalantyamSutra.matches(protected))

        val result = DerivationEngine(
            listOf(NaVibhaktauTusmahSutra, HalantyamSutra, dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra),
        ).derive(state)
        assertTrue(result.applications.any { it.sutra == "1.3.4" }, result.applications.map { it.sutra }.toString())
        assertTrue(result.final.terms.single().surface == "थास्")
    }
}
