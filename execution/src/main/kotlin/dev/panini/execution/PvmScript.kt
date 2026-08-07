package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(override val text: String) : PvmScriptStatement

    /**
     * A named kriyā definition using the संज्ञा-सूत्र pattern:
     *
     *     गुण् + ल्युट् + सुँ इति संज्ञा ।
     *         संख्या + अम् संख्या + अम् च गुण् + णिच् + लोट् + सिप् ।
     *     इति ॥
     *
     * [nameSegmented] is the segmented prātipadika form (e.g. "गुण् + ल्युट् + सुँ").
     * [body] contains the vākya sentences that form the procedure.
     */
    data class SamjnaDefinition(
        val nameSegmented: String,
        val body: List<Sentence>,
        override val text: String,
    ) : PvmScriptStatement
}

object PvmScript {

    /** Marker at the end of a saṃjñā header line. */
    private const val SAMJNA_HEADER_MARKER = "इति संज्ञा"

    /** Marker that closes a saṃjñā definition block. */
    private const val SAMJNA_BLOCK_END = "इति"

    fun parse(source: String): List<PvmScriptStatement> {
        val rawLines = source.lines()

        val samjnaDefinitions = mutableListOf<PvmScriptStatement.SamjnaDefinition>()
        val nonSamjnaLines = mutableListOf<String>()

        var inBlock = false
        var currentName = ""
        var currentBodyLines = mutableListOf<String>()
        var currentBlockText = mutableListOf<String>()

        for (line in rawLines) {
            val stripped = stripComment(line).trim()

            if (!inBlock) {
                val headerName = extractSamjnaHeaderName(stripped)
                if (headerName != null) {
                    inBlock = true
                    currentName = headerName
                    currentBodyLines = mutableListOf()
                    currentBlockText = mutableListOf(line)
                } else {
                    nonSamjnaLines += line
                }
            } else {
                currentBlockText += line
                val endsWithDoubleDanda = isSamjnaBlockEnd(stripped)
                if (stripped.isNotEmpty() && stripped != "॥" && stripped != "इति ॥") {
                    currentBodyLines += line
                }
                if (endsWithDoubleDanda) {
                    val bodyText = currentBodyLines
                        .map { stripComment(it).trim() }
                        .filter { it.isNotEmpty() }
                        .joinToString(" ")
                    val bodySentences = parseSentences(bodyText)
                    samjnaDefinitions += PvmScriptStatement.SamjnaDefinition(
                        nameSegmented = currentName,
                        body = bodySentences,
                        text = currentBlockText.joinToString("\n"),
                    )
                    inBlock = false
                }
            }
        }

        val sanitizedLines = nonSamjnaLines
            .map { stripComment(it).trim() }
            .filter { it.isNotEmpty() }

        val sentences = if (sanitizedLines.isEmpty()) {
            emptyList()
        } else {
            parseSentences(sanitizedLines.joinToString(" "))
        }

        return samjnaDefinitions + sentences
    }

    internal fun extractSamjnaHeaderName(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return null

        // Support "<name> इति संज्ञा ।" (legacy)
        val markerIdx = trimmed.indexOf(SAMJNA_HEADER_MARKER)
        if (markerIdx > 0) {
            return trimmed.substring(0, markerIdx).trim().ifEmpty { null }
        }

        // Pure Pāṇinian header: "<name> + सुँ ।" or "<name> + सुँ संज्ञा ।"
        // Must end with a single daṇḍa (or start of block), contain nominal nominative affix "+ सुँ",
        // and must NOT contain finite verb lakāra affixes (e.g. लोट्, लट्).
        val hasNominalAffix = trimmed.contains("+ सुँ") || trimmed.contains("+ प्रथमा")
        val hasVerbAffix = trimmed.contains("लोट्") || trimmed.contains("लट्") || trimmed.contains("लङ्") || trimmed.contains("विधिलिङ्")
        if (hasNominalAffix && !hasVerbAffix) {
            val nameText = trimmed.substringBefore("संज्ञा")
                .trimEnd('।', '॥', ' ')
                .trim()
            return nameText.ifEmpty { null }
        }

        return null
    }

    internal fun isSamjnaBlockEnd(line: String): Boolean {
        val trimmed = line.trim()
        // Block ends on standalone "॥" or "इति ॥" or line ending with "॥"
        return trimmed == "॥" || trimmed.endsWith("॥") || trimmed.contains("इति ॥")
    }

    private fun stripComment(line: String): String {
        val hashIdx = line.indexOf('#')
        val slashIdx = line.indexOf("//")
        val commentIdx = when {
            hashIdx != -1 && slashIdx != -1 -> minOf(hashIdx, slashIdx)
            hashIdx != -1 -> hashIdx
            else -> slashIdx
        }
        return if (commentIdx != -1) line.substring(0, commentIdx) else line
    }

    private fun parseSentences(joinedText: String): List<PvmScriptStatement.Sentence> {
        if (joinedText.isBlank()) return emptyList()
        val sentenceRegex = Regex("""[^।॥]+[।॥]*""")
        return sentenceRegex.findAll(joinedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .map(PvmScriptStatement::Sentence)
            .toList()
    }
}

