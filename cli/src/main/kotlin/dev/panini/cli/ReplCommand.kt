package dev.panini.cli

sealed class ReplCommand {
    object Help : ReplCommand()
    object Exit : ReplCommand()
    object ToggleTrace : ReplCommand()
    data class LookupDhatu(val query: String) : ReplCommand()
    data class CompileScript(val filePath: String, val className: String) : ReplCommand()
    data class DecodeNumeral(val numeral: String) : ReplCommand()
    data class EncodeNumber(val value: Long, val system: String?) : ReplCommand()
    data class EvalUtterance(val utterance: String) : ReplCommand()

    companion object {
        fun parse(line: String): ReplCommand {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return EvalUtterance("")
            if (trimmed.startsWith(":")) {
                val parts = trimmed.substring(1).split("\\s+".toRegex())
                val cmd = parts[0].lowercase()
                return when (cmd) {
                    "help", "सहायता" -> Help
                    "exit", "quit", "त्यज" -> Exit
                    "trace", "अनुदर्शनम्" -> ToggleTrace
                    "dhatu", "धातु" -> LookupDhatu(parts.getOrNull(1) ?: "")
                    "compile", "सङ्कलनम्" -> CompileScript(parts.getOrNull(1) ?: "", parts.getOrNull(2) ?: "GeneratedProgram")
                    "num", "सङ्ख्या", "संख्या" -> DecodeNumeral(parts.getOrNull(1) ?: "")
                    "encode", "सङ्केतनम्", "संकेतनम्" -> {
                        val num = parts.getOrNull(1)?.toLongOrNull() ?: -1L
                        val system = parts.getOrNull(2)?.lowercase()
                        EncodeNumber(num, system)
                    }
                    else -> Help
                }
            }
            return EvalUtterance(trimmed)
        }
    }
}
