package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.NashcapadantasyaSutra
import dev.panini.shiksha.*
import dev.panini.core.*
import kotlin.test.*

class InternalAnusvaraAffixDomainTest {
    private fun state() = DerivationState(listOf(
        DerivationTerm("stem", "मनान्स्", TermKind.PRATIPADIKA),
        SupAffix.JAS.term("suffix").copy(surface = "इ", upadesha = "शि", itProcessingPhase = ItProcessingPhase.PROCESSED),
    ))

    @Test fun `internal nasal before s changes with a retained processed suffix`() {
        val before = state()
        assertTrue(NashcapadantasyaSutra.matches(before))
        val after = NashcapadantasyaSutra.apply(before).state
        assertEquals(listOf(Vyanjana.MA, Svara.A, Vyanjana.NA, Svara.AA, Ayogavaha.ANUSVARA, Vyanjana.SA), after.terms.first().varnas)
        assertEquals(before.terms.last(), after.terms.last())
        assertEquals(before.terms.map { it.id }, after.terms.map { it.id })
        assertEquals(SupAffix.JAS, after.terms.last().sourceSupAffix)
        assertFalse(NashcapadantasyaSutra.matches(after))
    }

    @Test fun `unfinished affix processing remains protected`() {
        val before = state()
        assertFalse(NashcapadantasyaSutra.matches(before.replaceTerm("suffix",
            before.terms.last().copy(surface = "शि", itProcessingPhase = ItProcessingPhase.RAW_UPADESHA))))
    }

    @Test fun `an affix boundary is not automatically a pada boundary`() {
        val before = state().replaceTerm("stem", DerivationTerm("stem", "मनन्", TermKind.PRATIPADIKA))
            .replaceTerm("suffix", state().terms.last().copy(surface = "स्"))
        assertTrue(NashcapadantasyaSutra.matches(before))
        assertFalse(NashcapadantasyaSutra.matches(before.withSamjnas(setOf(SamjnaAssignment("stem", Samjna.PADA)))))
        assertFalse(NashcapadantasyaSutra.matches(before.replaceTerm("suffix", before.terms.last().copy(surface = "य"))))
    }

    @Test fun `neuter plural trace records num lengthening then internal anusvara`() {
        for ((stem, expected) in listOf("मनस्" to "मनांसि", "पयस्" to "पयांसि", "यशस्" to "यशांसि")) {
            for (vibhakti in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
                val result = SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.BAHUVACANA, Linga.NAPUMSAKA))
                assertEquals(expected, result.final.surface)
                val trace = result.applications.map { it.sutra }
                assertTrue(trace.indexOf("7.1.72") >= 0)
                assertTrue(trace.indexOf("8.3.24") > trace.indexOf("7.1.72"))
                assertEquals(1, trace.count { it == "8.3.24" })
            }
        }
    }
}
