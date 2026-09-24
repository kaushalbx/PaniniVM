package dev.panini.execution

/** Maps whitespace-insensitive token text back to exact source offsets. */
internal class SourceTextMap(source: String) {
    private val compact = buildString(source.length) {
        source.forEach { if (!it.isWhitespace()) append(it) }
    }
    private val sourceOffsets = buildList(source.length) {
        source.forEachIndexed { index, character -> if (!character.isWhitespace()) add(index) }
    }

    fun locate(text: String, startAt: Int = 0): LocatedText? {
        val needle = text.filterNot(Char::isWhitespace)
        val compactStart = compact.indexOf(needle, startAt)
        if (compactStart < 0 || needle.isEmpty()) return null
        val compactEnd = compactStart + needle.length - 1
        return LocatedText(
            span = SourceSpan(sourceOffsets[compactStart], sourceOffsets[compactEnd] + 1),
            nextCompactOffset = compactEnd + 1,
        )
    }
}

data class SourceSpan(val start: Int, val endExclusive: Int) {
    init {
        require(start >= 0 && endExclusive >= start)
    }

    val length: Int get() = endExclusive - start
}

internal data class LocatedText(val span: SourceSpan, val nextCompactOffset: Int)
