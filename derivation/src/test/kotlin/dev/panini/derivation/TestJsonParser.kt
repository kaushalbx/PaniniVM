package dev.panini.derivation

/** Minimal standards-based JSON parser used by data-driven tests without regex tokenization. */
internal object TestJsonParser {
    fun parse(text: String): Any? = Parser(text).parseDocument()

    private class Parser(private val source: String) {
        private var index = 0

        fun parseDocument(): Any? {
            val value = parseValue()
            whitespace()
            require(index == source.length) { "Unexpected JSON content at offset $index" }
            return value
        }

        private fun parseValue(): Any? {
            whitespace()
            require(index < source.length) { "Unexpected end of JSON" }
            return when (source[index]) {
                '{' -> parseObject()
                '[' -> parseArray()
                '"' -> parseString()
                't' -> literal("true", true)
                'f' -> literal("false", false)
                'n' -> literal("null", null)
                else -> parseNumber()
            }
        }

        private fun parseObject(): Map<String, Any?> {
            index++
            val result = linkedMapOf<String, Any?>()
            whitespace()
            if (take('}')) return result
            while (true) {
                whitespace()
                val key = parseString()
                whitespace()
                expect(':')
                result[key] = parseValue()
                whitespace()
                if (take('}')) return result
                expect(',')
            }
        }

        private fun parseArray(): List<Any?> {
            index++
            val result = mutableListOf<Any?>()
            whitespace()
            if (take(']')) return result
            while (true) {
                result += parseValue()
                whitespace()
                if (take(']')) return result
                expect(',')
            }
        }

        private fun parseString(): String {
            expect('"')
            val result = StringBuilder()
            while (index < source.length) {
                when (val char = source[index++]) {
                    '"' -> return result.toString()
                    '\\' -> {
                        require(index < source.length) { "Incomplete JSON escape" }
                        result.append(
                            when (val escaped = source[index++]) {
                                '"', '\\', '/' -> escaped
                                'b' -> '\b'
                                'f' -> '\u000c'
                                'n' -> '\n'
                                'r' -> '\r'
                                't' -> '\t'
                                'u' -> source.substring(index, index + 4).toInt(16).toChar().also { index += 4 }
                                else -> error("Invalid JSON escape \\$escaped")
                            },
                        )
                    }
                    else -> result.append(char)
                }
            }
            error("Unterminated JSON string")
        }

        private fun parseNumber(): Number {
            val start = index
            while (index < source.length && source[index] in "-+0123456789.eE") index++
            val token = source.substring(start, index)
            return token.toLongOrNull() ?: token.toDoubleOrNull() ?: error("Invalid JSON value at offset $start")
        }

        private fun <T> literal(token: String, value: T): T {
            require(source.startsWith(token, index)) { "Expected '$token' at offset $index" }
            index += token.length
            return value
        }

        private fun whitespace() {
            while (index < source.length && source[index].isWhitespace()) index++
        }

        private fun take(char: Char): Boolean = index < source.length && source[index] == char && (++index > 0)

        private fun expect(char: Char) {
            require(take(char)) { "Expected '$char' at offset $index" }
        }
    }
}
