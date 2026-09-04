package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ChutuSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ShahPratyayasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.CuradibhyoNicSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatrsanacauSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.VerAprktasyaSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AaneMukSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.ThasyaIkahSutra
import dev.panini.core.DhatuGana
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemainingAffixLifecycleTest {
    @Test
    fun `kvip is designated exactly before 6 1 67 consumes its processed vi`() {
        var state = DerivationState(listOf(rawAffix("kvip", "क्विप्", "3.2.61")))
        state = LasakvataddhiteSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        assertEquals(setOf("1.3.8", "1.3.3"), state.terms.single().itDesignations.map { it.sutra }.toSet())
        state = TasyaLopahSutra.apply(state).state
        assertEquals("वि", state.surface)
        state = VerAprktasyaSutra.apply(state).state
        assertEquals("", state.surface)
        state.requireCompleteItProcessing()
    }

    @Test
    fun `tha families reach ika only after exact halantyam processing`() {
        for (upadesha in listOf("ठक्", "ठच्", "ष्ठन्")) {
            var state = DerivationState(listOf(rawAffix("suffix", upadesha, "4.1.1")))
            if (LasakvataddhiteSutra.matches(state)) state = LasakvataddhiteSutra.apply(state).state
            if (ShahPratyayasyaSutra.matches(state)) state = ShahPratyayasyaSutra.apply(state).state
            state = HalantyamSutra.apply(state).state
            assertTrue(state.terms.single().itDesignations.any { it.sutra == "1.3.3" })
            state = TasyaLopahSutra.apply(state).state
            state = ThasyaIkahSutra.apply(state).state
            assertEquals("इक", state.surface)
            state.requireCompleteItProcessing()
        }
    }

    @Test
    fun `nic introduction retains its sutra and completes exact it processing`() {
        val initial = DerivationState(listOf(
            DerivationTerm("dhatu", "चुर्", TermKind.DHATU, gana = DhatuGana.CURADI),
            DerivationTerm("ting-tip", "तिप्", TermKind.PRATYAYA, upadesha = "तिप्"),
        ))
        var state = CuradibhyoNicSutra.apply(initial).state
        val raw = state.terms.first { it.id == "nic" }
        assertEquals("3.1.25", raw.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)
        state = ChutuSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals("इ", state.terms.first { it.id == "nic" }.surface)
        state.requireCompleteItProcessing()
    }

    @Test
    fun `satr and sanac enter as raw upadeshas with introducing provenance`() {
        val root = DerivationTerm("dhatu", "लभ्", TermKind.DHATU)
        val satrState = DerivationState(listOf(root), samjnas = setOf(SamjnaAssignment(root.id, Samjna.SATR)))
        val rawSatr = LatahSatrsanacauSutra.apply(satrState).state.terms.last()
        assertEquals("शतृँ", rawSatr.surface)
        assertEquals("3.2.124", rawSatr.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, rawSatr.itProcessingPhase)

        val sanacState = DerivationState(listOf(root), samjnas = setOf(SamjnaAssignment(root.id, Samjna.SANAC)))
        var state = LatahSatrsanacauSutra.apply(sanacState).state
        assertEquals("शानच्", state.terms.last().surface)
        state = LasakvataddhiteSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals("आन", state.terms.last().surface)

        state = AaneMukSutra.apply(state).state
        assertEquals("7.2.82", state.terms.last().createdBySutra)
        state = UpadesheAjanunasikaItSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        state = AdyantauTakitauSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals("मान", DerivationState(state.terms.drop(1)).surface)
        state.requireCompleteItProcessing()
    }

    private fun rawAffix(id: String, upadesha: String, source: String) = DerivationTerm(
        id = id,
        surface = upadesha,
        kind = TermKind.PRATYAYA,
        upadesha = upadesha,
        createdBySutra = source,
        itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
    )
}
