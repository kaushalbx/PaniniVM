package dev.panini.execution

/** Performs only source-level comment removal and Sanskrit sentence-boundary discovery. */
internal object PvmSourceScanner {
    fun stripComment(line: String): String {
        val hash = line.indexOf('#')
        val slash = line.indexOf("//")
        val boundary = listOf(hash, slash).filter { it >= 0 }.minOrNull() ?: return line
        return line.substring(0, boundary)
    }

    fun sentences(source: String): Sequence<String> =
        Regex("""[^।॥]+[।॥]*""").findAll(source).map { it.value.trim() }.filter(String::isNotEmpty)
}
