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

class SeshaInferenceTest {
    private val osCases = SupAffix.candidates("ओस्").map { it.vibhakti }.toSet()
    private fun resolve(cases: Set<Vibhakti>, relations: Set<SemanticRelation> = emptySet(),
        voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
        val expression = SubantaPada("राजन् + ओस्", MulaPratipadika("राजन्", "राजन्"), SupPratyaya("ओस्", "ओस्"))
        val p = ParticipantFacts("related", expression, cases, semanticRelations = relations)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("अस्"), p, listOf(p), voice))
    }

    @Test fun `genitive alone retains a relation hypothesis without establishing sambandha`() {
        val plain = resolve(setOf(Vibhakti.SASTHI))
        assertEquals(setOf(Karaka.SAMBANDHA), plain.candidates)
        assertEquals(null, plain.resolved)
        assertEquals(null, plain.resolvedVibhakti)
        assertEquals(emptyList(), plain.evidence)
        val syncretic = resolve(osCases)
        assertEquals(setOf(Karaka.SAMBANDHA, Karaka.ADHIKARANA), syncretic.candidates)
        assertEquals(null, syncretic.resolved)
        assertEquals(emptyList(), syncretic.evidence)
    }

    @Test fun `independently established sesha selects genitive from os without a karaka designation`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (observed in listOf(setOf(Vibhakti.SASTHI), osCases)) {
                val result = resolve(observed, setOf(SemanticRelation.SESHA_RELATION), voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
                assertEquals(listOf("2.3.50"), result.evidence.map { it.sutra })
                assertEquals(emptyList(), result.alternatives)
            }
        }
        assertEquals(null, resolve(setOf(Vibhakti.SAPTAMI), setOf(SemanticRelation.SESHA_RELATION)).resolvedVibhakti)
        assertEquals(null, resolve(setOf(Vibhakti.SASTHI),
            setOf(SemanticRelation.SESHA_RELATION, SemanticRelation.DIRECT_ADDRESS)).resolvedVibhakti)
    }

    @Test fun `distinct group exception cannot fall back to sesha even when relation premise is supplied`() {
        val distinct = setOf(SemanticRelation.COMPARATIVE_DISTINCTION, SemanticRelation.NIRDHARANA_REFERENCE,
            SemanticRelation.SEPARATE_COMPARISON_GROUPS, SemanticRelation.SESHA_RELATION)
        assertEquals(null, resolve(setOf(Vibhakti.SASTHI), distinct).resolvedVibhakti)
        val ablative = resolve(setOf(Vibhakti.SASTHI, Vibhakti.PANCHAMI), distinct)
        assertEquals(Vibhakti.PANCHAMI, ablative.resolvedVibhakti)
        assertEquals(listOf("2.3.42"), ablative.evidence.map { it.sutra })
    }
}
