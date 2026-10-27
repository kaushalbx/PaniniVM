package dev.panini.plugin

import com.intellij.psi.tree.IElementType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmColorCoverageTest {
    @Test
    fun `every non-whitespace lexer category has a configurable color`() {
        val highlighter = PvmSyntaxHighlighter()
        COLORABLE_TOKEN_TYPES.forEach { tokenType ->
            assertTrue(
                highlighter.getTokenHighlights(tokenType).isNotEmpty(),
                "$tokenType has no syntax color",
            )
        }
    }

    @Test
    fun `every category has a unique default foreground color`() {
        val keys = listOf(
            PvmSyntaxHighlighter.KEYWORD,
            PvmSyntaxHighlighter.CONTROL_FLOW,
            PvmSyntaxHighlighter.UPASARGA,
            PvmSyntaxHighlighter.SANADI,
            PvmSyntaxHighlighter.LAKARA,
            PvmSyntaxHighlighter.TING_AFFIX,
            PvmSyntaxHighlighter.SUP_AFFIX,
            PvmSyntaxHighlighter.VIKARANA,
            PvmSyntaxHighlighter.AGAMA,
            PvmSyntaxHighlighter.KRT_AFFIX,
            PvmSyntaxHighlighter.TADDHITA_AFFIX,
            PvmSyntaxHighlighter.STRI_AFFIX,
            PvmSyntaxHighlighter.LOPA,
            PvmSyntaxHighlighter.DERIVATION_MARKER,
            PvmSyntaxHighlighter.ABHYASA_AFFIX,
            PvmSyntaxHighlighter.INTERJECTION,
            PvmSyntaxHighlighter.PRAKRIYA_DECLARATION,
            PvmSyntaxHighlighter.PRAKRIYA_REFERENCE,
            PvmSyntaxHighlighter.DOMAIN_DECLARATION,
            PvmSyntaxHighlighter.DOMAIN_REFERENCE,
            PvmSyntaxHighlighter.NOMINAL_STEM,
            PvmSyntaxHighlighter.NUMBER,
            PvmSyntaxHighlighter.DHATU,
            PvmSyntaxHighlighter.PHALA,
            PvmSyntaxHighlighter.IDENTIFIER,
            PvmSyntaxHighlighter.OPERATOR,
            PvmSyntaxHighlighter.DELIMITER,
            PvmSyntaxHighlighter.COMMENT,
            PvmSyntaxHighlighter.BAD_CHARACTER,
        )
        val colors = keys.map { requireNotNull(it.defaultAttributes.foregroundColor) }
        assertEquals(colors.size, colors.distinct().size, "Default category colors must be unique")
    }

    @Test
    fun `lexer emits every declared lexical category`() {
        val source = """
            प्रक्रिया यदि प्र सन् लट् तिप् सुँ शप् नुम् क्त मतुप् टाप् लुक् उणादि सुच् अहो पञ्चन् भू फल राम + ( ) ॥ # टिप्पणी
            @
        """.trimIndent()
        val lexer = PvmLexerAdapter()
        lexer.start(source, 0, source.length, 0)
        val emitted = mutableSetOf<IElementType>()
        while (lexer.tokenType != null) {
            emitted += requireNotNull(lexer.tokenType)
            lexer.advance()
        }

        COLORABLE_TOKEN_TYPES.forEach { tokenType ->
            assertTrue(tokenType in emitted, "$tokenType was not emitted by the coverage sample")
        }
    }

    private companion object {
        val COLORABLE_TOKEN_TYPES = listOf(
            PvmTokenTypes.KEYWORD,
            PvmTokenTypes.CONTROL_FLOW,
            PvmTokenTypes.UPASARGA,
            PvmTokenTypes.SANADI,
            PvmTokenTypes.LAKARA,
            PvmTokenTypes.TING_AFFIX,
            PvmTokenTypes.SUP_AFFIX,
            PvmTokenTypes.VIKARANA,
            PvmTokenTypes.AGAMA,
            PvmTokenTypes.KRT_AFFIX,
            PvmTokenTypes.TADDHITA_AFFIX,
            PvmTokenTypes.STRI_AFFIX,
            PvmTokenTypes.LOPA,
            PvmTokenTypes.DERIVATION_MARKER,
            PvmTokenTypes.ABHYASA_AFFIX,
            PvmTokenTypes.INTERJECTION,
            PvmTokenTypes.NUMBER,
            PvmTokenTypes.DHATU,
            PvmTokenTypes.PHALA,
            PvmTokenTypes.IDENTIFIER,
            PvmTokenTypes.OPERATOR,
            PvmTokenTypes.LEFT_PAREN,
            PvmTokenTypes.RIGHT_PAREN,
            PvmTokenTypes.DELIMITER,
            PvmTokenTypes.COMMENT,
            PvmTokenTypes.BAD_CHARACTER,
        )
    }

    @Test
    fun `brace matcher pairs only real parentheses`() {
        val pairs = PvmPairedBraceMatcher().pairs

        assertEquals(1, pairs.size)
        assertEquals(PvmTokenTypes.LEFT_PAREN, pairs.single().leftBraceType)
        assertEquals(PvmTokenTypes.RIGHT_PAREN, pairs.single().rightBraceType)
        assertTrue(pairs.none {
            it.leftBraceType == PvmTokenTypes.OPERATOR || it.rightBraceType == PvmTokenTypes.OPERATOR
        })
    }
}
