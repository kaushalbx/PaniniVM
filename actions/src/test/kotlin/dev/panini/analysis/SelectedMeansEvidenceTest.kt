package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.*

class SelectedMeansEvidenceTest {
    private fun resolve(instrumentSource: SemanticEvidenceSource, agentSource: SemanticEvidenceSource?): KarakaResolution {
        val evidence = setOf(SemanticRelationEvidence(SemanticRelation.INSTRUMENT, instrumentSource, "means reading")) +
            listOfNotNull(agentSource?.let { SemanticRelationEvidence(SemanticRelation.INDEPENDENT_AGENT, it, "agent reading") })
        val p = ParticipantFacts("p", AvyayaPada("तेन", "तेन"), setOf(Vibhakti.TRTIYA),
            semanticRelations = evidence.mapTo(mutableSetOf()) { it.relation }, semanticEvidence = evidence)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("लिख्"), p, listOf(p), Prayoga.KARMANI))
    }

    @Test fun `root valency and case category alone do not establish selected means`() {
        for (source in listOf(SemanticEvidenceSource.DHATU_VALENCY, SemanticEvidenceSource.NOMINAL_CATEGORY,
            SemanticEvidenceSource.UNAMBIGUOUS_MORPHOLOGY)) {
            assertEquals(null, resolve(source, null).resolved)
        }
    }

    @Test fun `selected reading outranks less specific defaults while equal readings remain competing`() {
        val agent = resolve(SemanticEvidenceSource.DHATU_VALENCY, SemanticEvidenceSource.CLAUSE_CONTEXT)
        assertEquals(listOf("1.4.54", "2.3.18"), agent.evidence.map { it.sutra })
        val instrument = resolve(SemanticEvidenceSource.CLAUSE_CONTEXT, SemanticEvidenceSource.LEXICAL_ENTRY)
        assertEquals(listOf("1.4.42", "2.3.18"), instrument.evidence.map { it.sutra })
        val both = resolve(SemanticEvidenceSource.CLAUSE_CONTEXT, SemanticEvidenceSource.CLAUSE_CONTEXT)
        assertEquals(null, both.resolved)
        assertEquals(Vibhakti.TRTIYA, both.resolvedVibhakti)
        assertEquals(setOf(Karaka.KARTR, Karaka.KARANA), both.candidates)
        assertEquals(listOf("1.4.54", "2.3.18"), both.alternatives.single().evidence.map { it.sutra })
    }
}
