package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EcoYavayavahSutra
import dev.panini.shiksha.*
import dev.panini.core.Lakara
import dev.panini.vyakaranam.ast.Vikarana
import kotlin.test.*

class EcExpansionAnnotationTest {
    @Test fun `lot ending guard is independent of nasal annotation`() {
        for (ending in listOf("एते", "आते", "अन्ते", "अते", "एथे", "आथे")) {
            for (surface in listOf(ending, ending + "ँ")) {
                val state = DerivationState(listOf(
                    DerivationTerm("left", "ए", TermKind.PRATIPADIKA),
                    DerivationTerm("right", surface, TermKind.PRATYAYA),
                ), stage = DerivationStage.PADA_FORMED,
                    context = DerivationalContext(rupa = Rupa(lakara = Lakara.LOT)))
                assertFalse(EcoYavayavahSutra.matches(state), surface)
                assertTrue(EcoYavayavahSutra.matches(state.copy(appliedSutras = listOf("3.4.90"))), surface)
            }
        }
    }

    @Test fun `future stem guard requires a vikarana affix rather than a spelling lookalike`() {
        for (kind in listOf(TermKind.PRATYAYA, TermKind.PRATIPADIKA)) {
            val state = DerivationState(listOf(
                DerivationTerm("future", "स्य", kind, upadesha = Vikarana.SYA.upadesha),
                DerivationTerm("left", "ए", TermKind.PRATIPADIKA),
                DerivationTerm("right", "इ", TermKind.PRATIPADIKA),
            ), stage = DerivationStage.PADA_FORMED,
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LRT)))
            assertEquals(kind == TermKind.PRATYAYA, EcoYavayavahSutra.matches(state))
        }
    }

    @Test fun `all four ec expansions preserve annotated vowels signs and metadata`() {
        for ((vowel, replacement) in listOf(
            Svara.E to listOf(Svara.A, Vyanjana.YA),
            Svara.O to listOf(Svara.A, Vyanjana.VA),
            Svara.AI to listOf(Svara.AA, Vyanjana.YA),
            Svara.AU to listOf(Svara.AA, Vyanjana.VA),
        )) {
            val left = DerivationTerm("left", "${vowel.devanagari}ँऽ", TermKind.PRATIPADIKA, createdBySutra = "fixture")
            val right = DerivationTerm("right", "इँऽ", TermKind.PRATYAYA, upadesha = "fixture-affix")
            val before = DerivationState(listOf(left, right), stage = DerivationStage.PADA_FORMED)
            assertTrue(EcoYavayavahSutra.matches(before))
            val after = EcoYavayavahSutra.apply(before).state
            val result = after.terms.single()
            assertEquals(replacement + Svara.I, result.varnas)
            assertEquals(listOf(true, false, true), result.phonologicalText.effectiveVarnas.map { it.nasalized })
            assertEquals(listOf(2, 3), result.orthographicSigns.map { it.afterVarnaCount })
            assertEquals(left.upadesha, result.upadesha)
            assertEquals(left.createdBySutra, result.createdBySutra)
            assertEquals(setOf(right.upadesha), result.sourceSuffixUpadeshas)
            assertEquals(0, after.substitutions.single().sourceVarnaIndex)
            assertEquals(right.surface, after.droppedTerms.single().originalSurfaceBeforeDrop)
        }
    }
}
