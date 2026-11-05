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

class ProgramObjectConstructionTest {
    private fun frame(source: String) = assertNotNull(
        VyakaranamExecutionAdapter.analyzeGrammar(PaniniParser().parse(source))).frames.single()

    @Test fun `declared object meaning precedes dependent means selection without runtime values`() {
        val frame = frame("सूची + अम् यु + टा वि + भजँ + लोट् + सिप् ।")
        val obj = frame.relations.single { it.participant.pada.sourceText == "सूची+अम्" }
        assertEquals(Karaka.KARMAN, (obj.resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(listOf("1.4.49", "2.3.2"), obj.evidence.map { it.sutra })
        assertTrue(obj.evidence.flatMap { it.semanticEvidence }.any {
            it.relation == SemanticRelation.DESIRED_OBJECT && it.source == SemanticEvidenceSource.CLAUSE_CONTEXT
        })
        assertSame(frame.sourceVakya.padas.single { it.sourceText == "सूची+अम्" }, obj.participant.pada)
        val means = frame.relations.single { it.participant.pada.sourceText == "यु+टा" }
        assertEquals(listOf("1.4.42", "2.3.18"), means.evidence.map { it.sutra })
    }

    @Test fun `a root without an instruction still has its karma by voice`() {
        val obj = frame("ग्रन्थ + अम् पठँ + लोट् + सिप् ।").relations.single()
        assertEquals(listOf("1.4.49", "2.3.2"), obj.evidence.map { it.sutra })
    }

    @Test fun `a passive nominative is the expressed karma`() {
        val obj = frame("फल + सुँ ग्रहँ + यक् + लट् + त ।").relations.single()
        assertEquals(Karaka.KARMAN, (obj.resolution as FrameKarakaResolution.Resolved).karaka)
    }
}
