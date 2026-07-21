package dev.panini.parser.ast

import dev.panini.parser.ast.SimpleNominalKind

sealed interface ParsedNominalBase {

    val segmentedText: String

    data class Simple(
        val value: String,
        val kind: SimpleNominalKind,
    ) : ParsedNominalBase {
        override val segmentedText: String
            get() = value
    }

    data class Samasa(
        val members: List<ParsedNominalBase>,
        val separator: String,
    ) : ParsedNominalBase {
        init {
            require(members.size >= 2) {
                "A samāsa requires at least two members."
            }
        }

        override val segmentedText: String
            get() = members.joinToString(separator) { it.segmentedText }
    }

    data class Kridanta(
        val upasargas: List<String> = emptyList(),
        val dhatu: String,
        val vikarana: String?,
        val pratyaya: String,
    ) : ParsedNominalBase {
        override val segmentedText: String
            get() = buildList {
                addAll(upasargas)
                add(dhatu)
                vikarana?.let(::add)
                add(pratyaya)
            }.joinToString(" + ")
    }

    data class Taddhita(
        val prakriti: ParsedNominalBase,
        val pratyaya: String,
    ) : ParsedNominalBase {
        override val segmentedText: String
            get() = "${prakriti.segmentedText} + $pratyaya"
    }

    data class Stri(
        val prakriti: ParsedNominalBase,
        val pratyaya: String,
    ) : ParsedNominalBase {
        override val segmentedText: String
            get() = "${prakriti.segmentedText} + $pratyaya"
    }
}
