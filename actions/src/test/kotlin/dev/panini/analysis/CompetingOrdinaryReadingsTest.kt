package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.*

class CompetingOrdinaryReadingsTest {
    private data class Reading(val role: Karaka, val meaning: SemanticRelation, val sutra: String)

    @Test fun `every ordinary role pair validates its own active and passive case branch`() {
        val readings = listOf(Reading(Karaka.KARTR, SemanticRelation.INDEPENDENT_AGENT, "1.4.54"),
            Reading(Karaka.KARMAN, SemanticRelation.DESIRED_OBJECT, "1.4.49"),
            Reading(Karaka.KARANA, SemanticRelation.INSTRUMENT, "1.4.42"),
            Reading(Karaka.ADHIKARANA, SemanticRelation.LOCATION, "1.4.45"))
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            fun case(role: Karaka) = when (role) {
                Karaka.KARTR -> if (voice == Prayoga.KARTARI) Vibhakti.PRATHAMA else Vibhakti.TRTIYA
                Karaka.KARMAN -> if (voice == Prayoga.KARTARI) Vibhakti.DVITIYA else Vibhakti.PRATHAMA
                Karaka.KARANA -> Vibhakti.TRTIYA
                else -> Vibhakti.SAPTAMI
            }
            fun caseSutra(role: Karaka) = when (case(role)) {
                Vibhakti.PRATHAMA -> "2.3.46"
                Vibhakti.DVITIYA -> "2.3.2"
                Vibhakti.TRTIYA -> "2.3.18"
                else -> "2.3.36"
            }
            for (first in readings.indices) for (second in first + 1 until readings.size) {
                val pair = listOf(readings[first], readings[second])
                val roles = pair.mapTo(mutableSetOf()) { it.role }
                val cases = roles.mapTo(mutableSetOf(), ::case)
                val label = "$voice $roles"
                val p = ParticipantFacts("p", AvyayaPada(label, label), cases,
                    semanticRelations = pair.mapTo(mutableSetOf()) { it.meaning })
                val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p),
                    voice, candidates = roles))
                assertNull(result.resolved, label)
                assertEquals(cases.singleOrNull(), result.resolvedVibhakti, label)
                assertEquals(roles, result.candidates, label)
                assertEquals(1, result.alternatives.size, label)
                val alternative = result.alternatives.single()
                assertEquals(KarakaAlternativeKind.COMPETING_ANALYSIS, alternative.kind, label)
                assertTrue(alternative.matchesObservedMorphology, label)
                val chains = setOf(result.evidence.map { it.sutra }, alternative.evidence.map { it.sutra })
                assertEquals(pair.map { listOf(it.sutra, caseSutra(it.role)) }.toSet(), chains, label)
                for (strong in pair) {
                    val selected = p.copy(semanticEvidence = pair.mapTo(mutableSetOf()) {
                        SemanticRelationEvidence(it.meaning,
                            if (it == strong) SemanticEvidenceSource.CLAUSE_CONTEXT else SemanticEvidenceSource.VOICE_CONSTRUCTION,
                            "Test alternative reading strength")
                    })
                    val selectedResult = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), selected,
                        listOf(selected), voice, candidates = roles))
                    assertEquals(strong.role, selectedResult.resolved, "$label supplied ${strong.role}")
                    assertEquals(case(strong.role), selectedResult.resolvedVibhakti, label)
                    assertEquals(listOf(strong.sutra, caseSutra(strong.role)), selectedResult.evidence.map { it.sutra }, label)
                    assertTrue(selectedResult.alternatives.isEmpty(), label)
                }
            }
        }
    }

    @Test fun `equal object and instrument readings retain their own sutra chains`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INSTRUMENT))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p),
            Prayoga.KARTARI, candidates = setOf(Karaka.KARMAN, Karaka.KARANA)))
        assertNull(result.resolved)
        assertNull(result.resolvedVibhakti)
        assertEquals(setOf(Karaka.KARMAN, Karaka.KARANA), result.candidates)
        assertEquals(listOf("1.4.42", "2.3.18"), result.evidence.map { it.sutra })
        val competing = result.alternatives.single()
        assertEquals(KarakaAlternativeKind.COMPETING_ANALYSIS, competing.kind)
        assertTrue(competing.matchesObservedMorphology)
        assertEquals(Karaka.KARMAN, competing.karaka)
        assertEquals(Vibhakti.DVITIYA, competing.vibhakti)
        assertEquals(listOf("1.4.49", "2.3.2"), competing.evidence.map { it.sutra })
        assertTrue(result.evidence.all { !it.optional })
    }

    @Test fun `equal object and locus readings are not decided by rule order`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.DVITIYA, Vibhakti.SAPTAMI),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.LOCATION))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), p, listOf(p),
            Prayoga.KARTARI, candidates = setOf(Karaka.KARMAN, Karaka.ADHIKARANA)))
        assertNull(result.resolved)
        assertNull(result.resolvedVibhakti)
        assertEquals(listOf("1.4.45", "2.3.36"), result.evidence.map { it.sutra })
        assertEquals(listOf("1.4.49", "2.3.2"), result.alternatives.single().evidence.map { it.sutra })
    }
}
