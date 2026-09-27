package dev.panini.derivation

import dev.panini.shiksha.OrthographicSign
import dev.panini.shiksha.OrthographicSignPlacement
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import kotlin.test.Test
import kotlin.test.assertEquals

class PhonologicalMutationInvariantTest {
    @Test
    fun `copying a term with a new surface creates a synchronized varna cache`() {
        val original = DerivationTerm("stem", "नी", TermKind.DHATU)
        assertEquals(listOf(Vyanjana.NA, Svara.II), original.varnas)

        val changed = original.copy(surface = "नय")

        assertEquals(listOf(Vyanjana.NA, Svara.A, Vyanjana.YA, Svara.A), changed.varnas)
        assertEquals(changed.surface, changed.varnas.toDevanagari())
    }

    @Test
    fun `length changing substitution clamps orthographic signs to the new boundary`() {
        val sign = OrthographicSignPlacement(OrthographicSign.AVAGRAHA, afterVarnaCount = 4)
        val term = DerivationTerm(
            "stem", "कवेऽ", TermKind.PRATIPADIKA,
            orthographicSigns = listOf(sign),
        )

        val result = DerivationState(listOf(term)).substituteTermVarnas(
            "stem", listOf(Vyanjana.KA, Svara.A), Vyanjana.VA, emptyList(), "test",
        ).terms.single()

        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), result.orthographicSigns)
        assertEquals("कऽ", result.surface)
        assertEquals(listOf(Vyanjana.KA, Svara.A), result.varnas)
    }

    @Test
    fun `equal length substitution preserves vowel token metadata`() {
        val term = DerivationTerm("stem", "कँ", TermKind.DHATU)

        val result = DerivationState(listOf(term)).substituteTermVarnas(
            "stem", listOf(Vyanjana.KA, Svara.AA), Svara.A, listOf(Svara.AA), "test",
        ).terms.single()

        assertEquals("काँ", result.surface)
        assertEquals(true, result.phonologicalText.effectiveVarnas.last().nasalized)
    }

    @Test
    fun `substitution trace retains exact written state and orthographic signs for rollback`() {
        val signs = listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2))
        val term = DerivationTerm("stem", "कऽ", TermKind.PRATIPADIKA, orthographicSigns = signs)

        val result = DerivationState(listOf(term)).substituteTermVarnas(
            "stem", listOf(Vyanjana.KA, Svara.AA), Svara.A, listOf(Svara.AA), "6.4.1",
        )

        assertEquals("कऽ", result.substitutions.single().originalSurface)
        assertEquals(signs, result.substitutions.single().originalOrthographicSigns)
    }
}
