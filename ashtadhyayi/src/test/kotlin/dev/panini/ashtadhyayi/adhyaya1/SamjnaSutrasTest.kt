package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.KtaKtavatuNisthaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TaparasTatKalasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.ArthavadAdhaturSutra
import dev.panini.ashtadhyayi.adhyaya1.pada2.KrtTaddhitaSamasascaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.BhuvadayoDhatavahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.AdirNitudavahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ChutuSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.LasakvataddhiteSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.ShahPratyayasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class SamjnaSutrasTest {

    @Test
    fun testKtaKtavatuNisthaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("affix", "क्त", TermKind.PRATYAYA, upadesha = "क्त")))
        assertTrue(KtaKtavatuNisthaSutra.matches(state))
    }

    @Test
    fun testTaparasTatKalasyaSutra() {
        assertTrue(TaparasTatKalasyaSutra.matches("अत्"))
        assertTrue(TaparasTatKalasyaSutra.apply("अत्"))
    }

    @Test
    fun testArthavadAdhaturSutra() {
        val state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(DerivationTerm(id = "stem", surface = "राम", kind = TermKind.PRATIPADIKA))
        )
        assertTrue(ArthavadAdhaturSutra.matches(state))
    }

    @Test
    fun testKrtTaddhitaSamasascaSutra() {
        val state = DerivationState(terms = listOf(DerivationTerm("affix", "क्त", TermKind.PRATYAYA, upadesha = "क्त")))
        assertTrue(KrtTaddhitaSamasascaSutra.matches(state))
    }

    @Test
    fun testBhuvadayoDhatavahSutra() {
        val state = DerivationState(
            stage = DerivationStage.INITIAL,
            terms = listOf(DerivationTerm(id = "dhatu", surface = "भू", kind = TermKind.DHATU))
        )
        assertTrue(BhuvadayoDhatavahSutra.matches(state))
    }

    @Test
    fun testUpadesheAjanunasikaItSutra() {
        val state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(DerivationTerm(id = "pratyaya", surface = "सुँ", kind = TermKind.PRATYAYA))
        )
        assertTrue(UpadesheAjanunasikaItSutra.matches(state))
    }

    @Test
    fun testHalantyamSutra() {
        val state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(DerivationTerm(id = "pratyaya", surface = "ल्युट्", kind = TermKind.PRATYAYA, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA))
        )
        assertTrue(HalantyamSutra.matches(state))
    }

    @Test
    fun testLasakvataddhiteSutra() {
        val state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(DerivationTerm(id = "pratyaya", surface = "ल्युट्", kind = TermKind.PRATYAYA))
        )
        assertTrue(LasakvataddhiteSutra.matches(state))
    }

    @Test
    fun `lasakvataddhite preserves the exact initial marker class`() {
        val expected = mapOf(
            "ल्युट्" to ItMarker.LIT,
            "शप्" to ItMarker.SH,
            "कल्पित" to ItMarker.KIT,
            "खल्पित" to ItMarker.KHIT,
            "गल्पित" to ItMarker.GIT,
            "घल्पित" to ItMarker.GHIT,
            "ङल्पित" to ItMarker.NGIT,
        )

        expected.forEach { (upadesha, marker) ->
            val state = DerivationState(
                stage = DerivationStage.PRATYAYA_SELECTED,
                terms = listOf(DerivationTerm("pratyaya", upadesha, TermKind.PRATYAYA)),
            )
            val designated = LasakvataddhiteSutra.apply(state).state.terms.single().deferredItDesignations.single()
            assertEquals(marker, designated.marker, upadesha)
            assertEquals(upadesha.take(if (upadesha.getOrNull(1) == '्') 2 else 1), designated.designatedText)
            assertEquals("1.3.8", designated.sutra)
        }
    }

    @Test
    fun testTasyaLopahSutra() {
        val state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(DerivationTerm(id = "pratyaya", surface = "अ", kind = TermKind.PRATYAYA, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA))
        )
        assertFalse(TasyaLopahSutra.matches(state))
        val result = dev.panini.derivation.DerivationEngine(
            listOf(
                AdirNitudavahSutra, ShahPratyayasyaSutra, ChutuSutra,
                UpadesheAjanunasikaItSutra, HalantyamSutra, LasakvataddhiteSutra,
                TasyaLopahSutra,
            ),
        ).derive(state)
        val completed = result.final.terms.single()
        assertEquals(dev.panini.derivation.ItProcessingPhase.PROCESSED, completed.itProcessingPhase)
        assertEquals("अ", completed.surface)
        assertTrue(result.applications.none { it.sutra == "IT-COMPLETE" })
    }

    @Test
    fun `it rules delete only their designated sounds from raw upadeshas`() {
        assertEquals("ई", process("ङीप्", useInitial = LasakvataddhiteSutra))
        assertEquals("आ", process("टाप्", useInitial = ChutuSutra))
        assertEquals("अ", process("अण्"))
        assertEquals("अ", process("णल्", useInitial = ChutuSutra))
        assertEquals("थ", process("थल्"))
        assertEquals("आ", process("आट्", kind = TermKind.AGAMA))
        assertEquals("कृ", process("डुकृञ्", kind = TermKind.DHATU, useInitial = AdirNitudavahSutra))
        assertEquals("भू", process("ञिभू", kind = TermKind.DHATU, useInitial = AdirNitudavahSutra))
        assertEquals("अक", process("षक", useInitial = ShahPratyayasyaSutra))
        assertEquals("क", process("ष्क", useInitial = ShahPratyayasyaSutra))
        assertEquals("य", process("ण्यत्", useInitial = ChutuSutra))
        assertEquals("य", process("क्यच्", useInitial = LasakvataddhiteSutra))
        assertEquals("त", process("क्त", useInitial = LasakvataddhiteSutra))
        assertEquals("आन", process("कानच्", useInitial = LasakvataddhiteSutra))
        assertEquals("तुम्", process("तुमुँन्"))
        assertEquals("अम्", process("णमुँल्", useInitial = ChutuSutra))
        assertEquals("तवत्", process("क्तवतुँ", useInitial = LasakvataddhiteSutra))
        assertEquals("वस्", process("क्वसुँ", useInitial = LasakvataddhiteSutra))
    }

    private fun process(
        upadesha: String,
        useInitial: dev.panini.derivation.DerivationSutra? = null,
        kind: TermKind = TermKind.PRATYAYA,
    ): String {
        var state = DerivationState(
            stage = DerivationStage.PRATYAYA_SELECTED,
            terms = listOf(
                DerivationTerm(
                    id = "raw",
                    surface = upadesha,
                    kind = kind,
                    upadesha = upadesha,
                    itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
                ),
            ),
        )
        useInitial?.let { state = it.apply(state).state }
        if (UpadesheAjanunasikaItSutra.matches(state)) state = UpadesheAjanunasikaItSutra.apply(state).state
        if (HalantyamSutra.matches(state)) state = HalantyamSutra.apply(state).state
        state = TasyaLopahSutra.apply(state).state
        return state.terms.single().surface
    }
}
