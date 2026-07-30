package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(override val text: String) : PvmScriptStatement
}

object PvmScript {
    fun parse(source: String): List<PvmScriptStatement> {
        val sanitizedLines = source.lines()
            .map { line ->
                val hashIdx = line.indexOf('#')
                val slashIdx = line.indexOf("//")
                val commentIdx = when {
                    hashIdx != -1 && slashIdx != -1 -> minOf(hashIdx, slashIdx)
                    hashIdx != -1 -> hashIdx
                    else -> slashIdx
                }
                if (commentIdx != -1) line.substring(0, commentIdx) else line
            }
            .map(String::trim)
            .filter(String::isNotEmpty)

        if (sanitizedLines.isEmpty()) return emptyList()

        val joinedText = sanitizedLines.joinToString(" ")
        val sentenceRegex = Regex("""[^।॥]+[।॥]*""")
        return sentenceRegex.findAll(joinedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .map(PvmScriptStatement::Sentence)
            .toList()
    }
}

