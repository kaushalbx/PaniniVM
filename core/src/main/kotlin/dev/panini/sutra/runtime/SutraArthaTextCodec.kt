package dev.panini.sutra.runtime

enum class SutraArthaTextDiagnosticCode {
    UNEXPECTED_END,
    UNEXPECTED_TOKEN,
    INVALID_ESCAPE,
    INVALID_NUMBER,
    INVALID_REFERENCE,
    DUPLICATE_FIELD,
    TRAILING_INPUT,
}

data class SutraArthaTextDiagnostic(
    val code: SutraArthaTextDiagnosticCode,
    val position: Int,
    val message: String,
)

sealed interface SutraArthaTextDecoding {
    data class Success(val value: SutraArthaValue) : SutraArthaTextDecoding
    data class Invalid(val diagnostics: List<SutraArthaTextDiagnostic>) : SutraArthaTextDecoding
}

/**
 * Canonical evaluator-free text for recursive sūtra meaning:
 * t"text", s"symbol", n42, btrue, r"sutra", sequences, and records.
 */
object SutraArthaTextCodec {
    fun encode(value: SutraArthaValue): String = buildString {
        appendValue(value)
    }

    fun decode(source: String): SutraArthaTextDecoding {
        val parser = Parser(source)
        val value = parser.parseValue()
        parser.skipWhitespace()
        if (value != null && parser.position < source.length) {
            parser.report(
                SutraArthaTextDiagnosticCode.TRAILING_INPUT,
                "Unexpected trailing input.",
            )
        }
        return if (value != null && parser.diagnostics.isEmpty()) {
            SutraArthaTextDecoding.Success(value)
        } else {
            SutraArthaTextDecoding.Invalid(parser.diagnostics)
        }
    }

    private fun StringBuilder.appendValue(value: SutraArthaValue) {
        when (value) {
            is SutraArthaValue.Text -> {
                append('t')
                appendQuoted(value.value)
            }
            is SutraArthaValue.Symbol -> {
                append('s')
                appendQuoted(value.name)
            }
            is SutraArthaValue.Number -> append('n').append(value.value)
            is SutraArthaValue.Truth -> append(if (value.value) "btrue" else "bfalse")
            is SutraArthaValue.SutraReference -> {
                append('r')
                appendQuoted(value.id.value)
            }
            is SutraArthaValue.Sequence -> {
                append('[')
                value.values.forEachIndexed { index, item ->
                    if (index > 0) append(',')
                    appendValue(item)
                }
                append(']')
            }
            is SutraArthaValue.Record -> {
                append('{')
                value.fields.toSortedMap().entries.forEachIndexed { index, (name, item) ->
                    if (index > 0) append(',')
                    appendQuoted(name)
                    append(':')
                    appendValue(item)
                }
                append('}')
            }
        }
    }

