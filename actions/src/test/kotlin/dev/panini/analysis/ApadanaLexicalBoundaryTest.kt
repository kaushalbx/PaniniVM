package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.*

class ApadanaLexicalBoundaryTest {
    @Test fun `lexical apadana rules reject substring lookalikes even with supplied relation premises`() {
        val examples = listOf("बिभेकल्प्" to SemanticRelation.FEAR_CAUSE,
            "त्रायकल्प्" to SemanticRelation.FEAR_CAUSE,
            "पराजयकल्प्" to SemanticRelation.UNBEARABLE_OBJECT,
            "वारयकल्प्" to SemanticRelation.PREVENTED_DESIRED_OBJECT,
            "जनकल्प्" to SemanticRelation.BIRTH_MATERIAL_CAUSE,
            "भवकल्प्" to SemanticRelation.FIRST_MANIFESTATION_SOURCE,
            "प्रभवकल्प्" to SemanticRelation.FIRST_MANIFESTATION_SOURCE)
        for ((root, relation) in examples) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.PANCHAMI),
                semanticRelations = setOf(SemanticRelation.SOURCE, relation))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p), Prayoga.KARTARI))
            assertNull(result.resolved, root)
            assertTrue(result.evidence.isEmpty(), root)
        }
    }
}
