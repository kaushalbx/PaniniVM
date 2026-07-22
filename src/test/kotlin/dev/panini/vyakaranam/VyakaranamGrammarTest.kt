package dev.panini.vyakaranam

import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.PadaType
import dev.panini.vyakaranam.analysis.AnalyzedSamuccita
import dev.panini.vyakaranam.lexicon.DhatuEntry
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

class VyakaranamGrammarTest {

    @Test
    fun `parses an annotated finite sentence`() {
        assertParsesUkti("राम + सुँ फल + अम् खाद् + लट् + तिप् ।")
    }

    @Test
    fun `parses derivationally annotated nominal morphology`() {
        assertParsesUkti("प्र + कृ + क्त + मतुप् + ङीप् + सुँ अस्ति + लट् + तिप् ।")
    }

    @Test
    fun `parses sanadi morphology without treating it as surface Sanskrit`() {
        assertParsesUkti("राम + सुँ भू + णिच् + सन् + लट् + तिप् ।")
    }

    @Test
    fun `parses explicit compound sup deletion`() {
        assertParsesUkti("राजन् + ङस् + लुक्-पुरुष + सुँ भू + लट् + तिप् ।")
    }

    @Test
    fun `coordinated subantas participate in karaka analysis`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("लक्ष्मण", setOf(Linga.PUMS)),
            ),
            dhatus = listOf(
                DhatuEntry("गम्", "गम्", "भ्वादिगण", setOf(PadaType.PARASMAIPADA), false),
            ),
        )
        val analysis = PaniniyaVyakaranamEngine(lexicon).analyze(
            "राम + सुँ, लक्ष्मण + औ च गम् + लट् + झि ।",
        ).vakyas.single()

        assertIs<AnalyzedSamuccita>(analysis.padaAnalyses.first())
        assertEquals(2, analysis.karakas.count { it.karaka == Karaka.KARTR })
    }

    private fun assertParsesUkti(source: String) {
        val errors = mutableListOf<String>()
        val listener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                errors += "$line:$charPositionInLine ${msg.orEmpty()}"
            }
        }

        val lexer = VyakaranamLexer(CharStreams.fromString(source)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }
        val parser = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }

        parser.ukti()

        assertEquals(emptyList(), errors)
    }
}
