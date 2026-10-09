package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada2.CohKuhSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.*
import dev.panini.ashtadhyayi.adhyaya8.pada4.*
import dev.panini.shiksha.*
import kotlin.test.*

class TripadiAnnotationPreservationTest {
    private fun state(left: String, right: String? = null, affix: Boolean = false): DerivationState {
        val term = DerivationTerm("left", left, if (affix) TermKind.PRATYAYA else TermKind.PRATIPADIKA,
            itProcessingPhase = ItProcessingPhase.PROCESSED, formedPadaRupa = Rupa())
        return DerivationState(
            terms = listOf(term) + listOfNotNull(right?.let { DerivationTerm("right", it, TermKind.PRATIPADIKA) }),
            samjnas = setOf(SamjnaAssignment("left", Samjna.PADA)),
            stage = DerivationStage.PADA_FORMED,
        )
    }

    @Test
    fun `tripadi exact mutations preserve unrelated nasalization and parsed signs`() {
        // Synthetic annotated inputs test representation preservation, not new lexical prescriptions.
        val cases: List<Triple<DerivationSutra, DerivationState, List<Varna>>> = listOf(
            Triple(CohKuhSutra, state("अँच्ऽ"), listOf(Svara.A, Vyanjana.KA)),
            Triple(DhoDheLopaSutra, state("अँढ्ऽ", "ढ"), listOf(Svara.A)),
            Triple(MonusvarahSutra, state("अँम्ऽ", "क"), listOf(Svara.A, Ayogavaha.ANUSVARA)),
            Triple(NashcapadantasyaSutra, state("अँन्त्ऽ"), listOf(Svara.A, Ayogavaha.ANUSVARA, Vyanjana.TA)),
            Triple(AdesapratyayayohSutra, state("इँसऽति", affix = true), listOf(Svara.I, Vyanjana.SSA, Svara.A, Vyanjana.TA, Svara.I)),
            Triple(AnusvarasyaYayiParasavarnahSutra, state("अँंऽ", "क"), listOf(Svara.A, Vyanjana.NGA)),
            Triple(VaPadantasyaSutra, state("अँंऽ", "क"), listOf(Svara.A, Vyanjana.NGA)),
            Triple(JhalamJashJhashiSutra, state("अँत्ऽ", "द"), listOf(Svara.A, Vyanjana.DA)),
            Triple(KhariCaSutra, state("अँद्ऽ", "क"), listOf(Svara.A, Vyanjana.TA)),
            Triple(VavasaneSutra, state("अँद्ऽ"), listOf(Svara.A, Vyanjana.TA)),
            Triple(JharoJhariSavarneSutra, state("अँक्त्त्ऽ", "त"), listOf(Svara.A, Vyanjana.KA, Vyanjana.TA)),
        )
        for ((rule, original, expected) in cases) {
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val term = result.terms.first()
            assertEquals(expected, term.varnas, rule.sutra)
            assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized, rule.sutra)
            val signBoundary = if (rule == AdesapratyayayohSutra) 3 else expected.size
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, signBoundary)), term.orthographicSigns, rule.sutra)
            assertEquals(original.terms.first().id, term.id)
            assertEquals(original.terms.first().upadesha, term.upadesha)
            assertEquals(original.terms.first().formedPadaRupa, term.formedPadaRupa)
            assertEquals(original.terms.drop(1), result.terms.drop(1))
            assertEquals(rule.sutra, result.substitutions.last().sutra)
        }
    }

    @Test
    fun `initial h substitution preserves following nasal vowel and avagraha`() {
        val original = state("द्", "हँऽ")
        assertTrue(JhayoHonyatarasyamSutra.matches(original))
        val result = JhayoHonyatarasyamSutra.apply(original).state
        val target = result.terms.last()
        assertEquals(listOf(Vyanjana.DHA, Svara.A), target.varnas)
        assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), target.orthographicSigns)
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals(original.terms.last().upadesha, target.upadesha)
    }
}
