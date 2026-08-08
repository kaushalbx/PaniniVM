package dev.panini.execution

import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.parser.PaniniParser

/** Classifies terminal double-daṇḍa lines in a PVM saṃjñā block. */
internal object PvmBlockBoundary {
    private const val TERMINATOR = "॥"
    private val parser = PaniniParser()

    fun closes(line: String): Boolean = line.trim().endsWith(TERMINATOR)

    fun carriesBody(line: String): Boolean {
        val trimmed = line.trim()
        if (!closes(trimmed)) return trimmed.isNotEmpty()
        val beforeTerminator = trimmed.removeSuffix(TERMINATOR).trim()
        if (beforeTerminator.isEmpty()) return false
        val padas = parser.parseOrNull(beforeTerminator)
            ?.vakyas
            ?.flatMap { it.padas }
            ?: return true
        return padas.singleOrNull()?.let { it !is AvyayaPada || it.form != "इति" } ?: true
    }
}
