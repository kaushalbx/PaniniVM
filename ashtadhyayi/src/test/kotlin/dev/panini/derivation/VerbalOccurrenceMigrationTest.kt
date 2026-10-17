package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya3.pada4.ItasCaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.LopoVyorValiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.VantoYiPratyayeSutra
import dev.panini.core.Lakara
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.*
import kotlin.test.*

class VerbalOccurrenceMigrationTest {
    @Test fun `val excludes ya but retains the other hal sounds`() {
        for (varna in Vyanjana.entries) {
            val hal = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, varna)
            assertEquals(hal && varna != Vyanjana.YA,
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.VAL, varna), varna.toString())
        }
    }

    @Test fun `vy lopa preserves preceding annotations and excludes ya followers`() {
        for (word in listOf("अँऽय्", "अँऽव्")) {
            val left = DerivationTerm("left", word, TermKind.PRATIPADIKA)
            val state = DerivationState(listOf(left, DerivationTerm("right", "तत्र", TermKind.PRATIPADIKA)),
                samjnas = setOf(SamjnaAssignment(left.id, Samjna.PADA)))
            assertTrue(LopoVyorValiSutra.matches(state))
            val result = LopoVyorValiSutra.apply(state).state
            assertEquals(left.phonologicalText.effectiveVarnas.dropLast(1), result.terms.first().phonologicalText.effectiveVarnas)
            assertEquals(left.phonologicalText.sourceOrthographicSigns, result.terms.first().orthographicSigns)
            assertEquals(1, result.substitutions.single().sourceVarnaIndex)
            assertFalse(LopoVyorValiSutra.matches(state.replaceTerm("right", state.terms.last().copy(surface = "याति"))))
        }
    }

    @Test fun `vanta replacement preserves nasalization and signs`() {
        for ((word, expected) in listOf("गोँऽ" to listOf(Vyanjana.GA, Svara.A, Vyanjana.VA),
            "नौँऽ" to listOf(Vyanjana.NA, Svara.AA, Vyanjana.VA))) {
            val stem = DerivationTerm("stem", word, TermKind.PRATIPADIKA)
            val affix = DerivationTerm("affix", "य", TermKind.PRATYAYA)
            val state = DerivationState(listOf(stem, affix), samjnas = setOf(SamjnaAssignment(affix.id, Samjna.PRATYAYA)))
            assertTrue(VantoYiPratyayeSutra.matches(state))
            val result = VantoYiPratyayeSutra.apply(state).state
            assertEquals(expected, result.terms.first().varnas)
            assertTrue(result.terms.first().phonologicalText.effectiveVarnas[1].nasalized)
            assertEquals(1, result.substitutions.single().sourceVarnaIndex)
            assertTrue(result.terms.first().orthographicSigns.any { it.sign == OrthographicSign.AVAGRAHA && it.afterVarnaCount == 3 })
        }
    }

    @Test fun `itas ca deletes exact final i without replacing the affix`() {
        val affix = DerivationTerm("ting", "तँऽइ", TermKind.PRATYAYA, upadesha = "तिप्")
        val state = DerivationState(listOf(affix), stage = DerivationStage.ANGAKARYA,
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LANG)))
        assertTrue(ItasCaSutra.matches(state))
        val result = ItasCaSutra.apply(state).state
        assertEquals(affix.phonologicalText.effectiveVarnas.dropLast(1), result.terms.single().phonologicalText.effectiveVarnas)
        assertEquals(affix.upadesha, result.terms.single().upadesha)
        assertEquals(2, result.substitutions.single().sourceVarnaIndex)
        assertEquals(DerivationStage.PADA_FORMED, result.stage)
    }
}
