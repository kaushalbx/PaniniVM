package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class VyakaranamExecutionAnalyzerTest {

    @Test
    fun `ANTLR4 parses segmented single clause Sanskrit utterance`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)
        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result, result.toString())
        assertEquals(1, analyzed.analysis.kriyas.size)
        assertEquals("07.0007", analyzed.analysis.kriyas[0].dhatuId)
        assertEquals("सङ्ख्यायोजनम्", analyzed.analysis.kriyas[0].selectedOperation)
    }

    @Test
    fun `ANTLR4 parses segmented multi-clause utterance with tatah`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)

        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result, result.toString())
        assertEquals(2, analyzed.analysis.kriyas.size)
        assertEquals("07.0007", analyzed.analysis.kriyas[0].dhatuId)
        assertEquals("07.0007", analyzed.analysis.kriyas[1].dhatuId)
    }

    @Test
    fun `ANTLR4 parses 3-clause chained utterance separated by danda`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । फल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् । फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)

        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result, result.toString())
        assertEquals(3, analyzed.analysis.kriyas.size)
        assertEquals("07.0007", analyzed.analysis.kriyas[0].dhatuId)
        assertEquals("10.0391", analyzed.analysis.kriyas[1].dhatuId)
        assertEquals("07.0007", analyzed.analysis.kriyas[2].dhatuId)
        assertEquals(2, analyzed.analysis.dependencies.size)
    }

    @Test
    fun `ANTLR4 parses nominal-only sentence`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "राम + सुँ लक्ष्मण + सुँ च ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)
        val analyzed = assertIs<VakyaAnalysisResult.Analyzed>(result)
        // No verbs (kriyas) in a nominal sentence currently in this parser's logic
        assertEquals(0, analyzed.analysis.kriyas.size)
    }

    @Test
    fun `ANTLR4 parses complex compound members`() {
        // gachat (kridanta) - putra (simple)
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "गम् + शतृ-पुत्र + सुँ भू + लट् + तिप् ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)
        assertIs<VakyaAnalysisResult.Analyzed>(result)
    }

    @Test
    fun `ANTLR4 parses stri pratyaya derivations`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "अश्व + टाप् + सुँ ।",
        )
        val result = VyakaranamExecutionAnalyzer.analyze(input)
        assertIs<VakyaAnalysisResult.Analyzed>(result)
    }
}
