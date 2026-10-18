package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya3.pada1.ChinKarmaniChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NiShriDruSruBhyahKartariChangSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PusAdiDyutAdyLrditahParasmaipadesuSutra
import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LungAoristSutrasTest {

    @Test
    fun `3 1 48 substitutes cang for cli after named roots`() {
        assertEquals(NiShriDruSruBhyahKartariChangSutra, Ashtadhyayi.requireExecutable("3.1.48"))
        for (upadesha in listOf("श्रिञ्", "द्रु", "स्रु")) {
            val state = DerivationState(
                terms = listOf(
                    DerivationTerm("root", upadesha.removeSuffix("ञ्"), TermKind.DHATU, upadesha = upadesha),
                    DerivationTerm("cli", "च्लि", TermKind.PRATYAYA, upadesha = "च्लि",
                        createdBySutra = "3.1.43", itProcessingPhase = ItProcessingPhase.DEFERRED_SUBSTITUTION),
                ),
                context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARTARI, lakara = Lakara.LUNG)),
            )
            val cang = NiShriDruSruBhyahKartariChangSutra.apply(state).state.terms.single { it.id == "cli" }
            assertEquals("चङ्", cang.upadesha)
            assertEquals("3.1.48", cang.createdBySutra)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, cang.itProcessingPhase)
            assertEquals("च्लि", cang.sthaniProps?.upadesha)
        }
    }

    @Test
    fun `3 1 48 recognizes nic provenance but not the ni preverb`() {
        fun state(extra: DerivationTerm) = DerivationState(
            terms = listOf(
                DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "डुकृञ्"), extra,
                DerivationTerm("cli", "च्लि", TermKind.PRATYAYA, upadesha = "च्लि",
                    createdBySutra = "3.1.43", itProcessingPhase = ItProcessingPhase.DEFERRED_SUBSTITUTION),
            ),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARTARI, lakara = Lakara.LUNG)),
        )
        val nic = DerivationTerm("nic", "इ", TermKind.PRATYAYA, upadesha = "णिच्", createdBySutra = "3.1.26")
        assertTrue(NiShriDruSruBhyahKartariChangSutra.matches(state(nic)))
        val ni = DerivationTerm("ni", "नि", TermKind.PRATIPADIKA, upadesha = "नि")
        assertFalse(NiShriDruSruBhyahKartariChangSutra.matches(state(ni)))
    }

    @Test
    fun `3 1 48 requires kartari and the cli locus`() {
        val root = DerivationTerm("root", "स्रु", TermKind.DHATU, upadesha = "स्रु")
        val cli = DerivationTerm("cli", "च्लि", TermKind.PRATYAYA, upadesha = "च्लि")
        val karmani = DerivationState(listOf(root, cli),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARMANI, lakara = Lakara.LUNG)))
        assertFalse(NiShriDruSruBhyahKartariChangSutra.matches(karmani))
        assertFalse(NiShriDruSruBhyahKartariChangSutra.matches(karmani.copy(
            terms = listOf(root), context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARTARI, lakara = Lakara.LUNG)))))
    }

    @Test
    fun `3 1 55 recognizes both source subgroups and lrdit provenance`() {
        fun state(root: DerivationTerm, pada: PadaType = PadaType.PARASMAIPADA) = DerivationState(
            terms = listOf(root, DerivationTerm("cli", "च्लि", TermKind.PRATYAYA, upadesha = "च्लि",
                createdBySutra = "3.1.43", itProcessingPhase = ItProcessingPhase.DEFERRED_SUBSTITUTION)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUNG, pada = pada)),
        )
        val positives = listOf(
            DerivationTerm("pus", "पुष्", TermKind.DHATU, upadesha = "पुषँ", gana = DhatuGana.DIVADI,
                sourceDhatuId = "04.0079", sourceDhatuKrama = 79),
            DerivationTerm("dyut", "द्युत्", TermKind.DHATU, upadesha = "द्युतँ", gana = DhatuGana.BHVADI,
                sourceDhatuId = "01.0842", sourceDhatuKrama = 842),
            DerivationTerm("gam", "गम्", TermKind.DHATU, upadesha = "गमॢँ", gana = DhatuGana.BHVADI,
                sourceDhatuId = "01.1137", sourceDhatuKrama = 1137),
        )
        for (root in positives) {
            assertTrue(PusAdiDyutAdyLrditahParasmaipadesuSutra.matches(state(root)))
            val ang = PusAdiDyutAdyLrditahParasmaipadesuSutra.apply(state(root)).state.terms.single { it.id == "cli" }
            assertEquals("अङ्", ang.upadesha)
            assertEquals("च्लि", ang.sthaniProps?.upadesha)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, ang.itProcessingPhase)
        }
        assertEquals(PusAdiDyutAdyLrditahParasmaipadesuSutra, Ashtadhyayi.requireExecutable("3.1.55"))

        val wrongPus = positives[0].copy(gana = DhatuGana.BHVADI, sourceDhatuId = "01.0808", sourceDhatuKrama = 808)
        val outsideDyutadi = positives[1].copy(sourceDhatuId = "01.0867", sourceDhatuKrama = 867)
        assertFalse(PusAdiDyutAdyLrditahParasmaipadesuSutra.matches(state(wrongPus)))
        assertFalse(PusAdiDyutAdyLrditahParasmaipadesuSutra.matches(state(outsideDyutadi)))
        assertFalse(PusAdiDyutAdyLrditahParasmaipadesuSutra.matches(state(positives[0], PadaType.ATMANEPADA)))
    }

    @Test
    fun `derives ciṇ vikaraṇa in Luṅ karmaṇi via ChinKarmaniChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARMANI, lakara = Lakara.LUNG))
        )
        val result = DerivationEngine(listOf(ChinKarmaniChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.60" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "चिण्" })
    }
}
