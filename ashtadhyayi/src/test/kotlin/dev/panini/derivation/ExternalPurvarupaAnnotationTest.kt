package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EngahPadantadatiSutra
import dev.panini.shiksha.*
import kotlin.test.*

class ExternalPurvarupaAnnotationTest {
    @Test fun `purvarupa preserves surviving annotations and renders avagraha separately`() {
        for (surface in listOf("एँ", "ओँ")) {
            val left = DerivationTerm("left", surface, TermKind.PRATIPADIKA)
            val right = DerivationTerm("right", "अकँऽ", TermKind.PRATIPADIKA,
                upadesha = "fixture", createdBySutra = "fixture")
            val before = DerivationState(listOf(left, right),
                samjnas = setOf(SamjnaAssignment(left.id, Samjna.PADA)))
            assertTrue(EngahPadantadatiSutra.matches(before))
            val after = EngahPadantadatiSutra.apply(before).state
            assertEquals(left, after.terms.first())
            val changed = after.terms.last()
            assertEquals(right.varnas.drop(1), changed.varnas)
            assertTrue(changed.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(setOf(0, changed.varnas.size), changed.orthographicSigns.map { it.afterVarnaCount }.toSet())
            assertEquals(right.upadesha, changed.upadesha)
            assertEquals(right.createdBySutra, changed.createdBySutra)
            assertEquals(0, after.substitutions.single().sourceVarnaIndex)
            assertFalse(EngahPadantadatiSutra.matches(after))
        }
    }
}
