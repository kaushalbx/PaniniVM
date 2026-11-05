package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.SemanticEvidenceSource
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Karaka
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ProgramMeansConstructionTest {
    private fun frame(source: String) = assertNotNull(
        VyakaranamExecutionAdapter.analyzeGrammar(PaniniParser().parse(source))).frames.single()

    @Test fun `declared imperative callback has explicit program meaning and original occurrence`() {
        val frame = frame("सूची + अम् यु + टा वि + भजँ + लोट् + सिप् ।")
        val means = frame.relations.single { it.participant.pada.sourceText == "यु+टा" }
        assertEquals(Karaka.KARANA, (means.resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(listOf("1.4.42", "2.3.18"), means.evidence.map { it.sutra })
        assertTrue(means.evidence.flatMap { it.semanticEvidence }.any {
            it.relation == SemanticRelation.INSTRUMENT && it.source == SemanticEvidenceSource.CLAUSE_CONTEXT
        })
        assertSame(frame.sourceVakya.padas.single { it.sourceText == "यु+टा" }, means.participant.pada)
    }

    @Test fun `program construction cannot infer means from an unregistered domain or missing object`() {
        for (source in listOf(
            "यु + टा वि + भजँ + लोट् + सिप् ।",
            "सूची + अम् यु + टा वि + भजँ + लट् + तिप् ।",
            "सूची + अम् यु + टा एधँ + टा वि + भजँ + लोट् + सिप् ।")) {
            val frame = frame(source)
            frame.relations.filter { it.participant.pada.sourceText.endsWith("+टा") }.forEach {
                assertTrue(it.evidence.none { proof -> proof.sutra == "1.4.42" }, source)
            }
        }
    }
}
