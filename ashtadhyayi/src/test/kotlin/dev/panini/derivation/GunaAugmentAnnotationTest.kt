package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.shiksha.*
import kotlin.test.*

class GunaAugmentAnnotationTest {
    @Test fun `guna beginning augment composition retains target and annotations`() {
        for ((initial, replacement) in listOf("इ" to listOf(Svara.E), "उ" to listOf(Svara.O),
            "ऋ" to listOf(Svara.A, Vyanjana.RA))) {
            val target = DerivationTerm("target", initial + "कँऽ", TermKind.DHATU, createdBySutra = "fixture")
            val augment = DerivationTerm("augment", "अँऽ", TermKind.AGAMA,
                augmentTargetId = target.id, mergeIntoAugmentTarget = false, establishedBySutras = setOf("1.1.46"))
            val before = DerivationState(listOf(augment, target), stage = DerivationStage.PADA_FORMED)
            assertTrue(AdGunaSutra.matches(before))
            val after = AdGunaSutra.apply(before).state
            val changed = after.terms.single()
            assertEquals(replacement + target.varnas.drop(1), changed.varnas)
            assertEquals(target.id, changed.id)
            assertEquals(target.upadesha, changed.upadesha)
            assertEquals(target.createdBySutra, changed.createdBySutra)
            assertTrue(changed.phonologicalText.effectiveVarnas.first().nasalized)
            assertTrue(changed.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(setOf(replacement.size, changed.varnas.size), changed.orthographicSigns.map { it.afterVarnaCount }.toSet())
            assertEquals(listOf(augment.id, target.id), after.substitutions.map { it.targetId })
        }
    }
}
