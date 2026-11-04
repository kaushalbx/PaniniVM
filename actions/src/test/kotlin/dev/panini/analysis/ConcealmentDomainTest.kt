package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class ConcealmentDomainTest {
    @Test fun `structured concealment rejects wrong unknown and substring domains`() {
        for ((root, prefixes) in listOf("ली" to listOf("प्र"), "ली" to listOf("अज्ञात"),
            "निलीकल्प्" to emptyList(), "तिरोभकल्प्" to emptyList())) {
            val p = ParticipantFacts("observer", AvyayaPada("observer", "observer"), setOf(Vibhakti.PANCHAMI),
                semanticRelations = setOf(SemanticRelation.SOURCE, SemanticRelation.CONCEALMENT_OBSERVER))
            val verb = TingantaPada("action", prefixes, DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), p, listOf(p),
                Prayoga.KARTARI, verbNode = verb))
            assertTrue(result.evidence.none { it.sutra == "1.4.28" }, "$root $prefixes")
        }
    }
}
