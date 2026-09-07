package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.nio.file.Path
import kotlin.io.path.readText

class SvaraEngineTest {
    @Test
    fun `svara sutras are executable registry members and renderer contains no rule table`() {
        val rules = dev.panini.ashtadhyayi.Ashtadhyayi.executableSutrasAt(dev.panini.sutra.SutraStage.SVARA)
        assertEquals(setOf("3.1.3", "3.1.4", "6.1.158", "6.1.197"), rules.map { it.sutra }.toSet())

        val source = sequenceOf(
            Path.of("derivation", "src", "main", "kotlin", "dev", "panini", "derivation", "SvaraEngine.kt"),
            Path.of("..", "derivation", "src", "main", "kotlin", "dev", "panini", "derivation", "SvaraEngine.kt"),
        ).first { it.toFile().isFile }.readText()
        listOf("3.1.3", "3.1.4", "6.1.158", "6.1.197").forEach { assertFalse(it in source) }
    }


    @Test
    fun `test 6 1 158 single udatta rule for agni`() {
        val result = SvaraEngine.computeSvara("अग्नि", SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.EXPLICIT_UDATTA, "lexeme", vowelIndex = 0, lexicalSource = "lexicon:test"))))
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
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, "suffix", dev.panini.core.ItMarker.NIT, "1.3.3", 1))),
        )
        assertEquals(1, result.udattaVowelIndex, "6.1.197 prescribes the affix's initial udatta")
        assertEquals(AccentType.UDATTA, result.vowels[1].accent)
        assertTrue(result.rulesApplied.any { it.contains("6.1.197") })
    }

    @Test
    fun `test 3 1 4 anudatta for sup and pit affixes`() {
        val result = SvaraEngine.computeSvara(
            "अग्निना",
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.PIT_OR_SUP, "sup", vowelIndex = 2))),
        )
        assertTrue(result.rulesApplied.any { it.contains("3.1.4") })
    }

    @Test
    fun `test formatted devanagari contains anudatta underbar`() {
        val result = SvaraEngine.computeSvara(
            "अग्नि",
            SvaraContext(listOf(SvaraTrigger(SvaraTriggerKind.NIT_OR_NGIT, "suffix", dev.panini.core.ItMarker.NIT, "1.3.3", 1))),
        )
        assertTrue(result.formattedDevanagari.contains("\u0952"), "Anudatta vowel must be formatted with underbar \\u0952")
    }

    @Test
    fun `test derivation result automatically contains svaraResult`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("pratipadika", "अग्नि", TermKind.PRATIPADIKA, upadesha = "अग्नि"),
                DerivationTerm("pratyaya", "अ", TermKind.PRATYAYA, upadesha = "अ"),
            ),
            stage = DerivationStage.INITIAL
        )
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(state)
        val svaraResult = result.svaraResult
        kotlin.test.assertNotNull(svaraResult, "DerivationResult must automatically attach svaraResult")
        assertTrue(svaraResult.rulesApplied.any { it.contains("6.1.158") })
        assertTrue(result.applications.any { it.sutra == "3.1.3" })
        assertTrue(result.applications.any { it.sutra == "6.1.158" })
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

        assertEquals("1.3.3", context.triggers.single { it.kind == SvaraTriggerKind.NIT_OR_NGIT }.designationSutra)
        assertFalse(context.triggers.any { it.kind == SvaraTriggerKind.PIT_OR_SUP })
    }

    @Test
    fun `vowel loci include inherent a but not virama consonants`() {
        assertEquals(listOf(0, 3), DevanagariVowelLoci.positions("गर्ग"))
        assertTrue(DevanagariVowelLoci.positions("क्त्").isEmpty())
        val consonantOnly = DerivationState(listOf(DerivationTerm("pada", "क्त्", TermKind.PRATIPADIKA)))
        assertEquals(null, SvaraEngine.derive(consonantOnly).result)
    }

    @Test
    fun `lexical udatta keeps lexical source and only 6 1 158 is traced`() {
        val state = DerivationState(listOf(DerivationTerm(
            "dhatu", "भू", TermKind.DHATU,
            lexicalAccent = dev.panini.shiksha.Accent.UDATTA,
            lexicalAccentSource = "Dhātupāṭha:test",
        )))
        val result = SvaraEngine.derive(state)
        assertEquals(SvaraAssignmentSource.Lexical("Dhātupāṭha:test"), result.state.svaraAssignments.single().source)
        assertFalse(result.applications.any { it.sutra == "3.1.3" })
    }
}
