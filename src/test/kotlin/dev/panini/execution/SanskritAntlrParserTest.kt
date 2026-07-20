package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SanskritAntlrParserTest {
    @Test
    fun `ANTLR4 parses simple single clause Sanskrit utterance`() {
        val input = SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, एकं द्वे च योजय।")
        val result = SanskritAntlrParser.parse(input)
        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result)
        assertEquals(1, analyzed.analysis.kriyas.size)
        assertEquals("07.0007", analyzed.analysis.kriyas[0].dhatuId)
        assertEquals("सङ्ख्यायोजनम्", analyzed.analysis.kriyas[0].selectedOperation)
    }

    @Test
    fun `ANTLR4 parses multi-clause utterance with tatah`() {
        val input = SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "एकं द्वे च योजय ततः फले द्वे योजय।")
        val result = SanskritAntlrParser.parse(input)

        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result)
        assertEquals(2, analyzed.analysis.kriyas.size)
        assertEquals("07.0007", analyzed.analysis.kriyas[0].dhatuId)
        assertEquals("07.0007", analyzed.analysis.kriyas[1].dhatuId)
    }
}
