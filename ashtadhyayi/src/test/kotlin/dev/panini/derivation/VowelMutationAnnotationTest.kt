package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada3.AccaGhehSutra
import dev.panini.ashtadhyayi.adhyaya7.pada3.AngiCapahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada2.HaliCaSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.*
import kotlin.test.*

class VowelMutationAnnotationTest {
    @Test
    fun `vowel mutations retain target nasality and parsed signs`() {
        val cases = listOf(
            Triple(HaliCaSutra, DerivationTerm("stem", "दिँव्ऽ", TermKind.DHATU, upadesha = "दिवुँ"),
                DerivationTerm("affix", "य", TermKind.PRATYAYA, upadesha = "श्यन्")),
            Triple(AngiCapahSutra, DerivationTerm("stem", "अँआँऽ", TermKind.PRATIPADIKA),
                DerivationTerm("affix", "आ", TermKind.PRATYAYA, upadesha = SupAffix.TA.upadesha)),
            Triple(AccaGhehSutra, DerivationTerm("stem", "अँइँऽ", TermKind.PRATIPADIKA),
                DerivationTerm("affix", "इ", TermKind.PRATYAYA, upadesha = SupAffix.NGI.upadesha)),
        )
        for ((rule, stem, affix) in cases) {
            val original = DerivationState(listOf(stem, affix),
                activeAdhikaras = setOf("6.4.1"), samjnas = setOf(SamjnaAssignment(stem.id, Samjna.GHI)))
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val target = result.terms.first()
            val changedIndex = if (rule == HaliCaSutra) 1 else stem.varnas.lastIndex
            assertEquals(when (rule) { HaliCaSutra -> Svara.II; AngiCapahSutra -> Svara.E; else -> Svara.A }, target.varnas[changedIndex])
            assertEquals(stem.phonologicalText.effectiveVarnas.map { it.nasalized }, target.phonologicalText.effectiveVarnas.map { it.nasalized })
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, stem.varnas.size)), target.orthographicSigns)
            assertEquals(stem.id, target.id)
            assertEquals(stem.upadesha, target.upadesha)
            assertEquals(original.samjnas, result.samjnas)
            if (rule != AccaGhehSutra) assertEquals(affix, result.terms.last())
            else assertEquals(listOf(Svara.AU), result.terms.last().varnas)
        }
    }
}
