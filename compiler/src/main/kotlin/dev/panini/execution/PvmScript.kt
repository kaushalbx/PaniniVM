package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(override val text: String) : PvmScriptStatement
}

object PvmScript {
    fun parse(source: String): List<PvmScriptStatement> =
        source.lines()
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }
            .map(PvmScriptStatement::Sentence)
}
