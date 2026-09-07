package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SvaraEngineTest {

    @Test
    fun `test 6 1 158 single udatta rule for agni`() {
        val result = SvaraEngine.computeSvara("अग्नि")
        assertEquals(2, result.vowels.size, "agni should have 2 vowels")
        val udattaVowels = result.vowels.filter { it.accent == AccentType.UDATTA }
        assertEquals(1, udattaVowels.size, "Rule 6.1.158 dictates exactly 1 udatta per word")
        val anudattaVowels = result.vowels.filter { it.accent == AccentType.ANUDATTA }
        assertEquals(1, anudattaVowels.size, "Remaining vowels are anudatta")
        assertTrue(result.rulesApplied.any { it.contains("6.1.158") })
    }

    @Test
    fun `test 6 1 197 adyudatta for nnit affixes`() {
        val result = SvaraEngine.computeSvara(
            "गार्ग्य",
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, "suffix", dev.panini.core.ItMarker.NIT, "1.3.3"))),
        )
        assertEquals(0, result.udattaVowelIndex, "6.1.197 prescribes initial udatta for nnit affixes")
        assertEquals(AccentType.UDATTA, result.vowels[0].accent)
        assertTrue(result.rulesApplied.any { it.contains("6.1.197") })
    }

    @Test
    fun `test 3 1 4 anudatta for sup and pit affixes`() {
        val result = SvaraEngine.computeSvara(
            "अग्निना",
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, "sup"))),
        )
        assertTrue(result.rulesApplied.any { it.contains("3.1.4") })
    }

    @Test
    fun `test formatted devanagari contains anudatta underbar`() {
        val result = SvaraEngine.computeSvara(
            "अग्नि",
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, "suffix", dev.panini.core.ItMarker.NIT, "1.3.3"))),
        )
        assertTrue(result.formattedDevanagari.contains("\u0952"), "Anudatta vowel must be formatted with underbar \\u0952")
    }

    @Test
    fun `test derivation result automatically contains svaraResult`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "अग्नि", TermKind.PRATIPADIKA, upadesha = "अग्नि")),
            stage = DerivationStage.INITIAL
        )
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(state)
        val svaraResult = result.svaraResult
        kotlin.test.assertNotNull(svaraResult, "DerivationResult must automatically attach svaraResult")
        assertTrue(svaraResult.rulesApplied.any { it.contains("6.1.158") })
    }

    @Test
    fun `svara context carries exact designation provenance and does not treat arbitrary pratyaya as sup`() {
        val arbitrary = DerivationState(
            terms = listOf(
                DerivationTerm(
                    "krt", "अ", TermKind.PRATYAYA,
                    upadesha = "अ",
                    itMarkerProvenance = setOf(ItMarkerProvenance(dev.panini.core.ItMarker.NIT, "1.3.3", "ण्")),
                ),
            ),
        )

        val context = SvaraContext.from(arbitrary)

        assertEquals("1.3.3", context.triggers.single().designationSutra)
        assertFalse(context.triggers.any { it.kind == SvaraTriggerKind.PIT_OR_SUP })
    }
}
