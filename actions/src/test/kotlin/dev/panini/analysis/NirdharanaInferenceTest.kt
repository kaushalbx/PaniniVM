package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals

class NirdharanaInferenceTest {
    private val selection = setOf(SemanticRelation.GROUP_SELECTION, SemanticRelation.NIRDHARANA_REFERENCE)
    private val distinct = setOf(SemanticRelation.COMPARATIVE_DISTINCTION,
        SemanticRelation.NIRDHARANA_REFERENCE, SemanticRelation.SEPARATE_COMPARISON_GROUPS)
    private fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation>,
        voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
        val pada = SubantaPada("मनुष्य + ओस्", MulaPratipadika("मनुष्य", "मनुष्य"), SupPratyaya("ओस्", "ओस्"))
        val p = ParticipantFacts("reference-group", pada, cases, semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अस्"), p, listOf(p), voice))
    }

    @Test fun `member selection retains genitive and locative in active and passive contexts`() {
        val permitted = setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI)
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (case in permitted) {
                val result = resolve(setOf(case), selection, voice)
                assertEquals(Karaka.ANIRDHARITA, result.resolved)
                assertEquals(case, result.resolvedVibhakti)
                assertEquals(listOf("2.3.41"), result.evidence.map { it.sutra })
                assertEquals(permitted - case, result.alternatives.map { it.vibhakti }.toSet())
                assertEquals(false, result.alternatives.single().matchesObservedMorphology)
            }
        }
        val osCases = SupAffix.candidates("ओस्").map { it.vibhakti }.toSet()
        assertEquals(permitted, osCases)
        val ambiguous = resolve(osCases, selection)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertEquals(true, ambiguous.alternatives.single().matchesObservedMorphology)
        assertEquals(null, resolve(setOf(Vibhakti.PANCHAMI), selection).resolvedVibhakti)
        for (missing in selection) {
            assertEquals(emptyList(), resolve(osCases, selection - missing).evidence, missing.name)
        }
    }

    @Test fun `distinct groups require reference and separation premises and supersede both selection cases`() {
        val allCases = setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI, Vibhakti.PANCHAMI)
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (relations in listOf(distinct, distinct + selection)) {
                val result = resolve(allCases, relations, voice)
                assertEquals(Karaka.ANIRDHARITA, result.resolved)
                assertEquals(Vibhakti.PANCHAMI, result.resolvedVibhakti)
                assertEquals(listOf("2.3.42"), result.evidence.map { it.sutra })
                assertEquals(emptyList(), result.alternatives)
                for (wrongCase in listOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI)) {
                    // Neither 2.3.41 nor residual 2.3.50 may rescue the incompatible case.
                    assertEquals(null, resolve(setOf(wrongCase), relations, voice).resolvedVibhakti)
                }
            }
        }
        for (missing in distinct) {
            assertEquals(emptyList(), resolve(setOf(Vibhakti.PANCHAMI), distinct - missing).evidence, missing.name)
        }
        // Ordinary relational genitive remains available outside this exception domain.
        assertEquals(listOf("2.3.50"), resolve(setOf(Vibhakti.SASTHI),
            setOf(SemanticRelation.SESHA_RELATION)).evidence.map { it.sutra })
    }
}
