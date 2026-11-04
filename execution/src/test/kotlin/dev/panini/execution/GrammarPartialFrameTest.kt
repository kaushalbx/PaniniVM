package dev.panini.execution

import dev.panini.analysis.FrameDiagnosticCode
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.analysis.PadaAnalyzer
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.ast.AkhyataVakya
import kotlin.test.*

class GrammarPartialFrameTest {
    @Test fun `explicit non executable root has a lexical grammar identity without an invented operation`() {
        for ((root, identity) in listOf("रमुँ" to "01.0989", "रमँ" to "01.0953")) {
            val source = "$root + लोट् + सिप् ।"
            val ukti = PaniniParser().parse(source)
            val grammar = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
            val dhatu = assertNotNull(grammar.frames.single().kriya?.dhatu)
            assertEquals(identity, dhatu.id)
            assertTrue(dhatu.operations.isEmpty())
            assertTrue(grammar.blockingDiagnostics.none { it.code == FrameDiagnosticCode.UNKNOWN_DHATU })
            // A lexical binding may exist, but no operation may be invented for it.
            assertIs<ExecutionResult.Failure>(PaniniVM().eval(source))
        }
    }

    @Test fun `grammar exposes an unresolved lexical head without selecting a homonym or enabling execution`() {
        val source = "वि + रम् + लोट् + सिप् ।"
        val ukti = PaniniParser().parse(source)
        val head = (ukti.grammaticalVakyas().single() as AkhyataVakya).tinganta
        assertFailsWith<IllegalArgumentException> {
            PadaAnalyzer(InMemoryVyakaranamLexicon(emptyList(), emptyList())).analyzeTinganta(head)
        }
        val grammar = assertNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))
        assertNull(grammar.frames.single().kriya?.dhatu)
        assertTrue(grammar.blockingDiagnostics.any { it.code == FrameDiagnosticCode.UNKNOWN_DHATU })
        // Memory and executable binding retain their stricter lexical gate.
        assertNull(VyakaranamExecutionAdapter.analyzeForMemory(ukti))
        assertIs<ExecutionBindingResult.Invalid>(VyakaranamExecutionAdapter.bind(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source), ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
        ))
    }
}
