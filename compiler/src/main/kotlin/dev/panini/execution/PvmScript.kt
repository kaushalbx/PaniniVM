package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(override val text: String) : PvmScriptStatement
}

object PvmScript {
    fun parse(source: String): List<PvmScriptStatement> {
        val lines = source.lines()
            .map(String::trim)
            .filter { it.isNotEmpty() && !it.startsWith("#") && !it.startsWith("//") }
        val statements = mutableListOf<PvmScriptStatement>()
        var index = 0
        while (index < lines.size) {
            val line = lines[index]
            if (line.startsWith("यावत्")) {
                val body = lines.getOrNull(index + 1)
                require(body?.startsWith("तावत्") == true) {
                    "A yāvat condition must be followed by its tāvat body."
                }
                statements += PvmScriptStatement.Sentence(
                    "${line.removeSuffix("।").trimEnd()} $body",
                )
                index += 2
            } else {
                require(!line.startsWith("तावत्")) {
                    "A tāvat body requires a preceding yāvat condition."
                }
                statements += PvmScriptStatement.Sentence(line)
                index++
            }
        }
        return statements
    }
}
