package dev.panini.execution

import dev.panini.sankhya.SankhyaEvaluator

/** Semantic value types used by saṃjñā signatures and overload resolution. */
enum class SamjnaValueType {
    SANKHYA,
    SHABDA,
    SUCHI,
}

data class SamjnaParameter(
    val nameStem: String,
    val type: SamjnaValueType,
)

data class SamjnaSignature(
    val argumentType: SamjnaValueType? = null,
    val parameters: List<SamjnaParameter> = emptyList(),
    val resultType: SamjnaValueType? = null,
    val resultSchema: String? = null,
)

/**
 * Compatibility boundary that compiles surface type guards into a typed signature.
 * Runtime dispatch consumes [SamjnaSignature] and does not inspect rule-body text.
 */
object SamjnaSignatureCompiler {
    private val typeMarkers = linkedMapOf(
        SamjnaValueType.SANKHYA to listOf("सङ्ख्या + त्व", "सङ्ख्यात्व"),
        SamjnaValueType.SHABDA to listOf("शब्द + त्व", "शब्दत्व"),
        SamjnaValueType.SUCHI to listOf("सूची + त्व", "सूचीत्व"),
    )

    fun compile(body: List<PvmScriptStatement.Sentence>): SamjnaSignature {
        val parameters = body.mapNotNull(SamjnaSignatureDeclarationParser::parameter)
        val resultDeclarations = body.mapNotNull(SamjnaSignatureDeclarationParser::result)
        val resultType = resultDeclarations.singleOrNull()?.type
        val resultSchema = resultDeclarations.singleOrNull()?.schema
        val guardedTypes = body.asSequence()
            .filter { it.isNishedha }
            .mapNotNull { inferGuardType(it.text) }
            .distinct()
            .toList()
        return SamjnaSignature(
            argumentType = guardedTypes.singleOrNull(),
            parameters = parameters,
            resultType = resultType,
            resultSchema = resultSchema,
        )
    }

    fun inferGuardType(text: String): SamjnaValueType? =
        typeMarkers.entries.firstOrNull { (_, markers) -> markers.any(text::contains) }?.key
}

/** Parses grammatical signature declarations embedded at the start of a saṃjñā block. */
object SamjnaSignatureDeclarationParser {
    data class ResultDeclaration(val type: SamjnaValueType? = null, val schema: String? = null)
    private val typeSource = "(सङ्ख्या|शब्द|सूची)"
    private val outcomeSource = "(?:परिणाम|परि\\s*\\+\\s*नम्\\s*\\+\\s*घञ्)"
    private val parameterPattern = Regex(
        "^\\s*(.+?)\\s*\\+\\s*सुँ\\s+$typeSource\\s*\\+\\s*सुँ\\s+इति\\s+मान\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )
    private val resultPattern = Regex(
        "^\\s*$typeSource\\s*\\+\\s*सुँ\\s+इति\\s+$outcomeSource\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )
    private val schemaResultPattern = Regex(
        "^\\s*(.+?)\\s*\\+\\s*सुँ\\s+इति\\s+$outcomeSource\\s*\\+\\s*सुँ\\s*[।॥]?\\s*$",
    )

    fun parameter(sentence: PvmScriptStatement.Sentence): SamjnaParameter? {
        val match = parameterPattern.matchEntire(sentence.text) ?: return null
        return SamjnaParameter(match.groupValues[1].trim(), type(match.groupValues[2]))
    }

    fun result(sentence: PvmScriptStatement.Sentence): ResultDeclaration? {
        resultPattern.matchEntire(sentence.text)?.groupValues?.get(1)?.let {
            return ResultDeclaration(type = type(it))
        }
        val schema = schemaResultPattern.matchEntire(sentence.text)?.groupValues?.get(1)?.trim() ?: return null
        if (schema in setOf("सङ्ख्या", "शब्द", "सूची")) return null
        return ResultDeclaration(schema = canonicalConceptName(schema))
    }

    fun resultType(sentence: PvmScriptStatement.Sentence): SamjnaValueType? = result(sentence)?.type

    fun isDeclaration(sentence: PvmScriptStatement.Sentence): Boolean =
        parameter(sentence) != null || result(sentence) != null

    private fun type(source: String): SamjnaValueType = when (source) {
        "सङ्ख्या" -> SamjnaValueType.SANKHYA
        "शब्द" -> SamjnaValueType.SHABDA
        "सूची" -> SamjnaValueType.SUCHI
        else -> error("Unsupported saṃjñā value type: $source")
    }

    private fun canonicalConceptName(source: String): String =
        if (source.replace(Regex("\\s+"), "") == "परि+नम्+घञ्") "परिणाम" else source
}

object SamjnaValueClassifier {
    private val sankhyaEvaluator = SankhyaEvaluator()

    fun classifyTerm(term: String): SamjnaValueType =
        if (term.toLongOrNull() != null ||
            runCatching { sankhyaEvaluator.evaluateStems(listOf(term)).value }.getOrNull() != null
        ) {
            SamjnaValueType.SANKHYA
        } else {
            SamjnaValueType.SHABDA
        }

    fun classifyValue(value: SanskritValue): SamjnaValueType = when (value) {
        is SanskritValue.Sankhya, is SanskritValue.Rational, is SanskritValue.Range -> SamjnaValueType.SANKHYA
        is SanskritValue.Suchi, is SanskritValue.Gana -> SamjnaValueType.SUCHI
        else -> SamjnaValueType.SHABDA
    }
}

enum class SamjnaPrecedence(val rank: Int) {
    DEFAULT(0),
    NITYA(1),
    ANTARANGA(2),
    APAVADA(3),
}
