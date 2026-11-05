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

class ProgramLocusConstructionTest {
    private fun frame(source: String) = assertNotNull(
        VyakaranamExecutionAdapter.analyzeGrammar(PaniniParser().parse(source))).frames.single()

    @Test fun `declared indexed retrieval supplies a program position reading before runtime binding`() {
        val frame = frame("मूल्य + अम् सूची + ङसिँ द्वितीय + ङि ग्रहँ + श्ना + लोट् + सिप् ।")
        val locus = frame.relations.single { it.participant.pada.sourceText == "द्वितीय+ङि" }
        assertEquals(Karaka.ADHIKARANA, (locus.resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(listOf("1.4.45", "2.3.36"), locus.evidence.map { it.sutra })
        assertTrue(locus.evidence.flatMap { it.semanticEvidence }.any {
            it.relation == SemanticRelation.LOCATION && it.source == SemanticEvidenceSource.CLAUSE_CONTEXT
        })
        assertSame(frame.sourceVakya.padas.single { it.sourceText == "द्वितीय+ङि" }, locus.participant.pada)
    }

    @Test fun `a syncretic form gets no locus but a plain locative does, for any root and voice`() {
        val syncretic = frame("मूल्य + अम् सूची + ङसिँ द्वितीय + ओस् ग्रहँ + श्ना + लोट् + सिप् ।")
        assertTrue(syncretic.relations.all { relation -> relation.evidence.none { it.sutra == "1.4.45" } })
        for (source in listOf("ग्राम + ङि पठँ + लोट् + सिप् ।", "मूल्य + सुँ सूची + ङसिँ द्वितीय + ङि ग्रहँ + यक् + लट् + त ।")) {
            assertTrue(frame(source).relations.any { relation -> relation.evidence.any { it.sutra == "1.4.45" } }, source)
        }
    }
}
