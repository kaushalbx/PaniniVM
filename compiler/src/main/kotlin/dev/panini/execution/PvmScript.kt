package dev.panini.execution

sealed interface PvmScriptStatement {
    data class Sentence(val text: String) : PvmScriptStatement
    data class While(
        val condition: String,
        val body: List<Sentence>,
    ) : PvmScriptStatement
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
            if (!line.startsWith("यावत् ")) {
                statements += PvmScriptStatement.Sentence(line)
                index++
                continue
            }

            val condition = sentence(line.removePrefix("यावत् "))
            index++
            require(index < lines.size && lines[index].startsWith("तावत् ")) {
                "यावत्-clause must be followed by a तावत् body clause."
            }
            val body = mutableListOf<PvmScriptStatement.Sentence>()
            body += PvmScriptStatement.Sentence(sentence(lines[index].removePrefix("तावत् ")))
            index++
            while (index < lines.size && lines[index].startsWith("ततः ")) {
                body += PvmScriptStatement.Sentence(sentence(lines[index].removePrefix("ततः ")))
                index++
            }
            statements += PvmScriptStatement.While(condition, body)
        }
        return statements
    }

    private fun sentence(text: String): String = text.trim().let {
        if (it.endsWith("।")) it else "$it ।"
    }
}
