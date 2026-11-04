package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class FormalInstructionSourceTest {
    @Test fun `formal instruction source is a construction not a closed root list`() {
        for (passive in listOf(false, true)) for (root in listOf("इ", "गम्", "श्रु")) {
            val verb = TingantaPada("instruction action", if (root == "इ") listOf("अधि") else if (root == "गम्") listOf("आङ्") else emptyList(),
                DhatuPrakriti(root, root, if (root == "गम्") listOf("णिच्") else emptyList()),
                Lakara.LAT, TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
                vikarana = if (passive) Vikarana.YAK else null)
            fun resolve(formal: Boolean, case: Vibhakti = Vibhakti.PANCHAMI): KarakaResolution {
                val p = ParticipantFacts("teacher", AvyayaPada("teacher", "teacher"), setOf(case),
                    semanticRelations = setOf(SemanticRelation.SOURCE) +
                        if (formal) setOf(SemanticRelation.FORMAL_INSTRUCTION_SOURCE) else emptySet())
                return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                    if (passive) Prayoga.KARMANI else Prayoga.KARTARI, verbNode = verb))
            }
            assertEquals(listOf("1.4.29", "2.3.28"), resolve(true).evidence.map { it.sutra }, "$root $passive")
            assertNull(resolve(false).resolved, "$root $passive")
            assertTrue(resolve(false).evidence.isEmpty())
        }
    }

    @Test fun `ordinary listening relation keeps residual genitive instead of teacher designation`() {
        val p = ParticipantFacts("performer", AvyayaPada("नट", "नट"), setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.SESHA_RELATION))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("श्रु"), p, listOf(p), Prayoga.KARTARI))
        assertEquals(Karaka.SAMBANDHA, result.resolved)
        assertEquals(listOf("2.3.50"), result.evidence.map { it.sutra })
    }
}
