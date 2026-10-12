package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class SavarnaAugmentAnnotationTest {
    @Test fun `beginning augment coalescence retains target identity and surviving annotations`() {
        val target = DerivationTerm("target", "अकँऽ", TermKind.DHATU, createdBySutra = "fixture")
        val augment = DerivationTerm("augment", "अँऽ", TermKind.AGAMA,
            augmentTargetId = target.id, mergeIntoAugmentTarget = false, establishedBySutras = setOf("1.1.46"))
        val before = DerivationState(listOf(augment, target), stage = DerivationStage.PADA_FORMED)
        assertTrue(SavarnaDirghaSutra.matches(before))
        val after = SavarnaDirghaSutra.apply(before).state
        val changed = after.terms.single()
        assertEquals(listOf(Svara.AA) + target.varnas.drop(1), changed.varnas)
        assertEquals(target.id, changed.id)
        assertEquals(target.upadesha, changed.upadesha)
        assertEquals(target.createdBySutra, changed.createdBySutra)
        assertTrue(changed.phonologicalText.effectiveVarnas.first().nasalized)
        assertTrue(changed.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(setOf(1, changed.varnas.size), changed.orthographicSigns.map { it.afterVarnaCount }.toSet())
        assertEquals(listOf(augment.id, target.id), after.substitutions.map { it.targetId })
        assertEquals(listOf(0, 0), after.substitutions.map { it.sourceVarnaIndex })
    }
}
