package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
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
        val result = SvaraEngine.computeSvara("गार्ग्य", isNitOrNnit = true)
        assertEquals(0, result.udattaVowelIndex, "6.1.197 prescribes initial udatta for nnit affixes")
        assertEquals(AccentType.UDATTA, result.vowels[0].accent)
        assertTrue(result.rulesApplied.any { it.contains("6.1.197") })
    }

    @Test
    fun `test 3 1 4 anudatta for sup and pit affixes`() {
        val result = SvaraEngine.computeSvara("अग्निना", isPitOrSup = true)
        assertTrue(result.rulesApplied.any { it.contains("3.1.4") })
    }

    @Test
    fun `test formatted devanagari contains anudatta underbar`() {
        val result = SvaraEngine.computeSvara("अग्नि", isNitOrNnit = true)
        assertTrue(result.formattedDevanagari.contains("\u0952"), "Anudatta vowel must be formatted with underbar \\u0952")
    }
}
