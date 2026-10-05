package dev.panini.vyakaranam.parser

import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Grammar-only derivation input is deliberately separate from executable PVM. */
class VyutpattiGrammarTest {
    @Test
    fun `derivation entry uses segmented verbal morphology`() {
        val source = "भू + शप् + लट् + तिप्"
        val context = parseDerivation(source)
        assertEquals("भू", context.tingantaPada().dhatuPrakriti().text)
        assertEquals("शप्", context.tingantaPada().vikarana()?.text)
        assertEquals("लट्", context.tingantaPada().lakara().text)
        assertEquals("तिप्", context.tingantaPada().tingPratyaya().text)
    }

    @Test
    fun `derivation notation requires one root with prefixes before it`() {
        assertEquals("सम्+", parseDerivation("सम् + भू + शप् + लट् + तिप्")
            .tingantaPada().upasargaKrama()?.text)
        for (source in listOf("शप् + लट् + तिप्", "भू + कृ + लट् + तिप्",
            "भू + सम् + शप् + लट् + तिप्", "भू + अभ्यासः(भू) + लट् + तिप्",
            "भू + आदेशः(भव) + लट् + तिप्")) {
            assertFailsWith<PaniniParseException>(source) { parseDerivation(source) }
        }
    }

    private fun parseDerivation(source: String): VyakaranamParser.VyutpattiTingantaContext {
        val errors = PaniniSyntaxErrorListener()
        val lexer = VyakaranamLexer(CharStreams.fromString(source)).apply {
            removeErrorListeners()
            addErrorListener(errors)
        }
        val grammar = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            addErrorListener(errors)
        }
        val context = grammar.vyutpattiTinganta()
        errors.throwIfAny()
        return context
    }
}
