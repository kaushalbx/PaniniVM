package dev.panini.vyakaranam.parser

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PaniniCompletionContextTest {
    @Test fun `lakara requires a ting ending at caret`() {
        val expected = PaniniCompletionContext.expectedTokenNames("भू + लट् + ")
        assertTrue("TIP" in expected, expected.toString())
        assertFalse("SHAP" in expected, expected.toString())
        assertFalse("GHANJ" in expected, expected.toString())
    }

    @Test fun `vikarana requires lakara at caret`() {
        val expected = PaniniCompletionContext.expectedTokenNames("भू + शप् + ")
        assertTrue("LAT" in expected, expected.toString())
        assertFalse("TIP" in expected, expected.toString())
    }

    @Test fun `candidate continuation is validated by canonical grammar consumption`() {
        assertTrue(PaniniCompletionContext.acceptsNextToken("भू + ", "शप्"))
        assertFalse(PaniniCompletionContext.acceptsNextToken("भू + ", "तिप्"))
        assertTrue(PaniniCompletionContext.acceptsNextToken("द्वि + ", "कृत्वसुच्"))
        assertTrue(PaniniCompletionContext.acceptsNextToken("राम + ", "सुँ"))
    }

}