    private fun StringBuilder.appendQuoted(value: String) {
        append('"')
        value.forEach { character ->
            when (character) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (character.code < 0x20) {
                    append("\\u")
                    append(character.code.toString(16).padStart(4, '0'))
                } else {
                    append(character)
                }
            }
        }
        append('"')
    }

    private class Parser(
        private val source: String,
    ) {
        val diagnostics = mutableListOf<SutraArthaTextDiagnostic>()
        var position: Int = 0
            private set

        fun parseValue(): SutraArthaValue? {
            skipWhitespace()
            if (position >= source.length) {
                report(SutraArthaTextDiagnosticCode.UNEXPECTED_END, "Expected a semantic value.")
                return null
            }
            return when (source[position]) {
                't' -> parseQuoted()?.let(SutraArthaValue::Text)
                's' -> parseQuoted()?.let(SutraArthaValue::Symbol)
                'n' -> parseNumber()
                'b' -> parseTruth()
                'r' -> parseReference()
                '[' -> parseSequence()
                '{' -> parseRecord()
                else -> {
                    report(
                        SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN,
                        "Unexpected token '${source[position]}'.",
                    )
                    null
                }
            }
        }

        fun skipWhitespace() {
            while (position < source.length && source[position].isWhitespace()) position++
        }

        fun report(code: SutraArthaTextDiagnosticCode, message: String) {
            diagnostics += SutraArthaTextDiagnostic(code, position, message)
        }

        private fun parseQuoted(): String? {
            position++
            skipWhitespace()
            return parseRawQuoted()
        }

        private fun parseRawQuoted(): String? {
            if (!consume('"')) {
                report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected quoted text.")
                return null
            }
            val value = StringBuilder()
            while (position < source.length) {
                val character = source[position++]
                when (character) {
                    '"' -> return value.toString()
                    '\\' -> {
                        if (position >= source.length) {
                            report(SutraArthaTextDiagnosticCode.UNEXPECTED_END, "Unfinished escape.")
                            return null
                        }
                        when (val escaped = source[position++]) {
                            '\\', '"' -> value.append(escaped)
                            'n' -> value.append('\n')
                            'r' -> value.append('\r')
                            't' -> value.append('\t')
                            'u' -> {
                                val end = position + 4
                                val digits = source.substring(position, end.coerceAtMost(source.length))
                                val code = digits.takeIf { it.length == 4 }
                                    ?.toIntOrNull(16)
                                if (code == null) {
                                    report(
                                        SutraArthaTextDiagnosticCode.INVALID_ESCAPE,
                                        "Unicode escape requires four hexadecimal digits.",
                                    )
                                    return null
                                }
                                value.append(code.toChar())
                                position = end
                            }
                            else -> {
                                report(
                                    SutraArthaTextDiagnosticCode.INVALID_ESCAPE,
                                    "Unknown escape '\\$escaped'.",
                                )
                                return null
                            }
                        }
                    }
                    else -> value.append(character)
                }
            }
            report(SutraArthaTextDiagnosticCode.UNEXPECTED_END, "Unterminated quoted text.")
            return null
        }

        private fun parseNumber(): SutraArthaValue.Number? {
            position++
            val start = position
            if (position < source.length && source[position] == '-') position++
            while (position < source.length && source[position].isDigit()) position++
            val number = source.substring(start, position).toLongOrNull()
            if (number == null) {
                report(SutraArthaTextDiagnosticCode.INVALID_NUMBER, "Invalid 64-bit number.")
                return null
            }
            return SutraArthaValue.Number(number)
        }

        private fun parseTruth(): SutraArthaValue.Truth? {
            return when {
                source.startsWith("btrue", position) -> {
                    position += 5
                    SutraArthaValue.Truth(true)
                }
                source.startsWith("bfalse", position) -> {
                    position += 6
                    SutraArthaValue.Truth(false)
                }
                else -> {
                    report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected btrue or bfalse.")
                    null
                }
            }
        }

        private fun parseReference(): SutraArthaValue.SutraReference? {
            val start = position
            val id = parseQuoted() ?: return null
            return runCatching { SutraArthaValue.SutraReference(SutraId(id)) }
                .getOrElse {
                    position = start
                    report(
                        SutraArthaTextDiagnosticCode.INVALID_REFERENCE,
                        "A sūtra reference requires a non-blank identity.",
                    )
                    null
                }
        }

        private fun parseSequence(): SutraArthaValue.Sequence? {
            position++
            val values = mutableListOf<SutraArthaValue>()
            skipWhitespace()
            if (consume(']')) return SutraArthaValue.Sequence(values)
            while (true) {
                values += parseValue() ?: return null
                skipWhitespace()
                if (consume(']')) return SutraArthaValue.Sequence(values)
                if (!consume(',')) {
                    report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected ',' or ']'.")
                    return null
                }
            }
        }

        private fun parseRecord(): SutraArthaValue.Record? {
            position++
            val fields = linkedMapOf<String, SutraArthaValue>()
            skipWhitespace()
            if (consume('}')) return SutraArthaValue.Record(fields)
            while (true) {
                skipWhitespace()
                if (position >= source.length || source[position] != '"') {
                    report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected a quoted field name.")
                    return null
                }
                val name = parseRawQuoted() ?: return null
                if (name in fields) {
                    report(
                        SutraArthaTextDiagnosticCode.DUPLICATE_FIELD,
                        "Record field '$name' is declared more than once.",
                    )
                    return null
                }
                skipWhitespace()
                if (!consume(':')) {
                    report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected ':' after field name.")
                    return null
                }
                fields[name] = parseValue() ?: return null
                skipWhitespace()
                if (consume('}')) return SutraArthaValue.Record(fields)
                if (!consume(',')) {
                    report(SutraArthaTextDiagnosticCode.UNEXPECTED_TOKEN, "Expected ',' or '}'.")
                    return null
                }
            }
        }

        private fun consume(character: Char): Boolean {
            if (position >= source.length || source[position] != character) return false
            position++
            return true
        }
    }
}
