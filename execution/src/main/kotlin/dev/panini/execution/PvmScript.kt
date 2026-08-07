package dev.panini.execution

sealed interface PvmScriptStatement {
    val text: String

    data class Sentence(
        override val text: String,
        val ukti: dev.panini.vyakaranam.ast.Ukti? = null,
        val isNishedha: Boolean = false,
    ) : PvmScriptStatement

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
        val isInternal: Boolean = false,
    ) : PvmScriptStatement

    /**
     * An अधिकार-सूत्र (governing domain scope declaration):
     *
     *     गणित + अम् इति अधिकारः ।
     */
    data class AdhikaraDefinition(
        val domainSegmented: String,
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
                val rawHeaderName = extractSamjnaHeaderName(stripped)
                if (rawHeaderName != null) {
                    inBlock = true
                    currentName = rawHeaderName
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

                    val isInternalHeader = currentName.startsWith("अन्तरङ्गा ") || currentName.startsWith("अन्तरङ्ग ")
                    val cleanName = if (isInternalHeader) {
                        currentName.removePrefix("अन्तरङ्गा ").removePrefix("अन्तरङ्ग ").trim()
                    } else {
                        currentName
                    }

                    samjnaDefinitions += PvmScriptStatement.SamjnaDefinition(
                        nameSegmented = cleanName,
                        body = bodySentences,
                        text = currentBlockText.joinToString("\n"),
                        isInternal = isInternalHeader,
                    )
                    inBlock = false
                }
            }
        }

        val adhikaraDefinitions = mutableListOf<PvmScriptStatement.AdhikaraDefinition>()
        val regularNonSamjnaLines = mutableListOf<String>()

        nonSamjnaLines.forEach { line ->
            val stripped = stripComment(line).trim()
            val adhikaraDomain = extractAdhikaraDomain(stripped)
            if (adhikaraDomain != null) {
                adhikaraDefinitions += PvmScriptStatement.AdhikaraDefinition(
                    domainSegmented = adhikaraDomain,
                    text = line,
                )
            } else {
                regularNonSamjnaLines += line
            }
        }

        val sanitizedLines = regularNonSamjnaLines
            .map { stripComment(it).trim() }
            .filter { it.isNotEmpty() }

        val sentences = if (sanitizedLines.isEmpty()) {
            emptyList()
        } else {
            parseSentences(sanitizedLines.joinToString(" "))
        }

        return samjnaDefinitions + adhikaraDefinitions + sentences
    }

    internal fun isAdhikaraLine(line: String): Boolean {
        return line.contains("+ घञ्") || line.contains("अधिकार")
    }

    internal fun extractAdhikaraDomain(line: String): String? {
        val trimmed = line.trim()
        if (!isAdhikaraLine(trimmed)) return null

        val marker = if (trimmed.contains("अधि + कृ + घञ्")) {
            "अधि + कृ + घञ्"
        } else if (trimmed.contains("+ घञ्")) {
            val idx = trimmed.indexOf("+ घञ्")
            trimmed.substring(0, idx).trim()
        } else {
            "अधिकार"
        }

        var beforeAdhikara = trimmed.substringBefore(marker)
            .trimEnd('।', '॥', ' ', '+')
            .trim()
        if (beforeAdhikara.isEmpty()) return null

        if (beforeAdhikara.endsWith("इति")) {
            beforeAdhikara = beforeAdhikara.substringBeforeLast("इति").trim()
        }

        return beforeAdhikara.ifEmpty { null }
    }

    internal fun extractSamjnaHeaderName(line: String): String? {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || isAdhikaraLine(trimmed) || trimmed.contains("+ वत्") || trimmed.contains("+ मत्")) return null

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

    private val parser = dev.panini.vyakaranam.parser.PaniniParser()

    private fun parseSentences(joinedText: String): List<PvmScriptStatement.Sentence> {
        if (joinedText.isBlank()) return emptyList()
        val sentenceRegex = Regex("""[^।॥]+[।॥]*""")
        return sentenceRegex.findAll(joinedText)
            .map { it.value.trim() }
            .filter { it.isNotEmpty() }
            .map { text ->
                val trimmed = text.trim()
                val ukti = runCatching { parser.parse(trimmed) }.getOrNull()
                val isNishedha = ukti?.vakyas?.any { vakya ->
                    vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.AvyayaPada>().any { it.sourceText == "न" || it.sourceText == "मा" }
                } ?: (trimmed.startsWith("न ") || trimmed.startsWith("मा ") || trimmed.contains(" न ") || trimmed.startsWith("न+"))
                PvmScriptStatement.Sentence(text = text, ukti = ukti, isNishedha = isNishedha)
            }
            .toList()
    }
}

