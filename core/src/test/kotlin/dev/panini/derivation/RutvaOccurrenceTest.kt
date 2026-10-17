package dev.panini.derivation

import dev.panini.core.ItMarker
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class RutvaOccurrenceTest {
    private val origin = RutvaOccurrence(1, "8.2.66")
    private fun term() = DerivationTerm("word", "अर्अर्", TermKind.PRATIPADIKA,
        rutvaOccurrences = listOf(origin))

    @Test fun `range projection distinguishes ru from another native repha`() {
        assertEquals(listOf(origin.copy(varnaIndex = 2)), term().projectRutvaOccurrences(0, 0, listOf(Svara.A)))
        assertEquals(listOf(origin.copy(varnaIndex = 0)), term().projectRutvaOccurrences(0, 1, emptyList()))
        assertEquals(listOf(origin), term().projectRutvaOccurrences(3, 1, listOf(Vyanjana.YA)))
        assertEquals(emptyList(), term().projectRutvaOccurrences(1, 1, listOf(Vyanjana.YA)))
        assertEquals(emptyList(), term().projectRutvaOccurrences(1, 1, listOf(Svara.U)))
        assertEquals(listOf(origin.copy(varnaIndex = 0)),
            term().projectRutvaOccurrences(0, 2, listOf(Vyanjana.RA), mapOf(0 to 1)))
        assertEquals(listOf(origin), term().projectRutvaOccurrences(1, 1, listOf(Ayogavaha.VISARGA)))
        assertEquals(listOf(origin), term().projectRutvaOccurrences(1, 1, listOf(Ayogavaha.JIHVAMULIYA)))
        assertEquals(listOf(origin), term().projectRutvaOccurrences(1, 1, listOf(Ayogavaha.UPADHMANIYA)))
    }

    @Test fun `composition projects both carriers independently`() {
        val inserted = DerivationTerm("inserted", "अर्", TermKind.AGAMA,
            rutvaOccurrences = listOf(RutvaOccurrence(1, "8.3.7")))
        val result = term().insertDesignatedTerm(inserted, 1)
        assertEquals(listOf(origin.copy(varnaIndex = 3), RutvaOccurrence(2, "8.3.7")), result.rutvaOccurrences)
        assertEquals(listOf(Vyanjana.RA, Vyanjana.RA), result.rutvaOccurrences.map { result.varnas[it.varnaIndex] })
    }

    @Test fun `exact it deletion moves ru without transferring it to a native repha`() {
        val raw = DerivationTerm("word", "उर्अर्", TermKind.PRATIPADIKA, rutvaOccurrences = listOf(origin))
        val designation = raw.designateVarnaIt(0, ItMarker.U, "1.3.2")
        val result = raw.lopaOfDesignatedVarnas(listOf(designation))
        assertEquals(listOf(origin.copy(varnaIndex = 0)), result.rutvaOccurrences)
        assertEquals(listOf(Vyanjana.RA, Svara.A, Vyanjana.RA), result.varnas)
    }

    @Test fun `stale occurrence fails rather than silently marking another sound`() {
        assertFailsWith<IllegalArgumentException> {
            term().copy(surface = "अक्अर्").projectRutvaOccurrences(0, 0, emptyList())
        }
    }

    @Test fun `positional state operations project exact origins and consume only the replaced ru`() {
        var state = DerivationState(listOf(term()))
        state = state.insertTermVarnas("word", 0, listOf(Svara.I), "fixture-insert")
        assertEquals(listOf(origin.copy(varnaIndex = 2)), state.terms.single().rutvaOccurrences)
        state = state.deleteTermVarnas("word", 0, 1, "fixture-delete")
        assertEquals(listOf(origin), state.terms.single().rutvaOccurrences)
        state = state.replaceTermVarna("word", 3, listOf(Vyanjana.YA), "fixture-native-r")
        assertEquals(listOf(origin), state.terms.single().rutvaOccurrences)
        state = state.replaceTermVarna("word", 1, listOf(Vyanjana.YA), "fixture-ru")
        assertTrue(state.terms.single().rutvaOccurrences.isEmpty())
    }

    @Test fun `range and grouped deletion project occurrences without size-based guessing`() {
        val initial = DerivationState(listOf(term()))
        val expanded = initial.replaceTermVarnaRange("word", 0, 1, listOf(Svara.A, Svara.A),
            annotationSources = mapOf(0 to 0), sutra = "fixture-range")
        assertEquals(listOf(origin.copy(varnaIndex = 2)), expanded.terms.single().rutvaOccurrences)
        val deleted = expanded.deleteTermVarnaOccurrences("word", setOf(0, 1), "fixture-group-delete")
        assertEquals(listOf(origin.copy(varnaIndex = 0)), deleted.terms.single().rutvaOccurrences)
        val grouped = initial.replaceTermVarnaOccurrences("word", mapOf(1 to Vyanjana.YA, 3 to Vyanjana.RA), "fixture-group")
        assertTrue(grouped.terms.single().rutvaOccurrences.isEmpty())
    }

    @Test fun `whole replacements discard old ru but explicit occurrence mapping preserves it`() {
        val affix = term().copy(kind = TermKind.PRATYAYA)
        for (policy in listOf(WholeAffixDesignationPolicy.Consume,
            WholeAffixDesignationPolicy.FreshUpadesha,
            WholeAffixDesignationPolicy.PreserveAndRemap(emptyList()))) {
            assertTrue(affix.replaceWholeAffix("अर्", "new", "fixture", policy).rutvaOccurrences.isEmpty())
        }
        val mapped = affix.replaceWholeAffixWithVarnaMapping(listOf(Vyanjana.RA), mapOf(1 to 0), emptySet(), "fixture-map")
        assertEquals(listOf(origin.copy(varnaIndex = 0)), mapped.rutvaOccurrences)
        val consumed = affix.replaceWholeAffixWithVarnaMapping(listOf(Vyanjana.YA), mapOf(1 to 0), emptySet(), "fixture-map")
        assertTrue(consumed.rutvaOccurrences.isEmpty())
        val lexical = DerivationState(listOf(term())).replaceWholeTermSurface("word", "अर्इ", "fixture-lexical")
        assertTrue(lexical.terms.single().rutvaOccurrences.isEmpty())
    }

    @Test fun `drop retains historical occurrences but no operative ru on empty terms`() {
        for (kind in listOf(TermKind.PRATIPADIKA, TermKind.PRATYAYA, TermKind.AGAMA)) {
            val input = term().copy(kind = kind)
            val survivor = DerivationTerm("survivor", "अ", TermKind.PRATIPADIKA)
            val dropped = DerivationState(listOf(input, survivor)).removeTerm(input.id, "fixture-drop").droppedTerms.single()
            assertEquals(input.surface, dropped.originalSurfaceBeforeDrop)
            assertEquals(input.rutvaOccurrences, dropped.originalRutvaOccurrencesBeforeDrop)
            assertTrue(dropped.rutvaOccurrences.isEmpty())
            assertTrue(dropped.varnas.isEmpty())
        }
    }

    @Test fun `prefix transfer moves only its exact ru occurrences between carriers`() {
        val left = DerivationTerm("left", "अर्", TermKind.PRATIPADIKA, rutvaOccurrences = listOf(origin))
        val right = term()
        val moved = DerivationState(listOf(left, right)).transferFollowingPrefix(left.id, right.id, 2, "fixture-transfer")
        assertEquals(listOf(origin, origin.copy(varnaIndex = 3)), moved.terms.first().rutvaOccurrences)
        assertTrue(moved.terms.last().rutvaOccurrences.isEmpty())
        assertEquals(listOf(Svara.A, Vyanjana.RA), moved.terms.last().varnas)
    }

    @Test fun `initial vowel coalescence preserves following ru and archives original positions`() {
        val left = DerivationTerm("left", "आ", TermKind.PRATIPADIKA)
        val right = term()
        val changed = DerivationState(listOf(left, right)).deleteTermVarnas(right.id, 0, 1, "fixture-coalescence")
        val result = changed.concatenateAfterInitialVowelCoalescence(left.id, right, "fixture-coalescence")
        assertEquals(listOf(origin), result.terms.single().rutvaOccurrences)
        assertEquals(right.rutvaOccurrences, result.droppedTerms.single().originalRutvaOccurrencesBeforeDrop)
        assertEquals(right.surface, result.droppedTerms.single().originalSurfaceBeforeDrop)
        assertTrue(result.droppedTerms.single().rutvaOccurrences.isEmpty())
    }
}
