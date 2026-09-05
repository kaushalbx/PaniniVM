package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.MidacoAntyatParahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ChutuSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ShahPratyayasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.CuradibhyoNicSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.ClehSicSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.CliLungiSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KryadibhyahShnaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatrsanacauSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.VerAprktasyaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.ShnasorAllopahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.ShnabhyastayorAtahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada4.IHalyaghohSutra
import dev.panini.ashtadhyayi.adhyaya7.pada2.AaneMukSutra
import dev.panini.ashtadhyayi.adhyaya7.pada1.YuvoranakauSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.ThasyaIkahSutra
import dev.panini.core.DhatuGana
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemainingAffixLifecycleTest {
    @Test
    fun `3 1 134 affixes begin raw and follow their explicit it lifecycles`() {
        var lyu = DerivationState(listOf(rawAffix("lyu", "ल्यु", "3.1.134")))
        lyu = LasakvataddhiteSutra.apply(lyu).state
        lyu = TasyaLopahSutra.apply(lyu).state
        assertEquals("यु", lyu.surface)
        lyu = YuvoranakauSutra.apply(lyu).state
        assertEquals("अन", lyu.surface)
        lyu.requireCompleteItProcessing()

        var nini = DerivationState(listOf(rawAffix("nini", "णिनि", "3.1.134")))
        nini = ChutuSutra.apply(nini).state
        nini = TasyaLopahSutra.apply(nini).state
        assertEquals("इनि", nini.surface)
        nini.requireCompleteItProcessing()

        var ac = DerivationState(listOf(rawAffix("ac", "अच्", "3.1.134")))
        ac = HalantyamSutra.apply(ac).state
        ac = TasyaLopahSutra.apply(ac).state
        assertEquals("अ", ac.surface)
        ac.requireCompleteItProcessing()
    }

    @Test
    fun `cli is deferred until 3 1 44 consumes it and introduces fresh sic`() {
        val root = DerivationTerm("dhatu", "भू", TermKind.DHATU)
        val lung = DerivationTerm("lung", "लुङ्", TermKind.PRATYAYA, upadesha = "लुङ्")
        var state = CliLungiSutra.apply(DerivationState(listOf(root, lung))).state
        val cli = state.terms.first { it.id == "cli" }
        assertEquals("3.1.43", cli.createdBySutra)
        assertEquals(ItProcessingPhase.DEFERRED_SUBSTITUTION, cli.itProcessingPhase)

        state = ClehSicSutra.apply(state).state
        val sic = state.terms.first { it.id == "cli" }
        assertEquals("सिँच्", sic.surface)
        assertEquals("3.1.44", sic.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, sic.itProcessingPhase)

        state = UpadesheAjanunasikaItSutra.apply(state).state
        state = ChutuSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        assertEquals("स्", state.terms.first { it.id == "cli" }.surface)
        state.requireCompleteItProcessing()
    }

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

    @Test
    fun `shnam designations survive placement and only their exact segments are deleted`() {
        val root = DerivationTerm("dhatu", "रुध्", TermKind.DHATU)
        val shnam = rawAffix("shnam", "श्नम्", "3.1.78").copy(augmentTargetId = root.id)
        val ending = DerivationTerm("ting-tas", "तस्", TermKind.PRATYAYA, upadesha = "तस्")
        var state = DerivationState(
            terms = listOf(root, shnam, ending),
            samjnas = setOf(SamjnaAssignment(ending.id, Samjna.SARVADHATUKA)),
        )

        state = LasakvataddhiteSutra.apply(state).state
        state = HalantyamSutra.apply(state).state
        assertEquals(
            listOf("1.3.8" to "श्", "1.3.3" to "म्"),
            state.terms.first { it.id == "shnam" }.itDesignations.map { it.sutra to it.designatedText },
        )
        state = MidacoAntyatParahSutra.apply(state).state
        val placed = state.terms.first { it.id == root.id }
        assertEquals("रुश्नम्ध्", placed.surface)
        assertEquals(listOf(2 until 4, 5 until 7), placed.itDesignations.map { it.start until it.endExclusive })

        state = TasyaLopahSutra.apply(state).state
        assertEquals("रुनध्", state.terms.first { it.id == root.id }.surface)
        state = ShnasorAllopahSutra.apply(state).state
        assertEquals("रुन्ध्", state.terms.first { it.id == root.id }.surface)
        state.requireCompleteItProcessing()
    }

    @Test
    fun `shna enters raw and its surviving vowel follows 6 4 112 and 6 4 113`() {
        val root = DerivationTerm("dhatu", "क्री", TermKind.DHATU, gana = DhatuGana.KRYADI)
        val selected = KryadibhyahShnaSutra.apply(
            DerivationState(listOf(root, DerivationTerm("ting-tip", "तिप्", TermKind.PRATYAYA, upadesha = "तिप्")))
        ).state
        val raw = selected.terms.first { it.id == "shna" }
        assertEquals("श्ना", raw.surface)
        assertEquals("3.1.81", raw.createdBySutra)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, raw.itProcessingPhase)

        fun processedShna(ending: DerivationTerm): DerivationState {
            var state = DerivationState(
                listOf(root, raw, ending),
                samjnas = setOf(SamjnaAssignment(ending.id, Samjna.SARVADHATUKA)),
            )
            state = LasakvataddhiteSutra.apply(state).state
            return TasyaLopahSutra.apply(state).state
        }

        val vowelInitial = processedShna(DerivationTerm("ting-jhi", "अन्ति", TermKind.PRATYAYA, upadesha = "झि"))
        assertEquals("ना", vowelInitial.terms.first { it.id == "shna" }.surface)
        val withLopa = ShnabhyastayorAtahSutra.apply(vowelInitial).state
        assertEquals("न्", withLopa.terms.first { it.id == "shna" }.surface)

        val consonantInitial = processedShna(DerivationTerm("ting-ta", "त", TermKind.PRATYAYA, upadesha = "त"))
        val withI = IHalyaghohSutra.apply(consonantInitial).state
        assertEquals("नी", withI.terms.first { it.id == "shna" }.surface)
        withI.requireCompleteItProcessing()
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
