package dev.panini.parser.ast

import dev.panini.parser.ast.ParsedNominalBase

data class ParsedSubanta(
    val base: ParsedNominalBase,
    val supPratyaya: String?,
) {
    val segmentedText: String
        get() = supPratyaya?.let { "${base.segmentedText} + $it" }
            ?: base.segmentedText
}
