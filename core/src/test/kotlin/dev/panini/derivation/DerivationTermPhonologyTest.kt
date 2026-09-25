package dev.panini.derivation

import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.Test
import kotlin.test.assertEquals

class DerivationTermPhonologyTest {
    @Test
    fun `term stores a parsed phonological form`() {
        val term = DerivationTerm("stem", "कवि", TermKind.PRATIPADIKA)

        assertEquals(listOf(Vyanjana.KA, Svara.A, Vyanjana.VA, Svara.I), term.varnas)
    }

    @Test
    fun `copy with a new surface receives new phonology`() {
        val original = DerivationTerm("stem", "कवि", TermKind.PRATIPADIKA)
        val changed = original.copy(surface = "कवे")

        assertEquals(Svara.I, original.varnas.last())
        assertEquals(Svara.E, changed.varnas.last())
    }
}
