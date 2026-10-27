package dev.panini.plugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import dev.panini.parser.VyakaranamLexer
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.dhatupatha.DhatuPathaRegistration
import org.antlr.v4.kotlinruntime.CharStreams

class PvmLexerAdapter : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0

    private data class TokenSpan(val start: Int, val end: Int, val type: IElementType)
    private var spans: List<TokenSpan> = emptyList()
    private var spanIndex: Int = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.spanIndex = 0

        val totalLength = endOffset - startOffset
        if (totalLength <= 0) {
            spans = emptyList()
            return
        }

        val text = buffer.subSequence(startOffset, endOffset).toString()
        val antlrTokens = try {
            val lexer = VyakaranamLexer(CharStreams.fromString(text))
            lexer.allTokens
        } catch (_: Throwable) {
            emptyList()
        }

        val computedSpans = mutableListOf<TokenSpan>()
        var cursor = 0
        fun addGapSpans(from: Int, to: Int) {
            var runStart = from
            while (runStart < to) {
                val whitespace = text[runStart].isWhitespace()
                var runEnd = runStart + 1
                while (runEnd < to && text[runEnd].isWhitespace() == whitespace) runEnd++
                computedSpans.add(TokenSpan(
                    startOffset + runStart,
                    startOffset + runEnd,
                    if (whitespace) PvmTokenTypes.WHITE_SPACE else PvmTokenTypes.BAD_CHARACTER,
                ))
                runStart = runEnd
            }
        }

        for (token in antlrTokens) {
            val tStart = token.startIndex.coerceIn(0, totalLength)
            val tEnd = (token.stopIndex + 1).coerceIn(tStart, totalLength)

            // Fill gap before token with WHITE_SPACE
            if (cursor < tStart) {
                addGapSpans(cursor, tStart)
            }

            if (tEnd > tStart) {
                computedSpans.add(TokenSpan(
                    startOffset + tStart,
                    startOffset + tEnd,
                    mapAntlrTokenType(token.type, token.text.orEmpty()),
                ))
                cursor = tEnd
            }
        }

        // Fill trailing gap if any
        if (cursor < totalLength) {
            addGapSpans(cursor, totalLength)
        }

        spans = if (computedSpans.isNotEmpty()) computedSpans else listOf(TokenSpan(startOffset, endOffset, PvmTokenTypes.IDENTIFIER))
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = if (spanIndex < spans.size) spans[spanIndex].type else null
    override fun getTokenStart(): Int = if (spanIndex < spans.size) spans[spanIndex].start else endOffset
    override fun getTokenEnd(): Int = if (spanIndex < spans.size) spans[spanIndex].end else endOffset
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (spanIndex < spans.size) {
            spanIndex++
        }
    }

    private fun mapAntlrTokenType(type: Int, text: String): IElementType {
        val symbolicName = runCatching { vocabulary.getSymbolicName(type) }.getOrNull()
            ?: return PvmTokenTypes.IDENTIFIER

        val name = symbolicName

        return when {
            name == "PLUS" || name == "SAMASA_SEPARATOR" -> PvmTokenTypes.OPERATOR
            name == "LPAREN" -> PvmTokenTypes.LEFT_PAREN
            name == "RPAREN" -> PvmTokenTypes.RIGHT_PAREN
            name in setOf("COMMA", "DANDA", "DOUBLE_DANDA") -> PvmTokenTypes.DELIMITER
            name in setOf("YADI", "TARHI", "ANYATHA", "YAVAT", "TAVAT") -> PvmTokenTypes.CONTROL_FLOW
            name in setOf(
                "PRA", "PARAA", "APA", "SAM", "ANUU", "AVA", "NIS", "DUS", "VI",
                "AANG", "NI", "ADHI", "ATI", "SU", "UD", "ABHI", "PRATI", "PARI", "UPA", "ANTAR"
            ) -> PvmTokenTypes.UPASARGA
            name in setOf("SAN", "KYACH", "KAAMYACH", "KYANG", "KYASH", "NIC", "YAN", "YUK_SAN") -> PvmTokenTypes.SANADI
            name in setOf("LAT", "LIT", "LUT", "LRT", "LET", "LOT", "LANG", "LIN", "LUNG", "LRNG") -> PvmTokenTypes.LAKARA
            name in TING_TOKEN_NAMES -> PvmTokenTypes.TING_AFFIX
            name.startsWith("SUP_") -> PvmTokenTypes.SUP_AFFIX
            name in VIKARANA_TOKEN_NAMES -> PvmTokenTypes.VIKARANA
            name in AGAMA_TOKEN_NAMES -> PvmTokenTypes.AGAMA
            name in KRT_TOKEN_NAMES -> PvmTokenTypes.KRT_AFFIX
            name in TADDHITA_TOKEN_NAMES -> PvmTokenTypes.TADDHITA_AFFIX
            name in STRI_TOKEN_NAMES -> PvmTokenTypes.STRI_AFFIX
            name in setOf("LUK", "SHLU", "LUP", "ALUK") -> PvmTokenTypes.LOPA
            name == "UNADI" -> PvmTokenTypes.DERIVATION_MARKER
            name == "SUC" -> PvmTokenTypes.ABHYASA_AFFIX
            name == "INTERJECTION" -> PvmTokenTypes.INTERJECTION
            name in setOf(
                "HE", "BHOH", "CHA", "VAA", "ATHA", "TATAH", "ANANTARAM", "KINTU",
                "ATAH", "YATAH", "MAA", "NA", "ITI", "API", "EVA", "TU_AVYAYA",
                "HI", "KHALU", "NANU", "YATHA", "TATHA", "YADA", "TADA", "YATRA",
                "TATRA", "KADA", "KUTRA", "SARVATRA", "KATHAM", "KUTAH", "KRPAYA",
                "SAHASAA", "SHANAIH", "PUNAH", "NYUNATAYA", "ADYA", "SHVAH", "HYAH",
                "YADI", "TARHI", "ANYATHA", "YAVAT", "TAVAT",
                "KATAPAYADI", "ARYABHATIYA", "BHUTASAMKHYA", "PRAKRIYA_NOUN", "NAAMA",
                "KASHCHIT", "PRATYEKAH", "ADHIKA", "UNA",
                "SAKRIT", "DVIH", "TRIH", "CHATUH", "KRITVAS"
            ) -> PvmTokenTypes.KEYWORD
            name == "WS" -> PvmTokenTypes.WHITE_SPACE
            name == "LINE_COMMENT" -> PvmTokenTypes.COMMENT
            name == "IDENTIFIER" && PrimitiveSankhya.fromAnnotatedPratipadika(text) != null -> PvmTokenTypes.NUMBER
            name == "IDENTIFIER" && text == "फल" -> PvmTokenTypes.PHALA
            name == "IDENTIFIER" && text in dhatuSurfaces -> PvmTokenTypes.DHATU
            else -> PvmTokenTypes.IDENTIFIER
        }
    }

    private companion object {
        val vocabulary = VyakaranamLexer(CharStreams.fromString("")).vocabulary
        val dhatuSurfaces by lazy(LazyThreadSafetyMode.PUBLICATION) {
            DhatuPathaRegistration.allDhatus.flatMapTo(mutableSetOf()) {
                listOf(it.upadesha, it.sourceSurface)
            }
        }
        val TING_TOKEN_NAMES = setOf(
            "TIP", "TAS", "JHI", "SIP", "THAS", "THA", "MIP", "VAS", "MAS",
            "TA", "ATAAM", "JHA", "THAS_A", "ATHAAM", "DHVAM", "IT", "VAHI", "MAHING",
        )
        val VIKARANA_TOKEN_NAMES = setOf(
            "SHAP", "SHYAN", "SHNU", "SHNAM", "SHNA", "U_VIKARANA", "YAK", "SHAH",
            "SYA", "TAS_VIKARANA", "CLI", "SIC", "ANG", "CHANG", "KSA",
        )
        val AGAMA_TOKEN_NAMES = setOf(
            "AT", "IIT_AGAMA", "NUM", "TUK", "MUT", "NUT", "YASUT", "SIYUT",
            "SUK", "RUK", "RIK", "PUK", "YUK", "VUK",
        )
        val KRT_TOKEN_NAMES = setOf(
            "KTA", "KTAVATU", "TAVYAT", "ANIYAR", "YAT", "NYAT", "KYAP", "SHATR",
            "SHANACH", "GHANJ", "LYUT", "NVUL", "TRICH", "ANIN", "KHAL", "KWIP",
            "KTIN", "AC", "AP", "KA", "NIN", "NINI", "IN_KRT", "TI_KRT", "TRA",
            "ITRA", "ISHNUCH", "UK", "KTVA", "LYAP", "TUMUN", "NAMUL", "KASUN", "KTVOS",
        )
        val TADDHITA_TOKEN_NAMES = setOf(
            "MATUP", "VATUP", "MAT", "INI", "TVA", "TAL", "TARAP", "TAMAP", "MAYAT",
            "PRATYAYA_MA", "PRATYAYA_TAMA", "PRATYAYA_TIYA", "PRATYAYA_AMACH", "TASIL",
            "AN", "INJ", "DHAK", "THAJ", "CHHA", "KAN", "AYANA", "IYA", "INA", "TYAP",
            "TYA", "HA", "DAA", "THAAL", "THAMU", "VAT", "DHAA",
        )
        val STRI_TOKEN_NAMES = setOf("TAAP", "DAAP", "CHAAP", "NEEP", "NEESH", "NEEN", "UUNG", "TICH")
    }
}
