package dev.panini.parser.ast

import dev.panini.parser.ast.ParsedSubanta

sealed interface ParsedPada {

    data class Subanta(
        val value: ParsedSubanta,
    ) : ParsedPada

    data class Coordination(
        val members: List<ParsedSubanta>,
    ) : ParsedPada {
        init {
            require(members.size >= 2) {
                "A coordinated expression requires at least two members."
            }
        }
    }

    data class AvyayaKridanta(
        val dhatu: String,
        val vikarana: String?,
        val pratyaya: String,
    ) : ParsedPada

    data class Avyaya(
        val value: String,
    ) : ParsedPada
}
