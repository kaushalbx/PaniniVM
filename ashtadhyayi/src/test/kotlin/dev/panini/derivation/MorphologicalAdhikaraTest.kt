package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MorphologicalAdhikaraTest {

    @Test
    fun `enforces Angasya domain structurally`() {
        // Angasya adhikara is 6.4.1 to 7.4.97 (krama: 640001 to 740097)
        // Let's use 701013 (rule 7.1.13) as an example.
        val stateWithoutSuffix = DerivationState(
            terms = listOf(DerivationTerm("stem", "राम", TermKind.PRATIPADIKA))
        )
        val stateWithSuffix = DerivationState(
            terms = listOf(
                DerivationTerm("stem", "राम", TermKind.PRATIPADIKA),
                DerivationTerm("suffix", "ङे", TermKind.PRATYAYA)
            )
        )

        assertFalse(isDerivationEligible(710013, stateWithoutSuffix))
        assertTrue(isDerivationEligible(710013, stateWithSuffix))
    }

    @Test
    fun `enforces Dhatoh domain structurally`() {
        // Dhatoh adhikara is 3.1.91 to 3.4.117 (krama: 310091 to 340117)
        // Let's use 320001 (representing a rule in 3.2) as an example.
        val stateWithVerbalRoot = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU))
        )
        val stateWithNominalRoot = DerivationState(
            terms = listOf(DerivationTerm("stem", "घट", TermKind.PRATIPADIKA))
        )

        assertTrue(isDerivationEligible(320001, stateWithVerbalRoot))
        assertFalse(isDerivationEligible(320001, stateWithNominalRoot))
    }

    @Test
    fun `enforces Padasya domain structurally`() {
        // Padasya adhikara is 8.1.16 to 8.3.119 (krama: 810016 to 830119)
        // Let's use 802016 (representing a rule in 8.2) as an example.
        val stateSingleTerm = DerivationState(
            terms = listOf(DerivationTerm("word", "रामः", TermKind.PRATIPADIKA))
        )
        val stateWithActiveSuffix = DerivationState(
            terms = listOf(
                DerivationTerm("stem", "राम", TermKind.PRATIPADIKA),
                DerivationTerm("suffix", "सु", TermKind.PRATYAYA)
            )
        )

        assertTrue(isDerivationEligible(820016, stateSingleTerm))
        assertTrue(isDerivationEligible(820016, stateWithActiveSuffix))
    }

    private fun isDerivationEligible(sutraKrama: Int, state: DerivationState): Boolean {
        val activeDomains = Ashtadhyayi.adhikaraSutras.filter { domain ->
            val role = domain.role as SutraRole.Adhikara
            val start = role.customStartKrama ?: domain.krama
            val end = role.endKrama
            sutraKrama in start..end
        }
        return activeDomains.all { domain ->
            domain.number in state.activeAdhikaras || (domain as? DerivationSutra)?.matches(state) == true
        }
    }
}
