package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.VrddhirEciSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.*

class VrddhiAugmentCompositionTest {
    private fun state(rootSurface: String, augmentSurface: String = "अ"): DerivationState {
        val root = DerivationTerm("root", rootSurface, TermKind.DHATU, upadesha = "एध्", createdBySutra = "fixture")
        val augment = DerivationTerm("at-agama", augmentSurface, TermKind.AGAMA, upadesha = "अट्",
            augmentTargetId = root.id, mergeIntoAugmentTarget = false, establishedBySutras = setOf("1.1.46"),
            createdBySutra = "6.4.71", itMarkerProvenance = setOf(ItMarkerProvenance(ItMarker.T, "1.3.3", "ट्")))
        return DerivationState(listOf(augment, root), stage = DerivationStage.ANGAKARYA)
    }

    @Test fun `all ec grades retain target identity and original augment ledger`() {
        for ((input, expected) in listOf("एध्" to "ऐध्", "ओक्" to "औक्", "ऐध्" to "ऐध्", "औक्" to "औक्")) {
            val before = state(input)
            assertTrue(VrddhirEciSutra.matches(before))
            val after = VrddhirEciSutra.apply(before).state
            assertEquals(expected, after.surface)
            assertEquals("root", after.terms.single().id)
            assertEquals("fixture", after.terms.single().createdBySutra)
            assertEquals(DerivationStage.PADA_FORMED, after.stage)
            val dropped = after.droppedTerms.single()
            assertEquals("अ", dropped.originalSurfaceBeforeDrop)
            assertEquals(before.terms.first().itMarkerProvenance, dropped.itMarkerProvenance)
            assertTrue(after.substitutions.any { it.targetId == "at-agama" && it.sourceVarnaIndex == 0 })
        }
    }

    @Test fun `nasal vowels and written signs survive without reconstructing the root`() {
        val after = VrddhirEciSutra.apply(state("एँकँऽ", "अऽ")).state
        assertEquals("ऐँऽकँऽ", after.surface)
        assertEquals("ऐक".toVarnas(), after.terms.single().varnas)
        assertTrue(after.terms.single().phonologicalText.effectiveVarnas.filter { it.varna is Svara }.all { it.nasalized })
        assertEquals("अऽ", after.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 1)),
            after.droppedTerms.single().orthographicSigns)
    }

    @Test fun `shared initial grade retains exact contributing origin sets`() {
        val before = state("एध्").let { initial -> initial.copy(terms = initial.terms.map {
            it.copy(completedPadaOrigins = List(it.varnas.size) { _ -> setOf(it.id) })
        }) }
        val after = VrddhirEciSutra.apply(before).state
        assertEquals(setOf("root", "at-agama"), after.terms.single().padaOriginsAt(0))
        assertEquals(setOf("root"), after.terms.single().padaOriginsAt(1))
        assertEquals(listOf(setOf("at-agama")), after.droppedTerms.single().originalCompletedPadaOriginsBeforeDrop)
    }

    @Test fun `placed long a augment has the same ec grade domain`() {
        val before = state("ओक्", "आ")
        assertTrue(VrddhirEciSutra.matches(before))
        val after = VrddhirEciSutra.apply(before).state
        assertEquals("औक्", after.surface)
        assertEquals("आ", after.droppedTerms.single().originalSurfaceBeforeDrop)
    }

    @Test fun `empty augment composition does not deduplicate distinct signs on the shared boundary`() {
        val after = VrddhirEciSutra.apply(state("एऽध्", "अऽ")).state
        assertEquals("ऐऽऽध्", after.surface)
        assertEquals(2, after.terms.single().orthographicSigns.size)
        assertEquals("ऐध्".toVarnas(), after.terms.single().varnas)
    }

    @Test fun `same id without genuine beginning augment placement is not licensed`() {
        val before = state("एध्")
        val augment = before.terms.first()
        val right = before.terms.last()
        // Remove the final-pair a+ec route so this specifically audits augment lookup.
        val tail = DerivationTerm("ending", "ति", TermKind.PRATYAYA)
        for (bad in listOf(augment.copy(kind = TermKind.PRATIPADIKA), augment.copy(augmentTargetId = "other"),
            augment.copy(establishedBySutras = emptySet()), augment.copy(mergeIntoAugmentTarget = true))) {
            assertFalse(VrddhirEciSutra.matches(before.copy(terms = listOf(bad, right, tail))))
        }
    }
}
